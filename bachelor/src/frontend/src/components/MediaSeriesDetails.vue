<template>
    <div class="container flex flex-col flex-1 items-center py-2">
        <!-- Generel data about Media -->
        <Suspense>
            <MediaSeriesInfoCard/>
        </Suspense>

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
import {useRoute} from "vue-router";
import { useRouter } from 'vue-router';
import authHeader from '../services/authHeader';
import MediaSeriesInfoCard from './MediaSeriesInfoCard.vue';

const router = useRouter();
const route = useRoute();

//TODO:Sorting server-side instead client site 
const sort = () =>{
    const result =   mediaData.data.sort((x,y) => (x["mediumID"] < y["mediumID"] ? -1:1))
    console.log(result);
}



const getMediaDataByTitle =async () => {
    let config = {
                headers: authHeader(),
            }
    try {
        const result = await axios.get(
            `/api/v1/inventory/series/${route.params.seriesID}/media`,config);
        //Hier haben wir ein JSON- Array mit mehreren Elementen,
        console.log(result.data[0].mediumID)
        console.log(result.data)
        return result;
    } catch (error) {
        console.log(error);
    }
};

const getMediaSeriesData = async () =>{
    let config = {
                headers: authHeader(),
            }
    try {
        const result = await axios.get(
            `/api/v1/inventory/series/${route.params.seriesID}`,config);  
        console.log(result.data)
        return result.data;
    } catch (error) {
        console.log(error);
    }
}


const mediaData = await getMediaDataByTitle();
const mediaSeries = await getMediaSeriesData();
console.log("SeriesDaa#ta")
console.log(mediaSeries)




</script>
