<template>
    <div>
        <Suspense>
        <InventoryDetails />
        <template #fallback>
            <p>loadning...</p>
        </template>
        </Suspense>

        <button @click="router.back()">Go Back </button>
    </div>
</template>

<script setup>

import { useRouter, useRoute } from 'vue-router';
import InventoryDetails from '@/components/InventoryDetails.vue';

const router = useRouter();



</script>
