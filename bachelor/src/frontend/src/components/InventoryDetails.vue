<template>
    <div class="container flex flex-col flex-1 items-center py-2">
        <!-- Generel data about Media -->
        <div class="bg-slate-300 px-4 py-1">
            <p> Titel: {{ mediaData.data[0].title }} </p>
            <p> ISBN: {{ mediaData.data[0].isbn }}</p>
            <p> Medien Typ: {{ mediaData.data[0].mediumTyp }}</p>
            <p> Jahrgänge: {{ mediaData.data[0].year }} </p>
            <p> Fächer: {{ mediaData.data[0].subjects }} </p>
        </div>
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
                <tbody>
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
        const title = route.params.title
        const result = await axios.get(
            `/api/v1/inventory/title/${title}`,config);
        //Hier haben wir ein JSON- Array mit mehreren Elementen,
        console.log(result.data[0].mediumID)
        console.log(result.data)
        return result;
    } catch (error) {
        console.log(error);
    }
};

const mediaData = await getMediaDataByTitle();


</script>
