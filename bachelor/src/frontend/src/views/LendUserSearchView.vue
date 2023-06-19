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
            <p v-if="userSearchResults.length === 0"> Keine Ergebnisse gefunden </p> 
            <template v-else>
            <li v-for="user in userSearchResults"
            :key="user.id"
            class="py-2 cursor-pointer"
            @click="getUser(user)"
            >
            {{ user.firstName}} {{ user.lastName}}<!-- Typescript issue-->
            </li>
            </template>
            
         </ul>
    </div>
</main>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from "axios";
import { useRouter } from 'vue-router';
import type { PropType } from "vue";


const router = useRouter();
const getUser = (user:any) => {
    console.log(user);
    console.log(user.id);
    router.push({
        name: 'ausleiheUser',
        query:{
           id: user.id,
        }
    })

};

const searchQuery = ref("");
const queryTimeout = ref<number>();
const userSearchResults = ref(null);

const getSearchResults = () =>{
    console.log("getResults");
    clearTimeout(queryTimeout.value);
    queryTimeout.value = setTimeout(async () => {
        if(searchQuery.value !== ""){
            
            const [firstName, lastName] = splitFirstAndLastName(searchQuery.value);
            const result = await axios.get('/api/v1/user',{params:{firstName:firstName,lastName:lastName}});
            userSearchResults.value = result.data;
            console.log(userSearchResults.value);
            return;
        }
        userSearchResults.value = null;
    },300);
}

function splitFirstAndLastName(str:string){
    const fullName = str;
    const lastIndex = fullName.lastIndexOf(" ");

    let firstName = "";
    let lastName = "";
    //Split Fullname on last whitespace
    if(lastIndex !== -1){
        firstName = fullName.slice(0,lastIndex);
        lastName = fullName.slice(lastIndex + 1);
    }
    else{ //only one name entered
        firstName = str;
    }
    console.log("firstName:" + firstName)
    console.log("lastName:" + lastName)
    return [firstName,lastName]
}


</script>

<style scoped>

</style>