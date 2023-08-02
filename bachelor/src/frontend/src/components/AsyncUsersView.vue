<template>
        <!--Users-->
    <div class="mt-20 flex flex-col items-center">

        <div v-if="borrowers" class="w-full ">
            <button @click="deleteUsers" class="mr-1 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
                Auswahl Löschen
            </button>
            <button @click="resetPassword" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                Passwort zurücksetzen
            </button>
            
        </div>
        <!--SuccesMessage-->
    <div class=" max-w-lg w-full: mt-1 mb-3 bg-teal-200  border-t-4 border-teal-500 rounded-b text-teal-900 px-4 py-3 shadow-md"
    v-if="success">
    <div class="flex">
        <div class="py-1"><svg class="fill-current h-6 w-6 text-teal-700 mr-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z"/></svg></div>
         <div>
            <p class="font-bold">Passwörter zurückgesetzt</p>
            <div v-if="success > 0">
                <p>Laden Sie die CSV-Datei herunter, um den Nutzer ihr Einmal-Passwort zu geben</p>
                <button 
                class=" bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 border border-green-700 rounded" 
                @click="downloadBlob">download csv
                </button>
            </div>
        </div>
    </div>
    </div> 
        <table v-if="borrowers" >
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
                <tr v-for="borrower in borrowers"
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
</template>

<script setup>
import axios from 'axios' ;
import {  ref, watch } from 'vue';
import {onMounted,onBeforeMount ,computed} from 'vue';
import authHeader from '../services/authHeader';
import { useRoute,useRouter } from 'vue-router';
import Papa from 'papaparse';

const props = defineProps(['page','data']);
const currentSelections =ref([]);
const success = ref(false);
const resetUser = ref();

const route = useRoute();
const router = useRouter();
const currentPage = ref();
const totalPage = ref();
const data = ref();
const queryParams = ref();
const borrowers = ref();

// When the items change the value of the selected flag, the computed allSelected is recalculated. It's reactive so the :checked will change as that computed value changes.
const allSelected = computed(() =>{
    return borrowers.value.every(borrower => borrower.selected);
})

const selectAll = () =>{
    let all_s = allSelected.value;
    borrowers.value.forEach( borrower => borrower.selected = !all_s );   
}

watch(
  () => borrowers,
  (newData) => {
    // Note: `newValue` will be equal to `oldValue` here
    // *unless* state.someObject has been replaced
    currentSelections.value = borrowers.value
          .filter( borrower => borrower.selected )
          .map( borrower => borrower.id )
  },
  { deep: true }
)

/**
const getData = async() =>{
    let config = {
                headers: authHeader(),
                params: {
                    page:props.page,
                    borrowerState: props.state,
                    firstName: props.firstName,
                    lastNameName: props.lastName,
                    size: 2
                }
            }
  let myParams = config.params;
  const response = await axios.get('/api/v1/borrowers',config);
  router.push({name:'users',query:myParams})
  if(response.data.currentPage){

  }
  return response.data;
}
data.value = await getData();

 */
 onBeforeMount(() => {
    borrowers.value = props.data.borrowers
    if(borrowers.value){
        borrowers.value.forEach(borrower =>borrower["selected"]= false);
    }
    
})


const resetPassword = () =>{
    let user = JSON.parse(localStorage.getItem('user'));
        axios({
            method: 'put',
            url:'/api/v1/borrowers/reset',
            headers: {'Authorization': 'Bearer '+ user.jwt}, 
            data: currentSelections.value
            })
            .then(function (response) {
                        //handle success
            success.value = true;
            if(response.data && response.data.length > 0){
                //parse JSON -> csv 
                resetUser.value = Papa.unparse(response.data);
            }
  })
}

const deleteUsers = () =>{
    let user = JSON.parse(localStorage.getItem('user'));
    axios({
        method: 'delete',
        url:'/api/v1/borrowers',
        headers: {'Authorization': 'Bearer '+ user.jwt}, 
        data: currentSelections.value
        })
        .then(function (response) {
            router.go({name: 'users',})
  })
}

function downloadBlob(){
    const blob =  new Blob([resetUser.value],{type:'text/csv;charset-utf-8'});
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.download='newUsers.csv';
    a.href = url;
    a.click();    
}
</script>
