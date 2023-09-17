<template>
    <div>
        <div class=" container pt-4 mb-8  flex" v-show="$route.path === '/inventory'">
            <font-awesome-icon class="py-2 px-1" icon="fa-solid fa fa-barcode" size="2x" />
            <div v-if="!searchBySerialNr" class="w-full">
                <input type="text" placeholder="Mediencode einscannen oder tippen" v-model="mediaID" @keyup.enter="getMedia(mediaID)"
                    class="py-2 px-1 w-full bg-transparent border-b
                    focus:border-b-gray-600
                    focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]" />
            </div>
            <!--search by serialNr input field -->
            <div v-else class="w-full">
                <input type="text" 
                placeholder="Suche über Seriennummer" 
                v-model="serialNrSearchQuery"
                @input="getMediaBySerienNr()"
                    class="py-2 px-1 w-full bg-transparent border-b
                    focus:border-b-gray-600
                    focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]" />
                <ul class="bg-[#D0B591] text-white w-full shadow-md py-2 px-1 top-[66px]" v-if="serialNrSearchResult">
                    <p v-if="serialNrSearchResult.length === 0"> Keine Ergebnisse gefunden </p>
                    <template v-else>
                        <li v-for="medium in serialNrSearchResult" :key="medium.id" class="py-2 cursor-pointer hover:bg-[#A8763E] "
                            @click="getMedia(medium.mediumID)">
                            ID:{{ medium.mediumID }} SN:{{ medium.serialNr }} {{ medium.title }}
                        </li>
                    </template>
                </ul>
            </div>

            <div v-if="!searchBySerialNr">
                <button @click="getMedia" type="submit"
                    class="py-2 px-1 bg-[#AE857C] text-white hover:bg-[#6F1A07]">Suchen</button>
            </div>
        </div>
        <div v-show="$route.path === '/inventory'" class=" text-center text-cyan-700 hover:cursor-pointer ">
            <p v-if="!searchBySerialNr" @click="searchBySerialNr = !searchBySerialNr">Suche über Seriennummer</p>
            <p v-else @click="searchBySerialNr = !searchBySerialNr"> Suche über Mediencode</p>
        </div>

        <Suspense>
            <AsyncInventoryList v-show="$route.path === '/inventory'" />
            <template #fallback>
                <p>Loading...</p>
            </template>
        </Suspense>
        <RouterView />

    </div>
</template>

<script setup>
import AsyncInventoryList from '@/components/AsyncInventoryList.vue';
import authHeader from '../services/authHeader';
import axios from "axios";
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const queryTimeout = ref();
const mediaID = ref("");
const searchBySerialNr = ref(false);
const serialNrSearchQuery = ref("");
const serialNrSearchResult = ref(null);
const router = useRouter();


const getMedia = (id) => {
    router.push(`/inventory/${id}`)
    mediaID.value = "";
}

const getMediaBySerienNr = () => {
    console.log("searching...")
    clearTimeout(queryTimeout.value);
    queryTimeout.value = setTimeout(async () => {
        if (serialNrSearchQuery.value !== "") {
            let config = {
                headers: authHeader(),
                params: { serialNr: serialNrSearchQuery.value }
            }
            const response = await axios.get('/api/v1/inventory/serialNr', config);
            serialNrSearchResult.value = response.data;
            return;
        }
        serialNrSearchResult.value = null;
    }, 300);
}
</script>
