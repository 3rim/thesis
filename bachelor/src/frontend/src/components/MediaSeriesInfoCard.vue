<template>
    <!-- Generel data about Media -->
    <div class="px-4 py-1 bg-slate-50 rounded-md shadow-md">
        <p class=" text-gray-700 font-bold font-sans underline">Medienreihe-Informationen</p>

        <div class="text-gray-500">
            <p><strong>Titel: </strong>{{ mediaSeries.title }} </p>
            <p><strong>ISBN: </strong> {{ mediaSeries.isbn_EAN }}</p>
            <p><strong>Medien Typ: </strong> {{ mediaSeries.mediumTyp }}</p>
            <p><strong>Jahrgänge: </strong>
                <span class="px-1" v-for="item in mediaSeries.vintage">
                    {{item}}
                </span>
            </p>
            <p><strong>Fächer: </strong>
                <span class="px-1" v-for="item in mediaSeries.subjects">
                    {{item}}
                </span>
            </p>
        </div>
    </div>
</template>

<script setup>

import axios from 'axios' ;
import {useRoute} from "vue-router";
import authHeader from '../services/authHeader';

const route = useRoute();

const getMediaSeriesData = async () =>{
    let config = {
                headers: authHeader(),
            }
    try {
        const result = await axios.get(`/api/v1/inventory/series/${route.params.seriesID}`,config);
        console.log("result")
        console.log(result)
        return result.data;
    } catch (error) {
        console.log(error);
    }
}
const mediaSeries = await getMediaSeriesData();
</script>

