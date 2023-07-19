<template>
    <div class="container flex flex-col items-center">
        <div class="mt-20 bg-gray-300 px-2 py-2 rounded-lg shadow-lg ">
            <p class="text-center font-bold">Login</p>
            <form @submit.prevent="handleLogin">
                <!--Email-->
                <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="email">
                    Email
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="email" type="text" placeholder="email"
                v-model="email" required>
                <!--password-->
                <label class="block  tracking-wide text-gray-700 text-xs font-bold mb-2" for="password">
                    Passwort
                </label>
                <input class="appearance-none block w-full bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="password" type="text" placeholder="password"
                v-model="password" required>
                <button class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" type="submit">Login</button>
            </form>
        </div>

        <div v-if="message"
        class=" mt-5 flex flex-col flex-1 items-center">
        <p class="flex items-center bg-red-200 px-2 py-4 rounded" >{{ message }}</p>
       </div>
    </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();
const email = ref();
const password = ref();
const loading = ref(false);
const store = useStore();

const message = ref(null);

const loggedIn = computed(() =>{
    return store.state.auth.status.loggedIn;
})


//dispatch 'auth/login' Action to Vuex Store. If the login is successful, go to Profile Page, otherwise, show error message.
const handleLogin = () =>{
    loading.value = true;
    console.log(email.value)
    let user ={
        email: email.value,
        password: password.value
    }

    store.dispatch("auth/login",user).then(
        () => {
            router.push("/myProfile")
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
