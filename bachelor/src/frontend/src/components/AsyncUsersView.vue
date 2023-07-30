<template>
    <p>dasd</p>


</template>

<script setup>
import axios from 'axios' ;
import { ref } from 'vue';
import authHeader from '../services/authHeader';
import { useRoute,useRouter } from 'vue-router';


const route = useRoute();
const router = useRouter();
const currentPage = ref();
const totalPage = ref();
const data = ref();
const queryParams = ref();


const getData = async() =>{
    let config = {
                headers: authHeader(),
                params: {
                    page:0,
                    borrowerState: 'ACTIVE'
                }
            }
  let myParams = config.params;
  console.log(myParams)
  const response = await axios.get('/api/v1/borrowers',config);
  router.push({name:'users',query:myParams})
  console.log(response);
  if(response.data.currentPage){

  }
  return response;
}
data.value = await getData();

</script>
