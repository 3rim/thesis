<template>
    <header class="flex items-center px-4 py-3 bg-gray-300">
        <div class="px-2 pt-2 sm:flex">
            <p class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-slate-500">Meine Medien</p>
            <p class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-slate-500">Ausleihe/Rückname</p>
            <InvetoryDropDown />
            <AdminDropDownVue />
        </div>
    </header>
</template>

<script setup lang="ts">
import AdminDropDownVue from './AdminDropDown.vue';
import InvetoryDropDown from './InventoryDropDown.vue'


</script>
