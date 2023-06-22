<template>
    <div>
        <table>
            <thead>
                <tr>
                    <th>Titel</th>
                    <th>Gesamtanzahl</th>
                    <th>Verfügbar</th>
                </tr>
            </thead>
            <tbody>

            <tr v-for="medium in inventoryList" :key="medium.title"
                  class="bg-slate-500">
                    <td>{{ medium.title }}</td>
                    <td>{{ medium.amount }}</td>
                    <td>{{ medium.available }}</td>
                  </tr>
                </tbody>
        </table>
    </div>
</template>

<script setup>
import axios from 'axios' ;
import {useRoute} from "vue-router";
import { ref } from 'vue';

const route = useRoute();
const inventoryList = ref(null);

const getInventoryData =async () => {
    try {
        const inventoryData = await axios.get(
            `/api/v1/inventory`
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
