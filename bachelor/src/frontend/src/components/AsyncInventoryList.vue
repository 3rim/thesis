<template>
    <div class="flex flex-col flex-1 items-center py-8">
        <table>
            <thead class="bg-[#E4D4BA]">
                <tr class="">
                    <th class="px-2">Titel</th>
                    <th class="px-2">ISBN/EAN</th>
                    <th class="px-2">Medien Typ</th>
                    <th class="px-2">Fächer</th>
                    <th class="px-2">Jahrgänge</th>
                    <th class="px-2">Bestand</th>
                    <th class="px-2">Verfügbar</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="mediaSeries in inventoryList"
                 @click="goTo(mediaSeries.id)"
                 class=" text-center bg-[#F7F3E3] border-b-2 cursor-pointer">
                    <td class="">{{ mediaSeries.title }}</td>
                    <td class="">{{ mediaSeries.isbn_EAN }}</td>
                    <td class="">{{ mediaSeries.mediaTyp }}</td>
                    <td class="">
                        <span class="px-1" v-for="item in mediaSeries.subjects" :key="item.id">
                            {{ item }}
                        </span>
                    </td>
                    <td class="">
                        <span class="px-1" v-for="item in mediaSeries.vintage" :key="item.id">
                            {{ item }}
                        </span>
                    </td>
                    <td class="">{{ mediaSeries.amount }}</td>
                    <td class="">{{ mediaSeries.available }}</td>
                </tr>
             <!-- 
                <router-link 
                v-for="medium in testData" 
                    :to="{ name: 'inventory', params: { title: medium.title} }"
                    tag="tr"
                    >
                    <td>{{ medium.title }}</td>
                    <td>{{ medium.amount }}</td>
                    <td>{{ medium.available }}</td>               
                </router-link>
                -->
                </tbody>
        </table>
    </div>
</template>

<script setup>
import axios from 'axios' ;
import {useRoute} from "vue-router";
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import authHeader from '../services/authHeader';

const router = useRouter();
const goTo = (mediaSeriesID) => {
    router.push(`/inventory/series/${mediaSeriesID}`)
} 

const route = useRoute();
const inventoryList = ref(null);

const getInventoryData =async () => {
    let config = {
                headers: authHeader()
            }
    try {
        const inventoryData = await axios.get(
            `/api/v1/inventory`,config
        );
        console.log(inventoryData);
        inventoryList.value = inventoryData.data
        console.log(inventoryList.value);
        return inventoryData;
    } catch (error) {
        console.log(error);
    }
};


const inventoryData = await getInventoryData();  

const testData = ref([
            { title: "IPad", amount: 1, available: 0 },
            { title: "Java", amount: 2, available: 0},
            { title: "sql", amount: 4,available: 0},
            
        ]);
</script>
