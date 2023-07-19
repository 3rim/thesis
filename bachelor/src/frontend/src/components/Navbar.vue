<template >
    <header class="sticky top-0 shadow-lg bg-navbar" v-show="loggedIn">
        <nav class="container flex flex-col sm:flex-row items-center gap-4  ">
        <div class="px-2 pt-2 sm:flex">
            <RouterLink :to="{name:'home'}">
                <p class=" mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons " >Meine Medien</p>
            </RouterLink>
            <RouterLink :to="{name:'ausleihe'}">
                <p class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons">Ausleihe/Rückname</p>
            </RouterLink>
            <InvetoryDropDown />
            <AdminDropDownVue />
        </div>
    </nav>
    </header>
</template>

<script setup>
import AdminDropDownVue from './AdminDropDown.vue';
import InvetoryDropDown from './InventoryDropDown.vue';
import { computed, ref } from 'vue';
import { useStore } from 'vuex';

const store = useStore();


const loggedIn = computed(() =>{
    return store.state.auth.status.loggedIn;
})



</script>
