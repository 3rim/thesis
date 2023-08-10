<template>
    <div v-if="err"
        class=" flex flex-col flex-1 items-center">
        <p class="flex items-center bg-red-200 px-2 py-4 rounded" >{{ errorMessage }}</p>
       </div>
    <div class="flex flex-col flex-1 items-center"
        v-if="!err">
        <!-- Generell infos about this Medium -->
        <div class="py-3">
            <p><strong>Medium-ID:</strong> {{ route.params.mediumID }}</p>
            <p><strong>Titel:</strong> {{ mediumData.title }}</p>
            <p><strong>Ausleiher:</strong> {{ mediumData.currentBorrower }}</p>
            <p><strong>Status:</strong> {{ mediumData.status }}</p>
            <p v-if="mediumData.serialNr"><strong>Seriennummer:</strong> {{ mediumData.serialNr }}</p>
        </div>
        <!-- The loan histories -->
        <p> <strong>Ausleihhistorie </strong></p>
        <div>
            <table>
            <thead class="bg-[#E4D4BA]">
                <tr>
                    <th class="px-2">Ausgeliehen</th>
                    <th class="px-2">Zurück gegeben</th>
                    <th class="px-2">Ausleiher</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="loanHistorie in loanHistories"
                 class="bg-[#F7F3E3] border-b-2 cursor-pointer">
                    <td class="text-center">{{ $dateStringToGermanFormat(loanHistorie.dateOfLend) }}</td>
                    <td class="text-center">{{ $dateStringToGermanFormat(loanHistorie.dateOfReturn) }}</td>
                    <td v-if="loanHistorie.borrower" class="text-center">{{ loanHistorie.borrower }}</td>
                    <td v-else class="text-center"> Nutzer gelöscht</td>
                                 
                </tr>
                </tbody>
        </table>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";
import { useRouter, useRoute } from 'vue-router';
import authHeader from '../services/authHeader';

const err = ref(false);
const errorMessage = ref("")


const route = useRoute();
const loanHistories = ref(""); 
const mediumData = ref();
const getLoanHistories =async () => {
    let config = {
                headers: authHeader(),
            }
    try {
        const response = await axios.get(
            `/api/v1/loan/histories/${route.params.mediumID}`,config
        ).catch(function (error){
                    if(error.response){
                        console.log(error.response.data)
                        err.value = true;
                        console.log(error.response.message)
                        errorMessage.value = error.response.data.message
                    }
                });
        return response.data;
    } catch (error) {
        console.log(error);
    }
};
loanHistories.value = await getLoanHistories();


const getMediumData = async () =>{
    let config = {headers: authHeader()}
    try {
        const response = await axios.get( `/api/v1/inventory/${route.params.mediumID}`,config)
        return response.data;
    } catch (error) {
        // Handle errors
        err.value =true
        console.log(error)
        if(error.response.status === 404){
            errorMessage.value ="MediumID:"+route.params.mediumID+" nicht gefunden"
        }
        if(error.response.status === 400){
            errorMessage.value ="Anfrage konnte nicht verarbeitet werden"
        }
        
    }
}
mediumData.value = await getMediumData();
</script>