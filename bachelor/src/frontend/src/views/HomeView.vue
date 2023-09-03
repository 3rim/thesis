<script setup>
import AsyncBorrower from '../components/AsyncBorrower.vue';
import { useStore } from 'vuex';
import { onBeforeMount ,computed} from 'vue';
import { useRouter } from 'vue-router';

const store = useStore();
const router = useRouter();

const currentUser = computed(() =>{
    return store.state.auth.user;
})

/*If not logged in route to login page */
onBeforeMount(() => {
  if(!currentUser.value){
    router.push('/login');
  }
})

</script>

<template>
  <main class="mt-5">
    <Suspense v-if="currentUser">
    <AsyncBorrower :user-id="store.state.auth.user.id"/>
    <template #fallback>
            <p>loadning...</p>
        </template>
   </Suspense>
    
  </main>
</template>
