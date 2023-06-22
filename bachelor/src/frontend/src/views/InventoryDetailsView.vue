<template>
    <div>
        Hellooo

        <button @click="router.back()">Go Back </button>
    </div>
</template>

<script setup>

import { useRouter, useRoute } from 'vue-router';

const router = useRouter();



</script>
