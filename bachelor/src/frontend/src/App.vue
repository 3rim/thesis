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
import {useRouter} from 'vue-router';
import jwt_decode from 'jwt-decode';

const store = useStore();
const router = useRouter();

const loggedIn = computed(() =>{
  console.log("logged in "+store.state.auth.status.loggedIn);
  return store.state.auth.status.loggedIn;
})

const autoLogout = computed(() =>{
  console.log("auto log-out "+store.state.auth.status.autoLogout);
  return store.state.auth.status.autoLogout;
})


//Watch if autoLogout was triggerd => redirect to LoginPage
watch(autoLogout, (newValue) => {
  console.log(autoLogout+`x is ${newValue}` )
  router.push("/login")
})

//On App Reload check jwt.
//TODO: global timer (which restarts) instead in authModule. If closing tab or switching to other page like youtube the timer gets killed
const checkJwtExpirationOnAppReload = () =>{
  if(store.state.auth.user){
    let jwt = store.state.auth.user.jwt;
    const decoded =jwt_decode(jwt)
    console.log(decoded)
    if (Date.now() >=  decoded.exp *1000) {
      console.log("jwt expired")
      store.dispatch("auth/auto_logout")
    } 
  }
}

checkJwtExpirationOnAppReload();
</script>
