<template>
    <!--Todo: remove the last blank line in deactivateUsers-->
    <div
    v-if="showPreview">

        <!-- Info Message -->
        <div class="flex items-center bg-blue-500 text-white text-sm font-bold px-4 py-3" role="alert">
        <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M12.432 0c1.34 0 2.01.912 2.01 1.957 0 1.305-1.164 2.512-2.679 2.512-1.269 0-2.009-.75-1.974-1.99C9.789 1.436 10.67 0 12.432 0zM8.309 20c-1.058 0-1.833-.652-1.093-3.524l1.214-5.092c.211-.814.246-1.141 0-1.141-.317 0-1.689.562-2.502 1.117l-.528-.88c2.572-2.186 5.531-3.467 6.801-3.467 1.057 0 1.233 1.273.705 3.23l-1.391 5.352c-.246.945-.141 1.271.106 1.271.317 0 1.357-.392 2.379-1.207l.6.814C12.098 19.02 9.365 20 8.309 20z"/></svg>
        <p>Vorschau der Änderungen. Wenn die Daten passen bitte übernehmen</p>
        </div>

        <!-- New Users  -->
        <div class="max-w-lg w-full py-2"
            v-if="newUsers.length">
                <div class="py-2 bg-green-200  text-gray-700 text-s font-bold rounded-t-lg">
                    <div class="px-1"> Neue Users werden hinzugefügt </div>
                </div>
                <table class="w-full">
                    <thead class=" bg-gray-300  text-gray-700 text-s font-bold ">
                        <tr class="w-full">
                            <th class="px-2" >ID</th>
                            <th class="px-2">Vorname</th>
                            <th class="px-2">Nachname</th>
                            <th class="px-2">Gruppe</th>
                            <th class="px-2">Geb.</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="user in newUsers"
                    class="bg-gray-200  text-center w-full shadow-md py-2 px-1 border-b-2 border-gray-300 last:border-none">
                        <td class="">{{ user[idIndex] }}</td>
                        <td class="">{{ user[firstNameIndex] }}</td>
                        <td class="">{{ user[lastNameIndex] }}</td>
                        <td class="">{{ user[groupIndex] }}</td>
                        <td class="">{{ user[dateOfBirthIndex] }}</td>
                    </tr>
                    </tbody>
                </table>
        </div>
    
        <!-- changed Users orange-100 user.new[idIndex]} -->
        <div class="max-w-lg w-full py-2"
            v-if="changedUsers.length">
                <div class="py-2 bg-orange-100  text-gray-700 text-s font-bold rounded-t-lg">
                    <div class="px-1">Änderungen </div>
                </div>
                <table class="w-full">
                    <thead class=" bg-gray-300  text-gray-700 text-s font-bold ">
                        <tr class="w-full">
                            <th class="px-2" >ID</th>
                            <th class="px-2">Vorname</th>
                            <th class="px-2">Nachname</th>
                            <th class="px-2">Gruppe</th>
                            <th class="px-2">Geb.</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="user in changedUsers"
                    class="bg-gray-200  text-center w-full shadow-md py-2 px-1 border-b-2 border-gray-300 last:border-none">
                       
             
                        <td class="">
                            {{ user.old[idIndex] }}
                        </td>
                       
                        <td class="">
                            <div v-if="user.new[firstNameIndex] === user.old[firstNameIndex]">
                                {{ user.new[firstNameIndex] }}
                            </div>
                            <div v-else>
                                <p class=" inline line-through">{{ user.old[firstNameIndex] }}</p> 
                                <font-awesome-icon class="px-1" icon="a-solid fa fa-arrow-right" ></font-awesome-icon>
                                <p class="inline">{{ user.new[firstNameIndex] }}</p> 
                            </div>
                        </td>
                        <td class="">
                            <div v-if="user.new[lastNameIndex] === user.old[lastNameIndex]">
                                {{ user.new[lastNameIndex] }}
                            </div>
                            <div v-else>
                                <p class=" inline line-through">{{ user.old[lastNameIndex] }}</p> 
                                <font-awesome-icon class="px-1" icon="a-solid fa fa-arrow-right" ></font-awesome-icon> 
                                <p class="inline">{{ user.new[lastNameIndex] }}</p> 
                            </div>
                        </td>

                        <td class="">
                            <div v-if="user.new[groupIndex] === user.old[groupIndex]">
                                {{ user.new[groupIndex] }}
                            </div>
                            <div v-else>
                                <p class=" inline line-through">{{ user.old[groupIndex] }}</p>
                                <font-awesome-icon class="px-1" icon="a-solid fa fa-arrow-right" ></font-awesome-icon>
                                <p class="inline">{{ user.new[groupIndex] }}</p> 
                            </div>
                        </td>
                        <td class="">
                            <div v-if="user.new[dateOfBirthIndex] === user.old[dateOfBirthIndex]">
                                {{ user.new[dateOfBirthIndex] }}
                            </div>
                            <div v-else>
                                <p class=" inline line-through">{{ user.old[dateOfBirthIndex] }}</p> 
                                <font-awesome-icon class="px-1" icon="a-solid fa fa-arrow-right" ></font-awesome-icon>
                                <p class="inline">{{ user.new[dateOfBirthIndex] }}</p> 
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
        </div> 

        <!-- deactive Users -->
        <div class="max-w-lg w-full py-2"
            v-if="deactiveUsers.length >1">
                <div class="py-2 bg-red-100  text-gray-700 text-s font-bold rounded-t-lg">
                    <div class="px-1"> Users werden deaktiviert </div>
                </div>
                <table class="w-full">
                    <thead class=" bg-gray-300  text-gray-700 text-s font-bold ">
                        <tr class="w-full">
                            <th class="px-2" >ID</th>
                            <th class="px-2">Vorname</th>
                            <th class="px-2">Nachname</th>
                            <th class="px-2">Gruppe</th>
                            <th class="px-2">Geb.</th>

                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="user in deactiveUsers"
                    class="bg-gray-200  text-center w-full shadow-md py-2 px-1 border-b-2 border-gray-300 last:border-none">
                        <td class="">{{ user[idIndex] }}</td>
                        <td class="">{{ user[firstNameIndex] }}</td>
                        <td class="">{{ user[lastNameIndex] }}</td>
                        <td class="">{{ user[groupIndex] }}</td>
                        <td class="">{{ user[dateOfBirthIndex] }}</td>
                    </tr>
                    </tbody>
                </table>
        </div>
        <button
        @click="postFile"
        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded ">
            Änderungen übernehmen
        </button>
    </div>

    <!-- No change Message -->
    <div class="mt-10 bg-blue-100  border-t-4 border-blue-500 rounded-b text-teal-900 px-4 py-3 shadow-md"
    v-if="noChange">
    <div class="flex">
        <div class="py-1"><svg class="fill-current h-6 w-6 text-blue-700 mr-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z"/></svg></div>
        <div>
            <p class="font-bold">Keine Änderungen festgestellt!</p>
            <p class="text-sm">Daten sind aktuell.</p>
        </div>
        </div>
    </div>    

    <!--SuccesMessage-->
    <div class=" max-w-lg w-full mt-10 bg-teal-200  border-t-4 border-teal-500 rounded-b text-teal-900 px-4 py-3 shadow-md"
    v-if="success">
    <div class="flex">
        <div class="py-1"><svg class="fill-current h-6 w-6 text-teal-700 mr-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z"/></svg></div>
         <div>
            <p class="font-bold">Änderungen übernommen</p>
            <div v-if="newUsersLength > 0">
                <p>Es würden {{newUsersLength}} neue Benutzer hinzugefügt. Laden Sie die CSV-Datei herunter, um den Nutzer ihr Einmal-Passwort zu geben</p>
                <button 
                class=" bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 border border-green-700 rounded" 
                @click="downloadBlob">download csv
                </button>
            </div>
        </div>
    </div>
    </div> 
   
    <!--ErrorMessage-->
    <div v-if="err"
        class=" flex flex-col flex-1 items-center">
        <p class="flex items-center bg-red-200 px-2 py-4 rounded" >{{ errorMessage }}</p>
    </div>
    
