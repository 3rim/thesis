<template>
    <div class="container pt-4 mb-8  flex flex-col items-center">
    
        <!--Info Card-->
        <div class="bg-orange-100 border-l-4 border-orange-500 text-orange-700 p-4 rounded-r-lg " >
        <p class="font-bold">Rollen-Zuweisung</p>
        <p>
            Möchten Sie einen Nutzer Rollen zuweisen bzw. entfernen, so klicken sie auf die jeweiligen
            Boxen. 
        </p>
        </div>
        
        <Suspense>
            <UserRoles />
            <template #fallback>
             <p>loadning...</p>
            </template>
        </Suspense>
    </div>
</template>

<script setup>
import axios from 'axios' ;
import {  ref, watch } from 'vue';
import {onMounted ,computed} from 'vue';
import authHeader from '../services/authHeader';
import { useRoute,useRouter } from 'vue-router';
import UserRoles from '../components/UserRoles.vue'

const router = useRouter();
const route = useRoute();



</script>