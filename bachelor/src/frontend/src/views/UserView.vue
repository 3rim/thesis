<template>
  <div class="container pt-4 mb-8  flex flex-col items-center">
    <form class="flex flex-wrap" @submit.prevent="filter" @keyup.enter="filter">
            <!-- firstName -->
            <div class=" px-3 mb-6 md:mb-0">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="firstName">
                    Vorname
                </label>
                <input class="appearance-none block  bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="firstName" type="search" placeholder="Vorname"
                v-model="firstName" >
            </div>
            <!--lastName-->
            <div class=" px-3 mb-6 md:mb-0">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="lastName">
                    Nachname
                </label>
                <input class="appearance-none block  bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" id="lastName" type="search" placeholder="Nachname"
                v-model="lastName" >
            </div>
            <!-- State selection -->
            <div class=" px-3 mb-6 md:mb-0">
                <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" for="userState">Status</label>
                <select v-model="userState"  class=" block  bg-gray-200 text-gray-700 border  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" >
                    <option disabled value hidden=""></option>
                    <option></option>
                    <option>INITIALIZED</option>
                    <option>ACTIVE</option>
                    <option>DEACTIVATED</option>
                </select>
            </div>
            <div class="px-3 mb-6 md:mb-0">
              <button @click="filter" class=" mt-6 py-3 px-5 bg-blue-500 hover:bg-blue-700 text-white font-bold rounded "> Suchen</button>
            </div>
    </form>
    
    <!-- PageSize -->
    <div class="my-2 ">
        <p class=" text-gray-700 text-s font-bold text-center">Elemente pro Seite</p>
        <span v-for="size in pageSizes">
          <button 
          @click="setPagesSize(size)" 
          class="px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm"
          :class="{ 'bg-red-300': pageSize === size}">
          {{ size }}
        </button>
        </span>
    </div>

    <!-- Pagination -->
    <div class="items-center ">
      <!--PreviusPage-->
      <button @click="getPreviousPage" 
        class=" disabled:cursor-auto disabled:text-gray-400 px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm"
        :disabled="page <=0"
        > &lt;-
        
      </button>
      <!--Pages -->
      <span class=""
        v-for="(item,index) in new Array(totalPages)" :key="index">
          <button @click="getPage(index)" 
          class="px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm "
          :class="{ 'bg-red-300': page === index}">  
              {{ index }}
          </button>
      </span>
      <!-- NextPage -->
      <button @click="getNextPage()" 
      :disabled="page >= totalPages-1"
      class="disabled:cursor-auto disabled:text-gray-400 px-2 mx-1 border-2 border-sm border-current cursor-pointer text-sm"> -&gt;</button>
    </div>
  
    <div v-if="data">
      <AsyncUsersView :key="componentKey" :data="data" />
    </div>
    <div v-else>
      <p>Kein Ergebnis</p>
    </div>
  
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
const pageSizes = [10,20,30];
const pageSize = ref(10);

const setPagesSize = (size) =>{
  pageSize.value = size;
  loadData();
}

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
                    pageSize: pageSize.value
                }
            }
            console.log(config)
            let myParams = config.params;
            
  axios.get('/api/v1/borrowers',config)
  .then(function (response) {
    console.log(response)
    totalPages.value = response.data.totalPages;
    borrowers.value = response.data.borrowers;
    data.value = response.data
    console.log(data.value)
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
