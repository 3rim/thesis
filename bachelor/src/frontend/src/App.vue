<template>
  <div class="flex flex-col min-h-screen bg-primary">
    <Navbar v-if="loggedIn" />
    <RouterView/>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
import Navbar from './components/Navbar.vue'
import { computed, watch } from 'vue';
import { useStore } from 'vuex';
import {useRouter} from 'vue-router'

const store = useStore();
const router = useRouter();

const loggedIn = computed(() =>{
    return store.state.auth.status.loggedIn;
})

const autoLogout = computed(() =>{
    return store.state.auth.status.autoLogout;
})

//Watch if autoLogout was triggerd => redirect to LoginPage
watch(autoLogout, (newValue) => {
  console.log(autoLogout+`x is ${newValue}` )
  router.push("/login")
})




</script>
