<template>
  <div class="flex flex-col min-h-screen bg-primary">
    <Navbar v-if="loggedIn" />
    <RouterView/>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
import Navbar from './components/Navbar.vue'
import { computed} from 'vue';
import { useStore } from 'vuex';

const store = useStore();

const loggedIn = computed(() =>{
    return store.state.auth.status.loggedIn;
})
</script>
