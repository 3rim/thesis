<template>
    <div class="container flex flex-col items-center">
        <form @submit.prevent="addMedium" class="w-full max-w-lg py-4">
            <!--Dynamic Infos-->
            <input type="checkbox" id="checkbox" v-model="checked" />
            <label for="checkbox"> Mit Seriennummer inventarisieren</label>
            <div class="flex flex-wrap -mx-3 mb-6">
                <!-- MediumID -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="mediumID">
                    Medium ID
                </label>
                <input class="uppercase appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="mediumID" type="text" placeholder="medium ID"
                v-model="mediumID" required>
                </div>
                
                <!-- SerialNr -->
                <div class="w-full md:w-1/2 px-3" v-if="checked">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="serialNr">
                    Seriennummer
                </label>
                <input class=" uppercase appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500 focus:uppercase" id="serialNr" type="text" placeholder="Seriennummer"
                v-model="serialNr" >
                </div>
                
            </div>
            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Hinzufügen</button>
        </form>

        <!-- List -->
        <p
        class="font-bold text-lg"
        v-if="inventoriedMediaArray.length"
        > 
            Hinzugefügte Medien
        </p>
        <!-- ErrorMessage -->
        <div v-if="err" class=" w-full max-w-lg bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
            <strong class="font-bold">Inventarisierung fehlgeschlagen! </strong>
            <span class="block sm:inline"><p>{{ errorMessage }}</p></span>
            <span class="absolute top-0 bottom-0 right-0 px-4 py-3">
                <button @click=" err =!err"><svg class="fill-current h-6 w-6 text-red-500" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><title>Close</title><path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 1 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 1 1 1.697 1.697l-2.758 3.152 2.758 3.15a1.2 1.2 0 0 1 0 1.698z"/></svg></button>
            </span>
        </div>

        <!-- added Media container -->
        <div class=" max-w-lg w-full py-2"
        v-if="inventoriedMediaArray.length">
            <div class="flex flex-row text-gray-700 text-s font-bold">
                <span class="w-1/2"> Medien ID</span> <span class="w-1/2"> Seriennummer</span>
            </div>
            <ul class="bg-gray-200  w-full shadow-md py-2 px-1 top-[66px]">
                <li v-for="medium in inventoriedMediaArray"
                class="py-2 ">
                    <div class="flex flex-row">
                        <div class="w-5/6 flex flex-row" >                                  <!--flex justify-end => position right -->
                            <div class="w-1/2 "> {{ medium.mediumID}}</div> <div class="w-1/2 pl-10 "> {{ medium.serialNr}}</div>
                        </div>
                      <button @click="deleteMedium(medium.mediumID)" class="w-1/6  hover:bg-slate-100">Löschen</button>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";
import authHeader from '../services/authHeader';
import { useRoute } from 'vue-router';

const route = useRoute();
const err = ref(false);
const errorMessage = ref("")
const checked =ref(false);
const inventoriedMediaArray = ref([]);
const mediumID = ref();
const serialNr = ref();

const addMedium = () =>{
    console.log(mediumID)
    let config = {
                headers: authHeader()
            }
    axios.post(`/api/v1/inventory/series/${route.params.seriesID}/media`,
    {
        "mediumID": mediumID.value,
        "serialNr": serialNr.value
    },config)
    .then(res => {
        console.log(res)
        const data = res.data;
        console.log(data.mediumID)
        const medium = {
            mediumID : data.mediumID,
            serialNr : data.serialNr,
        }
        //Unshift => put element at the beginning of array
        inventoriedMediaArray.value.unshift(medium);
    }).catch(function (error){
                    if(error.response){
                        console.log(error.response.data)
                        err.value = true;
                        errorMessage.value = error.response.data.message
                        if(error.response.status === 400){
                        errorMessage.value ="Anfrage konnte nicht verarbeitet werden"
                        }
                        if(error.response.status === 404){
                            console.log(error)
                            errorMessage.value =error.response.data.message
                        }
                        if(error.response.status === 406){
                            console.log(error)
                            errorMessage.value =error.response.data.message
                        }
                        if(error.response.status === 500){
                            errorMessage.value ="Server fehler... Versuchen Sie es erneut"
                        }
                        console.log(error.response.data.message)
                    }
                });
    //Reset
    mediumID.value = "";
    serialNr.value = "";
    err.value = false
}

const deleteMedium = (mediumID) =>{
    console.log(mediumID)
    let config = {
                headers: authHeader()
            }
    axios.delete(`/api/v1/inventory/${mediumID}`,config)
    .then(res => {
        let i = inventoriedMediaArray.value.map(item => item.mediumID).indexOf(mediumID)
        inventoriedMediaArray.value.splice(i,1)
    })
}

</script>