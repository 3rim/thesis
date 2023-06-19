<template>
    <div class="flex flex-col flex-1 items-center">
        <!-- User  -->
        <div>
            <p class="py-1 text-white font-bold"> {{borrowerData.data.firstName}} {{borrowerData.data.lastName}}</p>
        </div>

        <!-- Medialist-->
        <div></div>
    </div>
</template>

<script setup lang="ts">
import axios from 'axios' ;
import {useRoute} from "vue-router";

const route = useRoute();
const getBorrowerData =async () => {
    try {
        const borrowerData = await axios.get(
            `/api/v1/user/${route.query.id}`
        );
        console.log(borrowerData);
        return borrowerData;
    } catch (error) {
        console.log(error);
    }
};

const borrowerData = await getBorrowerData();    
console.log(borrowerData);


</script>

