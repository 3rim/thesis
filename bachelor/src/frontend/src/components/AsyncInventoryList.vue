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
                 class=" text-center hover:bg-[#ede6cb] bg-[#F7F3E3] border-b-2 cursor-pointer">
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
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import authHeader from '../services/authHeader';

const router = useRouter();
const goTo = (mediaSeriesID) => {
    router.push(`/inventory/series/${mediaSeriesID}`)
}

const inventoryList = ref(null);

const getInventoryData =async () => {
    let config = {
                headers: authHeader()
            }
    try {
        const inventoryData = await axios.get(
            `/api/v1/inventory`,config
        );
        inventoryList.value = inventoryData.data
        return inventoryData;
    } catch (error) {
        console.log(error);
    }
};

await getInventoryData();  

</script>
