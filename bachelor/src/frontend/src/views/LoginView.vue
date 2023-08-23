<template>
    <div class="container flex flex-col items-center">
        <div class="mt-20 bg-gray-300 px-2 py-2 rounded-lg shadow-lg "
        v-if="!showInitLogin">
            <p class="text-center font-bold">Login</p>
            
            <Form 
                @submit="handleLogin" 
                
                class=""
                >
                <div>
                    <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="email">
                        Email
                    </label>
                    <Field v-model="email" class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" name="email" type="email" />
                    <ErrorMessage class="text-red-500" name="email" />
                </div>
                <div>
                    <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="password">
                        Password
                    </label>
                    <Field class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" name="password" type="password" />
                    
                </div>
                <button class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" type="submit">Login</button>
            </Form>
        </div>
        <div class="mt-20 bg-gray-300 px-2 py-2 rounded-lg shadow-lg "
        v-else>
            <p class="text-center font-bold">Neues Passwort setzen</p>            
            <Form 
                @submit="submitPasswort" 
                :validation-schema="initLoginSchema"
                class=""
                >
                <div>
                    <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="email">
                        Email
                    </label>
                    <Field v-model="email" class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" name="email" type="email" />
                    <ErrorMessage class="text-red-500" name="email" />
                </div>
                <div>
                    <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="currentPassword">
                        Aktuelles Passwort
                    </label>
                    <Field v-model="currentPassword" class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" name="currentPassword" type="password" />
                    <ErrorMessage class="text-red-500" name="currentPassword" />
                </div>
                <div>
                    <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="newPassword">
                        Neues Passwort
                    </label>
                    <Field class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" name="newPassword" type="password" />
                    <!--<ErrorMessage class="text-red-500" name="newPassword" />-->
  
                    <ErrorMessage  name="newPassword">
                     <p class="text-red-500"> Passwort muss min. 8 Zeichen lang sein</p>
                    </ErrorMessage>
                </div>
                <div>
                    <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="passwordConfirmation">
                        Passwort wiederholen
                    </label>
                    <Field class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" name="passwordConfirmation" type="password" />
                    <ErrorMessage  name="passwordConfirmation">
                     <p class="text-red-500"> Passwort stimmt nicht überein</p>
                    </ErrorMessage>
                </div>
                
                
                <button class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Passwort setzen</button>
            </Form>
        </div>

        <div v-if="message"
        class=" mt-5 flex flex-col flex-1 items-center">
        <p class="flex items-center bg-red-200 px-2 py-4 rounded" >{{ message }}</p>
       </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import {Form, Field,ErrorMessage } from 'vee-validate';
import * as yup from 'yup';

const schema = yup.object({
  //email: yup.string().required().email(),
  //name: yup.string().required(),
  //password: yup.string().required().min(8),
});
const initLoginSchema = yup.object({
  email: yup.string().required().email(),
  newPassword: yup.string().required().min(8),
  passwordConfirmation: yup
    .string()
    .required()
    .oneOf([yup.ref('newPassword')], 'Passwords do not match'),
});

const router = useRouter();
const email = ref();
const password = ref();
const newPassword = ref();
const currentPassword = ref();
const loading = ref(false);
const store = useStore();
const API_URL = '/api/v1/auth/';

const message = ref(null);
const showInitLogin = ref(false);


function onSubmit(values) {
  // Submit values to API...
  console.log(values)
  alert(JSON.stringify(values, null, 2));
}
const submitPasswort = async (form) =>{
    console.log(form)
    let user ={
        email: email.value,
        currentPassword: password.value,
        newPassword: newPassword.value
    }

    const response = await axios.post(API_URL + 'initialLogin', form)
    router.go(0);
}


//dispatch 'auth/login' Action to Vuex Store. If the login is successful, go to Profile Page, otherwise, show error message.
const handleLogin = async (form) =>{
    console.log("lasds")
    loading.value = true;
    let user ={
        email: email.value,
        password: password.value
    }
    
    //const response = await axios.post(API_URL + 'login', user)
    
    
        //login
        store.dispatch("auth/login",form).then(
        (response) => {
            if(response.initialLogin){
                showInitLogin.value = true;
            }
            else{
                router.push("/myProfile")
            }
        },
        (error) =>{
            console.log(error)
            loading.value = false;
            message.value = error.message;
            console.log(message.value)
        }
    )
    
}
</script>
