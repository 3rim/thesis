<template>
        <!--Users-->
    <div class="mt-20 flex flex-col items-center">
        <table  v-if="borrowers" >
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

const props = defineProps(['page','data']);

const currentSelections =ref();

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
</script>
