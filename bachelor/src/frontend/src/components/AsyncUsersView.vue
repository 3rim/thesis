<template>
    <p>dasd</p>
    <!--Filter -->
    <div class="container flex flex-col items-center">
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
                    <option>ACTIV</option>
                    <option>INACTIV</option>
                </select>
            </div>
        </form>

        <!--Users-->
    <div class="mt-20">
        <table>
            <thead>
                <tr>
                    <th class="px-4">
                    <input type="checkbox" class="checkbox" :checked="allSelected" @click="selectAll"/>
                    </th>
                    <th class="px-4">Id</th>
                    <th class="px-4">Vorname</th>
                    <th class="px-4">Nachname</th>
                    <th class="px-4">Geb.</th>
                    <th class="px-4">Gruppe</th>
                    <th class="px-4">Status</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="borrower in data.borrowers"
                class="text-center">
                    <td>
                    <input type="checkbox" class="checkbox" v-model="borrower.selected"/>
                    </td>
                    <td>{{ borrower.borrowerNr }}</td>
                    <td>{{ borrower.firstName }}</td>
                    <td>{{ borrower.lastName }}</td>
                    <td>{{ $dateStringToGermanFormat(borrower.dob)}}</td>
                    <td>{{ borrower.borrowerGroup }}</td>
                    <td>{{ borrower.borrowerState }}</td>
                </tr>
            </tbody>
        </table>
        <p v-if="currentSelections">Selection:{{ currentSelections }}</p>
    </div>

    </div>

    

</template>

<script setup>
import axios from 'axios' ;
import {  ref, watch } from 'vue';
import {onMounted ,computed} from 'vue';
import authHeader from '../services/authHeader';
import { useRoute,useRouter } from 'vue-router';

const firstName = ref();
const lastName = ref();
const userState = ref();
const currentSelections =ref();

const route = useRoute();
const router = useRouter();
const currentPage = ref();
const totalPage = ref();
const data = ref();
const queryParams = ref();

// When the items change the value of the selected flag, the computed allSelected is recalculated. It's reactive so the :checked will change as that computed value changes.
const allSelected = computed(() =>{
    return data.value.borrowers.every(borrower => borrower.selected);
})

const selectAll = () =>{
    let all_s = allSelected.value;
    data.value.borrowers.forEach( borrower => borrower.selected = !all_s );   
}

watch(
  () => data,
  (newData) => {
    // Note: `newValue` will be equal to `oldValue` here
    // *unless* state.someObject has been replaced
    currentSelections.value = newData.value.borrowers
          .filter( borrower => borrower.selected )
          .map( borrower => borrower.id )
  },
  { deep: true }
)


const getData = async() =>{
    let config = {
                headers: authHeader(),
                params: {
                    page:0,
                    borrowerState: ''
                }
            }
  let myParams = config.params;
  console.log(myParams)
  const response = await axios.get('/api/v1/borrowers',config);
  router.push({name:'users',query:myParams})
  console.log(response);
  console.log(response.data.borrowers)
  if(response.data.currentPage){

  }
  return response.data;
}
data.value = await getData();

const filter = () =>{
    console.log("submit")
}


onMounted(() => {
    data.value.borrowers.forEach(borrower =>borrower["selected"]= false);
})
</script>
