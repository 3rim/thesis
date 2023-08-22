<template>
    <!-- Generel data about Media -->
    <div class="px-4 py-1 bg-slate-50 rounded-md shadow-md">
        <p class=" text-gray-700 font-bold font-sans underline">Medienreihe-Informationen</p>

        <div class="text-gray-500">
            <p><strong>Titel: </strong>{{ title }} </p>
            <p><strong>ISBN/EAN: </strong> {{ ISBN_EAN }}</p>
            <p><strong>Original Preis: </strong> {{ originalPrice }}</p>
            <p><strong>Medien Typ: </strong> {{ mediaTyp }}</p>
            <p><strong>Jahrgänge: </strong>
                <span class="px-1" v-for="item in vintageArray">
                    {{item}}
                </span>
            </p>
            <p><strong>Fächer: </strong>
                <span class="px-1" v-for="item in subjectsArray">
                    {{item}}
                </span>
            </p>
        </div>
        <button
        type="button"
        @click="openModal"
        class="bg-gray-500 hover:bg-gray-700 text-white font-bold text-xs py-1 px-1 rounded-md"
      >
        Bearbeiten
        </button>
    </div>

    <!--Modal from HeadlessUI Framework-->
    <TransitionRoot appear :show="isOpen" as="template">
      <Dialog as="div"  class="relative z-10" @close="closeModal">
        <TransitionChild
          as="template"
          enter="duration-300 ease-out"
          enter-from="opacity-0"
          enter-to="opacity-100"
          leave="duration-200 ease-in"
          leave-from="opacity-100"
          leave-to="opacity-0"
        >
          <div class="fixed inset-0 bg-black bg-opacity-25" />
        </TransitionChild>
  
        <div class="fixed inset-0 overflow-y-auto">
          <div
            class="flex min-h-full items-center justify-center p-4 text-center"
          >
            <TransitionChild
              as="template"
              enter="duration-300 ease-out"
              enter-from="opacity-0 scale-95"
              enter-to="opacity-100 scale-100"
              leave="duration-200 ease-in"
              leave-from="opacity-100 scale-100"
              leave-to="opacity-0 scale-95"
            >
              <DialogPanel
                class="w-full max-w-lg transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all"
              >
                <DialogTitle
                  as="h3"
                  class="text-lg font-medium leading-6 text-gray-900"
                >
                 Medienreihe bearbeiten
                </DialogTitle>
                <div class="mt-2">
                  <form @submit="submitForm">
                    <div class="flex flex-wrap">
                        <!-- Titel -->
                        <div class="w-full md:w-1/2 px-3 mb-6 md:mb-0">
                        <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="title">
                            Titel
                        </label>
                        <input class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="title" type="text" placeholder="Titel des Medium"
                        v-model="title" required>
                        </div>
                        <!-- ISBN_EAN -->
                        <div class="w-full md:w-1/2 px-3">
                        <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="ISBN_EAN">
                            ISBN / EAN
                        </label>
                        <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="ISBN_EAN" type="text" placeholder="ISBN / EAN"
                        v-model="ISBN_EAN">
                        </div>
                        <!-- mediaTyp -->
                        <div class="w-full md:w-1/2 px-3">
                        <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="mediaTyp">
                            mediaTyp
                        </label>
                        <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="mediaTyp" type="text" placeholder="Medium Typ"
                        v-model="mediaTyp">
                        </div>

                        <div class="w-full md:w-1/2 px-3">
                        <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="originalPrice">
                            Originaler Preis €
                        </label>
                        <input class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" id="originalPrice" type="number"  min="0" step="0.01" placeholder="Originaler Preis"
                        v-model="originalPrice">
                        </div>  
                            
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
                    <div class="mt-4"> 
                    <button
                    class="inline-flex justify-center rounded-md border border-transparent mr-1 bg-blue-100  px-4 py-2 text-sm font-medium text-blue-900 hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2">
                    Speichern
                    </button> 
                  <button
                    type="button"
                    class="inline-flex justify-center rounded-md border border-transparent bg-red-100 px-4 py-2 text-sm font-medium text-red-900 hover:bg-red-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-red-500 focus-visible:ring-offset-2"
                    @click="closeModal"
                  >
                    Schließen
                  </button>
                </div>
                  </form>
                </div>
  
                
              </DialogPanel>
            </TransitionChild>
          </div>
        </div>
      </Dialog>
    </TransitionRoot>
</template>

<script setup>

import axios from 'axios' ;
import {ref} from 'vue'
import {useRoute} from "vue-router";
import authHeader from '../services/authHeader';
import {TransitionRoot,TransitionChild,Dialog,DialogPanel,DialogTitle,} from '@headlessui/vue';

const route = useRoute();
const isOpen = ref(false);
const mediaSeriesID = ref();
const title =ref("");
const ISBN_EAN =ref("");
const mediaTyp =ref("");
const originalPrice =ref(0.0);

const tempSubject = ref("");
const tempVintage =ref("");
const vintageArray =ref([]);
const subjectsArray =ref([]);

const closeModal= () =>  {
    isOpen.value = false

}
const openModal =() => {
    isOpen.value = true
}

const getMediaSeriesData = async () =>{
    let config = {
                headers: authHeader(),
            }
    try {
        const result = await axios.get(`/api/v1/inventory/series/${route.params.seriesID}`,config);
        const mediaSeries = result.data;

        mediaSeriesID.value =mediaSeries.id;
        title.value = mediaSeries.title;
        ISBN_EAN.value =mediaSeries.isbn_EAN;
        mediaTyp.value =mediaSeries.mediaTyp;
        originalPrice.value =mediaSeries.originalPrice;
        vintageArray.value =mediaSeries.vintage;
        subjectsArray.value =mediaSeries.subjects;
    } catch (error) {
        console.log(error);
    }
}
await getMediaSeriesData();

const submitForm = async ()  =>  {
    let config = {headers: authHeader()}

    try {
        const res = await axios.patch(`/api/v1/inventory/series/${mediaSeriesID.value}`,
        {
            "mediaTyp": mediaTyp.value,
            "vintage": vintageArray.value,
            "subjects": subjectsArray.value,
            "originalPrice": originalPrice.value,
            "title": title.value,
            "isbn_EAN": ISBN_EAN.value 
        },
        config);
        console.log(res)

    } catch (error) {
        // Handle errors
        console.log(error)
    }
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

