<template>
    <div class="py-1 px-1">
        <button @click="router.back()"
        class="bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded">
        Zurück </button>
        <Suspense>
        <MediaSeriesDetails />
        <template #fallback>
            <p>loadning...</p>
        </template>
        </Suspense>
    </div>
</template>

<script setup>

import { useRouter } from 'vue-router';
import MediaSeriesDetails from '../components/MediaSeriesDetails.vue';

const router = useRouter();

</script>
