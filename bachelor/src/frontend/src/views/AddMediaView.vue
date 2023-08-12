<template>
    <div class="container flex flex-col flex-1 items-center py-2 ">
        
        <Suspense>
            <MediaSeriesInfoCard/>
            <template #fallback>
                loading...
            </template>
        </Suspense>
        
        <!--Divider 
        Browsers display <hr> as an empty block element with automatic horizontal margins (that will center it if a width is specified). But when the <hr> becomes a flex item, the automatic width is pushed to 0. Disappearing from the view.
        ==> use width prop on hr to solve issue!
        --> 
        <hr class=" w-full my-8 bg-gray-300 border-0 dark:bg-gray-500 h-1">
        <MediaAdd />
    </div>
</template>

<script setup>
import { ref } from 'vue';
import MediaAdd from '../components/MediaAdd.vue';
import axios from 'axios';
import authHeader from '../services/authHeader';
import {useRoute} from "vue-router";
import MediaSeriesInfoCard from '../components/MediaSeriesInfoCard.vue';

const route = useRoute();
const response = ref();
const getSeriesData = () =>{
    let config = {
                headers: authHeader()
            }
    axios.get(`/api/v1/inventory/series/${route.params.seriesID}`,config)
    .then(res => {
        console.log(res);
        response.value = res.data;
    });

}
getSeriesData();



</script>