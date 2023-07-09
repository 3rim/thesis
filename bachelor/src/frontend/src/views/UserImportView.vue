<template>
    <div class="container pt-4 mb-8  flex flex-col items-center">
        AdminView
        <!-- Generall Infos about Import -->
        <div class="flex flex-row ">
            <div class="bg-slate-400 p-3 m-1">
                <p class="text-center">Nutzer-Import</p>
                <label for="userFile">User-csv : </label>
                <input type="file" accept=".csv" @change="handeFileUploud($event)" id="userFile">
                <button @click="showPreview = !showPreview" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded">
                    Vorschau anzeigen
                </button>
            </div>
            <!-- CSV-Schema -->
            <div class=" bg-slate-400 p-3 m-1">
                <p>CSV-Format</p>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Vorname</th>
                            <th>Nachname</th>
                            <th>Geb.</th>
                            <th>Adresse</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="dummyData in dummyDataList"
                  class="">
                    <td >{{ dummyData.borrowerNr }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.firstName }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.lastName }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.dob }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.adress }}</td>
                  </tr>
                </tbody>
                </table>
            </div>
        </div>

        <Suspense v-if="showPreview">
                <AsyncUserImportPreview :csv-file="fileContent" :name="myName" /> 
                <template #fallback>
                        Loading...
                </template>
        </Suspense>
    </div>
</template>

<script setup>
import axios from 'axios' ;
import {useRoute} from "vue-router";
import { ref } from 'vue';
import Papa from 'papaparse';
import AsyncUserImportPreview from '../components/AsyncUserImportPreview.vue'

const file = ref();
const fileContent = ref();
const currentUsers = ref();
const showPreview = ref(false);
const myName = ref("erim");

const handeFileUploud = (event) => {
    file.value = event.target.files[0]
    console.log(file.value);
    parseFile()
    
}


const parseFile = () =>{
    /*
    A parse result always contains three objects: data, errors, and meta. Data and errors are arrays, and meta is an object. 
    In the step callback, the data array will only contain one element.
            {
            data:   // array of parsed data
            errors: // array of errors
            meta:   // object with extra info
            }
    data is an array of rows. If header is false, rows are arrays; otherwise they are objects of data keyed by the field name.
    */
    Papa.parse( file.value, {
        skipEmptyLines: true,
       // header : true, // instead row of arrays , its row of objects
        complete: function( results ){
            fileContent.value = results;
            console.log(fileContent.value)
        }
    } );
}



const previewChanges = async () => {
	const response = await axios.get(
		`/api/v1/user/download`
	);

    console.log(response);
    var results = Papa.parse(response.data);
    currentUsers.value = results;
    console.log(results);
    console.log(currentUsers.value);



};



const dummyDataList = ref([
            { borrowerNr: 5, firstName: 'Erim', lastName: 'Medi', dob: '21.02.1996',adress:'My adress' },
            { borrowerNr: 15, firstName: 'Ensar', lastName: 'Medi', dob: '21.02.1996',adress:'My adress' },
            { borrowerNr: 201, firstName: 'Sarah', lastName: 'Schmidt', dob: '21.02.1996',adress:'My adress' },
            
        ]);
</script>