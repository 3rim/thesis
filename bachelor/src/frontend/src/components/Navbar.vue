<template>
    <header class="sticky top-0 shadow-lg">
        <nav class="container flex flex-col sm:flex-row items-center gap-4 py-1">
        <div class="px-2 pt-2 sm:flex">
            <RouterLink :to="{name:'home'}">
                <p class=" mt-1 block px-2 py-1 font-semibold rounded hover:bg-slate-500">Meine Medien</p>
            </RouterLink>
            <RouterLink :to="{name:'ausleihe'}">
                <p class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-slate-500">Ausleihe/Rückname</p>
            </RouterLink>
            <InvetoryDropDown />
            <AdminDropDownVue />
        </div>
    </nav>
    </header>
</template>

<script setup lang="ts">
import AdminDropDownVue from './AdminDropDown.vue';
import InvetoryDropDown from './InventoryDropDown.vue'


</script>
