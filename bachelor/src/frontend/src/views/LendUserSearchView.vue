<template>
<main class="container text-black">
    <!--nicht relativ machen sonst klappt der dropdown overlay nicht -->
    
    <div class=" pt-4 mb-8 flex"> 
        <font-awesome-icon class="py-2 px-1" icon="a-solid fa fa-user" size="2x"></font-awesome-icon>
        <div class="w-full">
            <input 
        type="text" 
        placeholder="Suche User"
        v-model="searchQuery"
        @input="getSearchResults()"
        class="py-2 px-1 w-full bg-transparent border-b
         focus:border-b-gray-800 
         focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]"/>
         <ul class="bg-[#D0B591] text-white w-full shadow-md
         py-2 px-1 top-[66px]"
         v-if="userSearchResults">
            <p v-if="userSearchResults.length === 0"> Keine Ergebnisse gefunden </p> 
            <template v-else>
            <li v-for="user in userSearchResults"
            :key="user.id"
            class="py-2 cursor-pointer hover:bg-[#A8763E] "
            @click="getUser(user)"
            >
            {{ user.firstName}} {{ user.lastName}} {{ user.borrowerGroup }} 
            <span v-if="user.leftTheSchool === true"
            class="text-[#6F1A07] font-bold px-3">
                 Hat die Schule verlassen</span>
            </li>
            </template>
         </ul>
        </div>
       
    </div>
</main>
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";
import { useRouter } from 'vue-router';
import authHeader from '../services/authHeader';

const router = useRouter();
const getUser = (user) => {
    router.push({
        name: 'ausleiheUser',
        query:{
           id: user.id,
        }
    })

};

const searchQuery = ref("");
const queryTimeout = ref();
const userSearchResults = ref(null);

const getSearchResults = () =>{
    clearTimeout(queryTimeout.value);
    queryTimeout.value = setTimeout(async () => {
        if(searchQuery.value !== ""){
            
            const [firstName, lastName] = splitFirstAndLastName(searchQuery.value);
            let config = {
                headers: authHeader(),
                params: {firstName:firstName,lastName:lastName}
            }
            const result = await axios.get('/api/v1/borrowers/name',config);
            userSearchResults.value = result.data;
            console.log(userSearchResults.value)
            return;
        }
        userSearchResults.value = null;
    },300);
}

function splitFirstAndLastName(str){
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
    return [firstName,lastName]
}
</script>