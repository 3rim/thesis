<template>
    <div class="flex flex-col flex-1 items-center py-8">
        <table>
            <thead class="bg-[#E4D4BA]">
                <tr>
                    <th class="px-2">Titel</th>
                    <th class="px-2">Gesamtanzahl</th>
                    <th class="px-2">Verfügbar</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="medium in inventoryList"
                 @click="goTo(medium.title)"
                 class="bg-[#F7F3E3] border-b-2 cursor-pointer">
                    <td class="text-center">{{ medium.title }}</td>
                    <td class="text-center">{{ medium.amount }}</td>
                    <td class="text-center">{{ medium.available }}</td>
                                 
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
const goTo = (title) => {
    router.push(`/inventory/title/${title}`)
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
