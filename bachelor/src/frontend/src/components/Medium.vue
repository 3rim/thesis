<template>
    <div v-if="err"
        class=" flex flex-col flex-1 items-center">
        <p class="flex items-center bg-red-200 px-2 py-4 rounded" >{{ errorMessage }}</p>
       </div>
    <div class="flex flex-col flex-1 items-center"
        v-if="!err">
        <!-- Generell infos about this Medium -->
        <div class="py-3">
            <p>Medium-ID: {{ route.params.mediumID }}</p>
            <p>Titel: {{ mediumData.data.title }}</p>
            <p>Aktueller Ausleiher: {{ mediumData.data.currentBorrower }}</p>
        </div>
        <!-- The loan histories -->
        <p>Ausleihhistorie</p>
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
                    <td class="text-center">{{ loanHistorie.dateOfLend }}</td>
                    <td class="text-center">{{ loanHistorie.dateOfReturn }}</td>
                    <td class="text-center">{{ loanHistorie.borrower.firstName }}</td>
                                 
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

const err = ref(false);
const errorMessage = ref("")


const route = useRoute();
const loanHistories = ref(""); 
const getMediaData =async () => {
    try {
        const response = await axios.get(
            `/api/v1/inventory/${route.params.mediumID}`
        ).catch(function (error){
                    if(error.response){
                        console.log(error.response.data)
                        err.value = true;
                        console.log(error.response.message)
                        errorMessage.value = error.response.data.message
                    }
                });



        console.log(response);
        loanHistories.value = response.data.loanHistories
        console.log(loanHistories.value)
        return response;
    } catch (error) {
        console.log(error);
    }
};
const mediumData = await getMediaData();

</script>