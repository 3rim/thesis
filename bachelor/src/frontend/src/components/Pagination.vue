<template>
    <div class="items-center bg-slate-300">
        <button @click="page = page-1" class="px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm"> &lt;-</button>
        <span class=""
         v-for="(item,index) in new Array(totalPages)" :key="index">
            <button @click="page = index" class="px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm">
                {{ index }}
            </button>
        </span>
        <button @click="page = page+1" class="px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm"> -&gt;</button>
    
    </div>
</template>

<script setup>
import {  ref, watch } from 'vue';
const page = ref(0);
const props = defineProps(['totalPages']);


</script>
