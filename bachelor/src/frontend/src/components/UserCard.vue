<template>
    <div class=" items-center bg-gray-300">
        
        <p class="text-center">Name: {{userName}}</p>
        <p class="text-center">Klasse: {{userGroup}}</p>
        
    </div>
</template>

<script setup>
import { ref } from 'vue';

const userName = ref("");
const userGroup = ref("");
userName.value = "erim";
userGroup.value = "Lehrer";

</script>
