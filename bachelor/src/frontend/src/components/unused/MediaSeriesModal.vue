<template>
  <div class="flex flex-col">
    <p class=" text-center">Generelle Informationen der neuen Medienreihe</p>
    <form>
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
    </form>
    
    <button @click="$emit('close')">Close</button>
  </div>

</template>
<script setup>
import { ref,defineProps,  } from 'vue'
const emit = defineEmits(['close'])

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