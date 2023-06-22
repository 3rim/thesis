<template>
    <div>
        <Suspense>
            <AsyncInventoryList/>
            <template #fallback>
            <p>Loading...</p>
            </template>
        </Suspense>
     
    </div>
</template>

<script setup>
import AsyncInventoryList from '@/components/AsyncInventoryList.vue';


</script>
