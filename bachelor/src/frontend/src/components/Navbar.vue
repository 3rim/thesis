<template >
    <header class="sticky top-0 shadow-lg bg-navbar" v-show="loggedIn">
        <nav class="container flex flex-col sm:flex-row items-center gap-4  ">
        <div class="px-2 pt-2 sm:flex">
            <RouterLink :to="{name:'profile'}">
                <p class=" mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons " >Home</p>
            </RouterLink>
            <RouterLink v-if="showLoanBoard" :to="{name:'ausleihe'}">
                <p class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons">Ausleihe/Rückname</p>
            </RouterLink>
           
            <InventoryMenu v-if="showInventoryDropDown"/>
            <AdminMenu v-if="showAdminDropDown"/>

            <button 
            class=" lg:absolute lg:top-1 lg:right-1 md:absolute md:top-1 md:right-1  mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons"
            @click="logout">Logout
            </button>
        </div>
    </nav>
    </header>
</template>

<script setup>
import AdminMenu from './AdminMenu.vue'
import InventoryMenu from './InventoryMenu.vue';
import { computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';


const store = useStore();
const router = useRouter();


const loggedIn = computed(() =>{
    return store.state.auth.status.loggedIn;
})

//A computed ref! Access with .value
const currentUser = computed(() =>{
    return store.state.auth.user;
})

const showAdminDropDown = computed(() =>{
    if(currentUser.value && currentUser.value.roles){
        return currentUser.value['roles'].includes('ADMIN')
    }
})

const showLoanBoard = computed(() =>{
    if(currentUser && currentUser.value.roles){
        return currentUser.value['roles'].includes('LIBRARIAN') 
        ||currentUser.value['roles'].includes('ADMIN') 
        ||currentUser.value['roles'].includes('LOAN_HELPER')  
    }
})

const showInventoryDropDown = computed(() =>{
    if(currentUser && currentUser.value.roles){
        return currentUser.value['roles'].includes('LIBRARIAN')
        || currentUser.value['roles'].includes('ADMIN')
        || currentUser.value['roles'].includes('INVENTORY_HELPER') 
    }
})

const logout = () => {
    store.dispatch('auth/logout');
    router.push('/login')
}

</script>