</template>

<script setup>
import axios from 'axios' ;
import { ref } from 'vue';
import Papa from 'papaparse';
import FormData from 'form-data';
import authHeader from '../services/authHeader';

const props = defineProps(['csvFile','file']);
const currentUsers = ref();
const newUsers = ref([]);
const changedUsers = ref([]);
const deactiveUsers = ref([]);

const success = ref(false);
const showPreview = ref(false);
const err = ref(false);
const noChange = ref(false);

const idIndex = 0;
const firstNameIndex = 1;
const lastNameIndex = 2;
const groupIndex=3;
const dateOfBirthIndex = 4;

const newUsersCsv = ref();
const newUsersLength = ref();

const getUsers = async () => {
    
	const response = await axios.get(
		`/api/v1/user/download`,{headers:authHeader()}
	);
    var results = Papa.parse(response.data);
    currentUsers.value = results;

    //Delay renedering by 1 sec
    await new Promise((res)=> setTimeout(res,1000));
};

function postFile() {
    const file = props.file
    let formData = new FormData();
    formData.append('file', file);
    let user = JSON.parse(localStorage.getItem('user'));
    axios.post('/api/v1/user',formData,{
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': 'Bearer '+ user.jwt
        }
    }
    ).then(function (response) {
    //handle success
    success.value = true;
    showPreview.value= false;
    noChange.value = false;
    err.value = false;
    if(response.data && response.data.length > 0){
        //parse JSON -> csv 
        newUsersCsv.value = Papa.unparse(response.data);
        newUsersLength.value = response.data.length;
        console.log(newUsersLength.value)
    }
    
  })
  .catch(function (response) {
    //handle error
    success.value= false;
    showPreview.value =false;
    noChange.value = false;
    err.value = true;
  });
}


