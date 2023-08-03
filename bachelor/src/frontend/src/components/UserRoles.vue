<template>
    <div class="container pt-4 mb-8">
        <div class="flex flex-wrap bg-gray-200 rounded-md px-1">
            <div class="w-1/2">
                <p><strong>Nr: </strong>{{ user.borrowerNr }}</p>
                <p><strong>Vorname: </strong>{{ user.firstName }}</p>
                <p><strong>Nachname: </strong>{{ user.lastName }}</p>
            </div>
            <div class="w-1/2">
                <p><strong>Geburtstag: </strong>{{ user.dob }}</p>
                <p><strong>Gruppe: </strong>{{ user.borrowerGroup }}</p>
                <p><strong>Status: </strong>{{ user.borrowerState }}</p>
            </div>
        </div>
        
        <!-- Roles -->
        <div class="flex flex-col mt-3">
            <p class=" text-lg font-bold">Rollen</p>
            <div>
                <input type="checkbox" id="ADMIN" value="ADMIN" v-model="checkeRoles">
                <label for="ADMIN">Admin</label>
            </div>

            <div>
                <input type="checkbox" id="LIBRARIAN" value="LIBRARIAN" v-model="checkeRoles">
                <label for="LIBRARIAN">Bibliothekar</label>
            </div>

            <div>
                <input type="checkbox" id="LOAN_HELPER" value="LOAN_HELPER" v-model="checkeRoles">
                <label for="LOAN_HELPER">Ausleih-Helfer</label>
            </div>

            <div>
                <input type="checkbox" id="INVENTORY_HELPER" value="INVENTORY_HELPER" v-model="checkeRoles">
                <label for="INVENTORY_HELPER">Inventar-Helfer</label>
            </div>
        </div>

        <button @click="safeRoles" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Speichern</button>

        <!-- ResponseBanner -->
        <div v-if="editRolesResponse" >
            <div v-if="editRolesResponse.status ===200">
                <div class="mt-2 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative">
                    <strong class="font-bold">Änderungen wurden übernommen!</strong>
                    <span class="absolute top-0 bottom-0 right-0 px-4 py-3">
                        <svg @click="hideResponse" class="fill-current h-6 w-6 text-green-500" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><title>Close</title><path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 1 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 1 1 1.697 1.697l-2.758 3.152 2.758 3.15a1.2 1.2 0 0 1 0 1.698z"/></svg>
                    </span>
                </div>
            </div>
            <div v-else>
                <div class="mt-2 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative">
                    <strong class="font-bold">Änderungen nicht übernommen werden!</strong>
                    <span class="block sm:inline">{{ editRolesResponse.data }}.</span>
                    <span class="absolute top-0 bottom-0 right-0 px-4 py-3">
                        <svg @click="hideResponse" class="fill-current h-6 w-6 text-red-500" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><title>Close</title><path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 1 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 1 1 1.697 1.697l-2.758 3.152 2.758 3.15a1.2 1.2 0 0 1 0 1.698z"/></svg>
                    </span>
                </div>
            </div>
        </div>
    

    </div>
</template>

<script setup>
import axios from 'axios' ;
import {  ref } from 'vue';
import authHeader from '../services/authHeader';
import { useRoute } from 'vue-router';

const route = useRoute();

const user = ref();
const checkeRoles = ref([])

const editRolesResponse = ref();
const errorMessage = ref();

const hideResponse = () => {
    editRolesResponse.value = null
}

const getUser = async () =>{
    let config = {headers: authHeader()}

    try {
        const res = await axios.get(`/api/v1/borrowers/${route.params.id}`,config);
        user.value = res.data;
        checkeRoles.value = user.value.roles;
    } catch (error) {
        // Handle errors
        console.log(error)
    }
    
}

await getUser();

const safeRoles = async ()  => {
    let config = {headers: authHeader()}

    try {
        const res = await axios.patch(`/api/v1/borrowers/${route.params.id}/roles`,checkeRoles.value,config);
        editRolesResponse.value = res;
    } catch (error) {
        // Handle errors
        console.log(error)
    }
    
}


</script>
