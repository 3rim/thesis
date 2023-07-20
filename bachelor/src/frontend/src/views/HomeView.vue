<script setup>
import AsyncBorrowerView from '../components/AsyncBorrowerView.vue';

</script>

<template>
  <main class="mt-5">
    <Suspense>
    <AsyncBorrowerView :user-id="1"/>
    <template #fallback>
            <p>loadning...</p>
        </template>
   </Suspense>
    
  </main>
</template>
