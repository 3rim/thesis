<script setup>
import AsyncBorrowerView from '../components/AsyncBorrowerView.vue';
import { useStore } from 'vuex';

const store = useStore();
</script>

<template>
  <main class="mt-5">
    <Suspense>
    <AsyncBorrowerView :user-id="store.state.auth.user.id"/>
    <template #fallback>
            <p>loadning...</p>
        </template>
   </Suspense>
    
  </main>
</template>
