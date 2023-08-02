<template>
  <div class="container pt-4 mb-8  flex flex-col items-center">
    <form class="flex flex-wrap" @submit.prevent="filter" @keyup.enter="filter">
            <!-- firstName -->
            <div class=" px-3 mb-6 md:mb-0">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="firstName">
                    Vorname
                </label>
                <input class="appearance-none block  bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="firstName" type="text" placeholder="Vorname"
                v-model="firstName" >
            </div>
            <!--lastName-->
            <div class=" px-3 mb-6 md:mb-0">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="lastName">
                    Nachname
                </label>
                <input class="appearance-none block  bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="lastName" type="text" placeholder="Nachname"
                v-model="lastName" >
            </div>
            <div class=" px-3 mb-6 md:mb-0">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="userState">Status</label>
                <select v-model="userState" class=" block  bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white">
                    <option disabled value hidden="">Please select one</option>
                    <option>INITIALIZED</option>
                    <option>ACTIVE</option>
                    <option>INACTIV</option>
                </select>
            </div>
    </form>
 
    <div class="items-center ">
        <button @click="getPreviousPage" 
        class=" disabled:cursor-auto disabled:text-gray-400 px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm"
        :disabled="page <=0"
        > &lt;-
        
        </button>
        <span class=""
         v-for="(item,index) in new Array(totalPages)" :key="index">
            <button @click="getPage(index)" 
            class="px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm "
            :class="{ 'bg-red-300': page === index}">
            
                {{ index }}
            </button>
        </span>
        <button @click="getNextPage()" 
        :disabled="page >= totalPages-1"
        class="disabled:cursor-auto disabled:text-gray-400 px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm"> -&gt;</button>
    </div>
  
    <div v-if="data">
      <AsyncUsersView :key="componentKey" :data="data" />
    </div>
    <div v-else>No Content</div>
  </div>
  
</template>

<script setup>
import AsyncUsersView from '../components/AsyncUsersView.vue'
import axios from 'axios' ;
import {  ref, watch } from 'vue';
import {onMounted ,computed} from 'vue';
import authHeader from '../services/authHeader';
import { useRoute,useRouter } from 'vue-router';

const page = ref(0);
const firstName = ref();
const lastName = ref();
const userState = ref();
const borrowers = ref([]);
const router = useRouter();
const route = useRoute();

const totalPages = ref(0);
const data = ref();
const componentKey =ref(0);

const filter = () =>{
  loadData()
}

const getPage = (index) =>{
    page.value = index;
    console.log(index);
    loadData();
    
}

const getPreviousPage = () =>{
  page.value = page.value -1;
    loadData();
}

const getNextPage = () =>{
    page.value = page.value +1;
    loadData();
}

const loadData = () =>{
  let config = {
                headers: authHeader(),
                params: {
                    page:page.value,
                    borrowerState: userState.value,
                    firstName: firstName.value,
                    lastName: lastName.value,
                    size: 2
                }
            }
            console.log(config)
            let myParams = config.params;
            
  data.value = axios.get('/api/v1/borrowers',config)
  .then(function (response) {
    console.log(response)
    totalPages.value = response.data.totalPages;
    borrowers.value = response.data.borrowers;
    data.value = response.data
    forceRender();
  });
}

const forceRender = () =>{
  componentKey.value += 1;
}

onMounted(() => {
    data.value = loadData();
})


</script>
