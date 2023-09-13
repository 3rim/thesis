<template>
    <div class="flex flex-col flex-1 items-center">
        <!-- User  -->
        <div v-if="borrowerData.borrowerState == 'DEACTIVATED'">
            <p class="text-[#6F1A07] font-bold underline">Nicht mehr auf der Schule!</p>
        </div>
        <div class="py-1 ">
            <p class=""><span class="font-bold">Vorname: </span> {{borrowerData.firstName}}</p>
            <p class=""><span class="font-bold">Nachname: </span> {{borrowerData.lastName}}</p>
            <p class=""><span class="font-bold">Gruppe: </span> {{borrowerData.borrowerGroup}}</p>
            <p class=""><span class="font-bold">Geb.: </span> {{ $dateStringToGermanFormat(  borrowerData.dob)}}</p>
        </div>

        <!-- Medialist-->
        <div>
            <table class="">
                <thead class="bg-[#E4D4BA]">
                    <tr class="">
                        <th class="px-2" >MediumID</th>
                        <th class="px-2">Medium Titel</th>
                        <th class="px-2">Seriennummer</th>
                        <th class="px-2">Ausgegeben am</th>
                    </tr>
                </thead>
                <tbody>
                  <tr v-for="medium in mediaList" :key="medium.id"
                  class="bg-[#F7F3E3]">
                    <td class="border-collapse border border-slate-400">{{ medium.mediumID }}</td>
                    <td class="border-collapse border border-slate-400">{{ medium.title }}</td>
                    <td class="border-collapse border border-slate-400">{{ medium.serialNr }}</td>
                    <td class="border-collapse border border-slate-400">{{ $dateStringToGermanFormat(medium.dateOfLend) }}</td>
                  </tr>
                </tbody>
            </table>

        </div>
    </div>
</template>

<script setup>
import axios from 'axios' ;
import {useRoute} from "vue-router";
import { ref } from 'vue';
import authHeader from '../services/authHeader';

const props = defineProps(['userId']);
const route = useRoute();
const mediaList = ref(null);
const getBorrowerData =async () => {
    try {
        const borrowerData = await axios.get(
            `/api/v1/borrowers/${props.userId}`,
            {headers:authHeader()}
        );
        console.log(borrowerData);
        mediaList.value = borrowerData.data.mediumList
        return borrowerData.data;
    } catch (error) {
        console.log(error);
    }
};

const borrowerData = await getBorrowerData();   


const testData = ref([
            { mediumID: 0, mediumTyp: 'mediumTyp', serialNr: 'frank.murphy@test.com', isbn: 'User' },
            { mediumID: 1, mediumTyp: 'mediumTyp', serialNr: 'frank.murphy@test.com', isbn: 'User' },
            { mediumID: 2, mediumTyp: 'mediumTyp', serialNr: 'frank.murphy@test.com', isbn: 'User' },
            
        ]);
</script>

