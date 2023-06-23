<template>
    <div>
        <Suspense>
            <AsyncInventoryList v-show="$route.path==='/inventory'" />
            <template #fallback>
            <p>Loading...</p>
            </template>
        </Suspense>
    <RouterView />
    
    </div>
</template>

<script setup>
import AsyncInventoryList from '@/components/AsyncInventoryList.vue';

</script>
