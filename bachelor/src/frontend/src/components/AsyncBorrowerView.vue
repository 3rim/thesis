<template>
    <div class="flex flex-col flex-1 items-center">
        <!-- User  -->
        <div>
            <p class="py-1 text-white font-bold"> {{borrowerData.data.firstName}} {{borrowerData.data.lastName}}</p>
        </div>

        <!-- Medialist-->
        <div>
            <table class="">
                <thead class="bg-gray-50">
                    <tr>
                        <th >MediumID</th>
                        <th >MediumTyp</th>
                        <th >ISBN</th>
                        <th >Seriennummer</th>
                    </tr>
                </thead>
                <tbody>
                  <tr v-for="medium in mediaList" :key="medium.id"
                  class="bg-slate-500">
                    <td>{{ medium.mediumID }}</td>
                    <td>{{ medium.title }}</td>
                    <td>{{ medium.mediumTyp }}</td>
                    <td>{{ medium.isbn }}</td>
                    <td>{{ medium.serialNr }}</td>
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

const componentKey = ref(0);

const forceRerender = () => {
  componentKey.value += 1;
};

const route = useRoute();
const mediaList = ref(null);
const getBorrowerData =async () => {
    try {
        const borrowerData = await axios.get(
            `/api/v1/user/${route.query.id}`
        );
        console.log(borrowerData);
        mediaList.value = borrowerData.data.mediumList
        return borrowerData;
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

