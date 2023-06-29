<template>
    <div class="container flex flex-col items-center">
        <p class="">Allgemeine Informationen</p>
        <form @submit.prevent="submitForm" class="w-full max-w-lg py-4">
            <!-- Generell Infos -->
            <div class="flex flex-wrap -mx-3 mb-6">
                <!-- Titel -->
                <div class="w-full md:w-1/2 px-3 mb-6 md:mb-0">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="title">
                    Titel
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="title" type="text" placeholder="Titel des Medium"
                v-model="title" required>
                
                </div>
                <!-- ISBN -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="isbn">
                    ISBN / EAN
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="isbn" type="text" placeholder="ISBN / EAN"
                v-model="ISBN">
                </div>
                
                <!-- MediumTyp -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="mediumTyp">
                    MediumTyp
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="mediumTyp" type="text" placeholder="Medium Typ"
                v-model="mediumTyp">
                </div>
                <!-- Preis -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="originalPrice">
                    Originaler Preis €
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="originalPrice" type="number"  min="0" step="0.01" placeholder="Originaler Preis"
                v-model="originalPrice">
                </div>     
                <!-- Jahrgänge -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="vintage">
                    Jahrgang
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="vintage" type="text" placeholder="Jahrgang"
                v-model="tempVintage">
                <p v-if="tempVintage.length">{{ tempVintage }} <button @click="addVintage" class="font-bold"> hinzufügen </button></p>
                    <div v-for="vintage in vintageArray" 
                    class="text-xs font-semibold inline-block py-1 px-2  rounded-full text-sky-600 bg-sky-200  last:mr-0 mr-1 hover:cursor-pointer"> 
                        <span @click="deleteVintage(vintage)">{{ vintage }}</span>
                    </div>
                </div>

                <!-- Fächer -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="grid-subjects">
                    Fächer
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="grid-subjects" type="text" placeholder="Fächer" 
                v-model="tempSubject">
                <p v-if="tempSubject.length">{{ tempSubject }} <button @click="addSubject" class="font-bold"> hinzufügen </button></p>
                    <div v-for="subject in subjectsArray" 
                    class="text-xs font-semibold inline-block py-1 px-2  rounded-full text-sky-600 bg-sky-200  last:mr-0 mr-1 hover:cursor-pointer"> 
                        <span @click="deleteSubject(subject)">{{ subject }}</span>
                    </div>
                </div>   
            </div>
            <!--Divider -->
            <hr class="my-8 bg-gray-300 border-0 dark:bg-gray-500 h-1">
            <!--Dynamic Infos-->
            <p  class="container flex flex-col items-center" >Einzigartige Infos</p>

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
        <p v-if="inventoriedMediaArray.length"> Inventarisierte Medien </p>
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

const title =ref("");
const ISBN =ref("");
const mediumTyp =ref("");
const mediumID =ref("");
const serialNr =ref("");
const originalPrice =ref(0.0);

const tempSubject = ref("");
const tempVintage =ref("");
const vintageArray =ref([]);
const subjectsArray =ref([]);

const checked =ref(false);
const inventoriedMediaArray = ref([]);

const deleteMedium = (mediumID) =>{
    console.log(mediumID)
    const response = axios.delete(`/api/v1/inventory/${mediumID}`)
    .then(res => {
        console.log(res)
        let i = inventoriedMediaArray.value.map(item => item.mediumID).indexOf(mediumID)
        console.log(i)
        inventoriedMediaArray.value.splice(i,1)
        console.log(inventoriedMediaArray.value)
    })
}


const submitForm = () => {
    if(mediumID.value.length){

    }
    const response = axios.post('/api/v1/inventory',
    {
        "mediumID": mediumID.value,
        "mediumTyp": mediumTyp.value,
        "serialNr": serialNr.value,
        "year": vintageArray.value,
        "subjects": subjectsArray.value,
        "originalPrice": originalPrice.value,
        "title": title.value,
        "dateOfLend": "",
        "isbn": ISBN.value
    }).then(res => {
        const data = res.data;
        console.log(data.mediumID)
        const medium = {
            mediumID : data.mediumID,
            serialNr : data.serialNr,
        }
        //Unshift => put element at the beginning of array
        console.log("Medium:")
        console.log(medium)
        inventoriedMediaArray.value.unshift(medium);
        console.log("inventoriedMediaArray.value")
        console.log(inventoriedMediaArray.value)
    })
    

    //Reset mediumID and serialNr
    mediumID.value = "";
    serialNr.value = "";

}

const addSubject = () =>{
    if( tempSubject.value){
        if(!subjectsArray.value.includes(tempSubject.value)){
            subjectsArray.value.push(tempSubject.value);
        }
        tempSubject.value = '';
    }
}

const addVintage = () =>{
    if( tempVintage.value){
        if(!vintageArray.value.includes(tempVintage.value)){
            vintageArray.value.push(tempVintage.value);
        }
        tempVintage.value = '';
    }
}

//Delete subject by clicking on it
const deleteSubject = (subject) =>{
    subjectsArray.value = subjectsArray.value.filter((item) =>{
        return subject !== item;
    })
}
//Delete vintage by clicking on it
const deleteVintage = (vintage) =>{
    vintageArray.value = vintageArray.value.filter((item) =>{
        return vintage !== item;
    })
}

</script>