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
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-red-500 rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="title" type="text" placeholder="Titel des Medium"
                v-model="title">
                <p class="text-red-500 text-xs italic">Bitte Feld ausfüllen.</p>
                </div>
                <!-- ISBN -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="isbn">
                    ISBN / EAN
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="isbn" type="text" placeholder="ISBN / EAN"
                v-model="ISBN">
                </div>
                <!-- Jahrgänge -->
                <div class="w-full md:w-1/2 px-3">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="vintage">
                    Jahrgang
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="vintage" type="text" placeholder="Jahrgang"
                v-model="tempVintage"
                @keyup.enter="addVintage">
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
                v-model="tempSubject"
                @keyup.enter="addSubject">
                    <div v-for="subject in subjectsArray" 
                    class="text-xs font-semibold inline-block py-1 px-2  rounded-full text-sky-600 bg-sky-200  last:mr-0 mr-1 hover:cursor-pointer"> 
                        <span @click="deleteSubject(subject)">{{ subject }}</span>
                    </div>
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
                    Originaler Preiss €
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="originalPrice" type="number"  min="0" step="0.01" placeholder="Originaler Preis"
                v-model="originalPrice">
                </div>        
            </div>
            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Hinzufügen</button>
            
        </form>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const title =ref("");
const ISBN =ref("");
const mediumTyp =ref("");
const originalPrice =ref<Number>(0.0);

const tempSubject = ref("");
const tempVintage =ref("");
const vintageArray =ref([]);
const subjectsArray =ref([]);

const submitForm = () => {
    console.log("submit form");

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