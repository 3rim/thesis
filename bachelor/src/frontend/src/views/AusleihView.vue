<template>
<main class="container text-white">
    <!--nicht relativ machen sonst klappt der dropdown overlay nicht -->
    <div class=" pt-4 mb-8 "> 
        <input 
        type="text" 
        placeholder="Suche User"
        v-model="searchQuery"
        @input="getSearchResults()"
        class="py-2 px-1 w-full bg-transparent border-b
         focus:border-b-gray-600 
         focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]"/>
         <ul class="bg-blue-950 text-white w-full shadow-md
         py-2 px-1 top-[66px]"
         v-if="userSearchResults">
            <li v-for="user in userSearchResults"
            :key="user.id"
            class="py-2 cursor-pointer"
            >
            {{ user.firstName }} <!-- Typescript issue-->
            </li>
         </ul>
    </div>
</main>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from "axios";

const searchQuery = ref("");
const queryTimeout = ref<number>();
const userSearchResults = ref(null);

const getSearchResults = () =>{
    console.log("getResults");
    clearTimeout(queryTimeout.value);
    queryTimeout.value = setTimeout(async () => {
        if(searchQuery.value !== ""){
            const result = await axios.get('/api/v1/user',{params:{firstName:"Max"}});
            const result2 = await axios.get('/api/v1/user/1');
            userSearchResults.value = result.data
            console.log(userSearchResults);
            console.log(result2.data.firstName);
            console.log()
            return;
        }
        userSearchResults.value = null;
    },300);
}


</script>

<style scoped>

</style>