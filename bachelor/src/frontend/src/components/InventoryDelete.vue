<template>
    <div class="container flex flex-col items-center">
        <p class=" py-5 font-bold">Medien aus dem Inventar löschen</p>
        <!-- Warning -->
        <div>
            <div class="bg-red-500 text-white font-bold rounded-t px-4 py-2">
                Achtung
            </div>
            <div class="border border-t-0 border-red-400 rounded-b bg-red-100 px-4 py-3 text-red-700">
                <p>Das löschen eines Medium kann NICHT rückgängig gemacht werden.</p>
            </div>
        </div>

        <form @submit.prevent="deleteMedium(mediumID)" class="w-full max-w-lg py-4">
            <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="mediumID">Medium ID</label>
            <div class="flex flex-wrap ">
                <div class="w-5/6 pr-1 ">
                <input class="uppercase appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="mediumID" type="text" placeholder="medium ID"
                v-model="mediumID" required>
                </div>
                <button class=" w-1/6  bg-blue-500 hover:bg-blue-700 text-white font-bold  px-4 rounded">Löschen</button>

            </div>
        </form>
        
        <!-- ErrorMessage -->
        <div v-if="err" class=" w-full max-w-lg bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
            <strong class="font-bold">Löschen fehlgeschlagen! </strong>
            <span class="block sm:inline">{{ errorMessage }}.</span>
            <span class="absolute top-0 bottom-0 right-0 px-4 py-3">
                <button @click=" err =!err"><svg class="fill-current h-6 w-6 text-red-500" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><title>Close</title><path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 1 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 1 1 1.697 1.697l-2.758 3.152 2.758 3.15a1.2 1.2 0 0 1 0 1.698z"/></svg></button>
            </span>
        </div>
        <!-- SuccessMessage -->
        <div v-if="success" class="px-4 py-3 leading-normal text-green-700 bg-green-100 rounded-lg" role="alert">
            <p class="font-bold">Medium gelöscht!</p>
            <p>{{ successMessage }}!</p>
        </div>


    </div>
        
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";
import authHeader from '../services/authHeader';
const err = ref(false);
const errorMessage = ref("")
const success = ref(false);
const successMessage = ref("")


const mediumID =ref("");



const deleteMedium = (ID) =>{
    let config = {
                headers: authHeader(),
            }
    axios.delete(`/api/v1/inventory/${ID}`,config)
    .then(res => {
        successMessage.value = res.data;
        success.value = true;
        err.value = false;
        mediumID.value ="";
    }).catch(function (error){
                    if(error.response){
                        err.value = true;
                        success.value = false;
                        if(error.response.status == 404)
                            errorMessage.value = "Medien ID existiert nicht"
                        if(error.response.status == 400)
                            errorMessage.value = "Eingabe nicht verarbeitbar"
                        else
                        errorMessage.value = "Server fehler... Probieren Sie es später erneut"
                    }
                });

}


</script>