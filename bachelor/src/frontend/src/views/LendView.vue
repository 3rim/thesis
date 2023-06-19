<template>
    <div class="container">
        <div class=" pt-4 mb-8 ">
             <input 
        type="text" 
        placeholder="Mediencode einscannen oder tippen"
        v-model="searchQuery"
        @input="getSearchResults()"
        class="py-2 px-1 w-full bg-transparent border-b
         focus:border-b-gray-600 
         focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]"/>
        </div>
       
        <Suspense>
        <AsyncBorrowerView />
        <template #fallback>
            <p>loadning...</p>
        </template>
        </Suspense>
    </div>
</template>

<script setup lang="ts">
import AsyncBorrowerView from '../components/AsyncBorrowerView.vue';

</script>
