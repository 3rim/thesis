<template>
    <div class="container pt-4 mb-8  flex flex-col items-center">
        <!-- Generall Infos about Import -->
        <div class="flex flex-row ">
            <div class="flex flex-col bg-[#E4D4BA] p-3 m-1 w-3/5 rounded-lg ">
                <div>
                    <p class="text-center font-bold underline">Nutzer-Import Hinweise</p>
                    <p class="">
                        Für den Nutzer-Import muss eine .csv Datei (CSV durch Komma getrennt) hochgeladen werden. Jene CSV-Datei wird
                        mit den aktuellen aktiven Nutzern verglichen. Die Zuordnung eines Nutzers geschieht anhand seiner eindeutigen ID!
                    </p>
                    <table class="w-full ">
                        <thead class="text-gray-700 text-s font-bold">
                            <tr>
                                <th class="px-2"> Funktion </th>
                                <th class="px-2">Nutzer in CSV vorhanden</th>
                                <th class="px-2">Nutzer in Datenbank vorhanden </th>
                            </tr>
                        </thead>
                        <tbody class="">
                            <tr>
                                <th class="">Neuen User anlegen</th>
                                <th><font-awesome-icon :icon="['fas', 'check']" style="color: #089131;" /> </th>
                                <th><font-awesome-icon :icon="['fas', 'x']" style="color: #f51000;" /></th>
                            </tr>
                            <tr>
                                <th>Änderungen</th>
                                <th><font-awesome-icon :icon="['fas', 'check']" style="color: #089131;" /></th>
                                <th><font-awesome-icon :icon="['fas', 'check']" style="color: #089131;" /></th>
                            </tr>
                            <tr>
                                <th>Nutzer deaktivieren</th>
                                <th><font-awesome-icon :icon="['fas', 'x']" style="color: #f51000;" /></th>
                                <th><font-awesome-icon :icon="['fas', 'check']" style="color: #089131;" /></th>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
            </div>
            <!-- CSV-Schema -->
            <div class=" bg-[#E4D4BA] p-3 m-1 rounded-lg w-2/5">
                <p class=" text-center underline font-bold">CSV-Format Beispiel</p>
                <p>Die CSV-Datei muss in dem Format wie im Beispiel sein!</p>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Vorname</th>
                            <th>Nachname</th>
                            <th>Gruppe</th>
                            <th>Geb.</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="dummyData in dummyDataList"
                  class="">
                    <td class="border-collapse border border-slate-400">{{ dummyData.borrowerNr }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.firstName }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.lastName }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.group }}</td>
                    <td class="border-collapse border border-slate-400">{{ dummyData.dob }}</td>
                  </tr>
                </tbody>
                </table>
            </div>
            
        </div>
        <div class=" px-2 py-2 mb-3 bg-slate-300 rounded-lg">
                    <label for="userFile">User-csv hochladen </label>
                    <input type="file" accept=".csv" @change="handeFileUploud($event)" id="userFile">
                    <button
                    @click="showPreview = !showPreview"
                    :disabled="(file ==null)"
                    class="bg-blue-500 enabled:hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded disabled:opacity-25 ">
                        Vorschau anzeigen
                    </button>
                </div>

        <Suspense v-if="showPreview">
                <AsyncUserImportPreview :csv-file="fileContent" :file="file"/> 
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


const file = ref(null);
const fileContent = ref();
const showPreview = ref(false);

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


const dummyDataList = ref([
            { borrowerNr: 5, firstName: 'Erim', lastName: 'Medi', dob: '21.02.1996',group:'Lehrer' },
            { borrowerNr: 15, firstName: 'Ensar', lastName: 'Medi', dob: '21.02.1996',group:'5a' },
            { borrowerNr: 201, firstName: 'Sarah', lastName: 'Schmidt', dob: '21.02.1996',group:'Beliebig' },
            
        ]);
</script>