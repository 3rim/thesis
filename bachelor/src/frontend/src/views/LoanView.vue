<template>
    <div class="container">
        <div class=" pt-4 mb-8  flex">
            <font-awesome-icon class="py-2 px-1" icon="fa-solid fa fa-barcode" size="2x" />
            <div class="w-full">
                <input type="number"  placeholder="Mediencode einscannen oder tippen" v-model="mediaID"
                    @keyup.enter="loanMedia" class="py-2 px-1 w-full bg-transparent border-b [-moz-appearance:_textfield] [&::-webkit-inner-spin-button]:m-0 [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:m-0 [&::-webkit-outer-spin-button]:appearance-none
                focus:border-b-gray-600 
                focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]" />
                
            </div>
            <div>
                <button @click="loanMedia" type="submit"
                    class="py-2 px-1 bg-[#AE857C] text-white hover:bg-[#6F1A07]">Ausleihen</button>
            </div>
        </div>
        <div v-if="err" class=" flex flex-col flex-1 items-center">
            <p class="flex items-center bg-red-200 px-2 py-4 rounded">{{ errorMessage }}</p>
        </div>
        <Suspense>
            <AsyncBorrower :key="componentKey" :user-id="route.query.id" />
            <template #fallback>
                <p>loadning...</p>
            </template>
        </Suspense>
    </div>
</template>

<script setup>
import AsyncBorrower from '../components/AsyncBorrower.vue';
import { ref } from 'vue';
import axios from "axios";
import { useRoute } from "vue-router";
import authHeader from '../services/authHeader';
import { Form, Field, ErrorMessage } from 'vee-validate';
import * as yup from 'yup';

const inputRules = yup.number();

const err = ref(false);
const errorMessage = ref("");

const componentKey = ref(0);
//Re-render component if necessary

const route = useRoute();
const mediaID = ref("");

const forceRerender = () => {
    componentKey.value += 1;
};

const loanMedia = async () => {
    err.value = false;
    if (mediaID.value !== "") {
        const borrowerID = route.query.id;
        let config = {
            headers: authHeader(),
            params: { borrowerID: borrowerID, mediumID: mediaID.value }
        }
        await axios.post('/api/v1/loan', null, config)
            .catch(function (error) {
                if (error) {
                    console.log(error.response.status)
                    err.value = true;
                    if(error.response.status === 400){
                        errorMessage.value ="Anfrage konnte nicht verarbeitet werden"
                    }
                    if(error.response.status === 404){
                        errorMessage.value ="MediumID:"+mediaID.value+" nicht gefunden"
                    }
                    if(error.response.status === 406){
                        console.log(error)
                        errorMessage.value =error.response.data.message
                    }
                    
                    if(error.response.status === 500){
                        errorMessage.value ="Server fehler... Versuchen Sie es erneut"
                    }
                }
            });
        forceRerender();
        //reset mediaID to blank
        mediaID.value = "";
    }
}
</script>
