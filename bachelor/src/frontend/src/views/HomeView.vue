<script setup>
import AsyncBorrowerView from '../components/AsyncBorrowerView.vue';

</script>

<template>
  <main class="">
    <div>sdsa</div>
    <Suspense>
    <AsyncBorrowerView :user-id="1"/>
    <template #fallback>
            <p>loadning...</p>
        </template>
   </Suspense>
    
  </main>
</template>
