<template>
    <div >
        <div class=" container pt-4 mb-8  flex"
        v-show="$route.path==='/inventory'">
            <font-awesome-icon class="py-2 px-1" icon="fa-solid fa fa-barcode" size="2x" />
            <div>
            </div>
            <div class="w-full">
                <input 
                type="text" 
                placeholder="Mediencode einscannen oder tippen"
                v-model="mediaID"
                @keyup.enter="getMedia"
                class="py-2 px-1 w-full bg-transparent border-b
                focus:border-b-gray-600 
                focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]"/> 
            </div>
            <div>
                <button 
                @click="getMedia" 
                type="submit" 
                class="py-2 px-1 bg-[#AE857C] text-white hover:bg-[#6F1A07]">Suchen</button>
            </div>    
        </div>
        <Suspense>
            <AsyncInventoryList 
            v-show="$route.path==='/inventory'" />
            <template #fallback>
            <p>Loading...</p>
            </template>
        </Suspense>
    <RouterView />
    
    </div>
</template>

<script setup>
import AsyncInventoryList from '@/components/AsyncInventoryList.vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue';


const mediaID = ref("");

const router = useRouter();
const getMedia = () => {
    
    router.push(`/inventory/${mediaID.value}`)
    mediaID.value ="";
}
</script>
