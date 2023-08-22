<template>
    <div class="container flex flex-col flex-1 items-center py-2">
        <!-- Generel data about Media -->
        <Suspense>
            <MediaSeriesInfoCard/>
        </Suspense>    
        <button 
        class="my-2 bg-red-500 enabled:hover:bg-red-700 text-white font-bold py-2 px-4 rounded-full  disabled:opacity-25  "
        :disabled="amount > 0"
        @click="deleteMediaSeries"
        >
            Medienreihe löschen
        </button>
    <!-- Tabelle -->
        <div class=" py-2">
            <table class="">
                <thead class="bg-[#E4D4BA]">
                    <tr class="">
                        <th  @click="sort" class="px-5" >Medium-ID</th>
                        <th class="px-4">Seriennummer</th>
                        <th class="px-4">Status</th>
                        <th class="px-20">Ausleiher</th>
                        <th class="px-2">Ausgeliehen seit</th>
                    </tr>
                </thead>
                <tbody v-if="mediaData">
                  <tr v-for="medium in mediaData.data"
                  class="bg-[#F7F3E3] border-b-2">
                    <td class="text-center ">{{ medium.mediumID }}</td>
                    <td class="text-center">{{ medium.serialNr }}</td>
                    <td class="text-center">{{ medium.status }}</td>
                    <td class="text-center cursor-pointer">{{ medium.currentBorrower }}</td>
                    <td class="text-center">{{ $dateStringToGermanFormat(medium.dateOfLend) }}</td>
                  </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>

import axios from 'axios' ;
import { ref } from 'vue';
import {useRoute} from "vue-router";
import { useRouter } from 'vue-router';
import authHeader from '../services/authHeader';
import MediaSeriesInfoCard from './MediaSeriesInfoCard.vue'


const router = useRouter();
const route = useRoute();
const amount = ref();

const getMediaSeriesMedia =async () => {
    let config = {headers: authHeader()}
    try {
        const result = await axios.get(`/api/v1/inventory/series/${route.params.seriesID}/media`,config);
        amount.value = result.data.length;
        return result;
    } catch (error) {
        console.log(error);
    }
};

const mediaData = await getMediaSeriesMedia();

const deleteMediaSeries = async ()  =>{
    let config = {headers: authHeader()}
    try {
        await axios.delete(`/api/v1/inventory/series/${route.params.seriesID}`,config);
        router.push("/inventory")
    } catch (error) {
        console.log(error);
    }
}

</script>
