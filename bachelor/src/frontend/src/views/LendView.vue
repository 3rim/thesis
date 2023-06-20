<template>
    <div class="container">
        <div class=" pt-4 mb-8 ">
        
                <input 
        type="text" 
        placeholder="Mediencode einscannen oder tippen"
        v-model="mediaID"
        class="py-2 px-1 w-full bg-transparent border-b
         focus:border-b-gray-600 
         focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]"/> 
         
         <button @click="lendMedia" type="submit" class="bg-white">Ausleihen</button>
    
        </div>
       
        <Suspense>
        <AsyncBorrowerView :key="componentKey" />
        <template #fallback>
            <p>loadning...</p>
        </template>
        </Suspense>
    </div>
</template>

<script setup lang="ts">
import AsyncBorrowerView from '../components/AsyncBorrowerView.vue';

import { ref } from 'vue';
import axios from "axios";
import { useRouter } from 'vue-router';
import {useRoute} from "vue-router";


const componentKey = ref(0);
//Re-render component if necessary
const forceRerender = () => {
  componentKey.value += 1;
  console.log("rerender"+componentKey)
};


const router = useRouter();
const route = useRoute();

const mediaID = ref("");
const queryTimeout = ref<number>();



const lendMedia = () =>{
    queryTimeout.value = setTimeout(async () => {
        if(mediaID.value !== ""){
            const borrowerID = route.query.id;
            const result = await axios.post('/api/v1/loan',null,{params:{borrowerID:borrowerID,mediumID:mediaID.value}});
            forceRerender();
            //reset mediaID to blank
            mediaID.value = "";
        }
    },);
}

</script>