await getUsers();

const analyseChanges =() =>{
    const dataBaseUsersArray = currentUsers.value.data;
    const csvUsersArray = props.csvFile.data;

    //sclice(1) to skip headers
    csvUsersArray.slice(1).forEach( (csvUser )=> {
        // find user by id
        let result = dataBaseUsersArray.find(user => user[idIndex] == csvUser[idIndex])

        let index = dataBaseUsersArray.findIndex( (user) => user[idIndex] == csvUser[idIndex])
        //console.log(index)

        //User not found ==> new User
        if(index < 0){
            newUsers.value.push(csvUser);
        }
        //User found ==> changeDetected = true
        else if(changeDetected(dataBaseUsersArray[index],csvUser)){
            changedUsers.value.push({old: result, new: csvUser});
            dataBaseUsersArray.splice(index,1)
        }
        else{
            dataBaseUsersArray.splice(index,1)
        }


    });
    //Remaining users to be deactivated
    deactiveUsers.value = dataBaseUsersArray;

    //if csv and database have differeces show the preview
    if(newUsers.value.length > 0 || changedUsers.value.length >0 || deactiveUsers.value.length >1){
        showPreview.value = true;
        noChange.value = false;
    }
    else{
        showPreview.value = false;
        noChange.value = true;
    }
}

function changeDetected(a,b) {
    const oldArray = a;
    const latestArray = b;
    // Check length of both arrays
    // if length not equal then arrays are different
    if(oldArray.length!=latestArray.length)
        return true;
    else {
        // check every element of the two arrays
        for(var i=0;i<oldArray.length;i++)
            if(oldArray[i]!=latestArray[i]){
                return true;
            }
        return false;
        }
}
analyseChanges();

function downloadBlob(){
    const blob =  new Blob([newUsersCsv.value],{type:'text/csv;charset-utf-8'});
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.download='newUsers.csv';
    a.href = url;
    a.click();    
}
</script>
