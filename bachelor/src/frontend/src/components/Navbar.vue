<template >
    <header class="sticky top-0 shadow-lg bg-navbar" v-show="loggedIn">
        <nav class="container flex flex-col sm:flex-row items-center gap-4  ">
        <div class="px-2 pt-2 sm:flex">
            <RouterLink :to="{name:'home'}">
                <p class=" mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons " >Meine Medien</p>
            </RouterLink>
            <RouterLink v-if="showLoanBoard" :to="{name:'ausleihe'}">
                <p class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons">Ausleihe/Rückname</p>
            </RouterLink>
            <InvetoryDropDown v-if="showInventoryDropDown" /> 
            <AdminDropDownVue v-if="showAdminDropDown" />
        </div>
        <div>
            <button 
            class=""
            @click="logout">Logout</button>
        </div>
    </nav>
    </header>
</template>

<script setup>
import AdminDropDownVue from './AdminDropDown.vue';
import InvetoryDropDown from './InventoryDropDown.vue';
import { computed, ref } from 'vue';
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
