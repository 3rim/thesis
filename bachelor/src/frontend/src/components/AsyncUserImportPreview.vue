<template>
    <div>

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
                                <font-awesome-icon class="px-1" icon="a-solid fa fa-arrow-right" size="x"></font-awesome-icon>
                                <p class="inline">{{ user.new[firstNameIndex] }}</p> 
                            </div>
                        </td>
                        <td class="">
                            <div v-if="user.new[lastNameIndex] === user.old[lastNameIndex]">
                                {{ user.new[lastNameIndex] }}
                            </div>
                            <div v-else>
                                <p class=" inline line-through">{{ user.old[lastNameIndex] }}</p> -> <p class="inline">{{ user.new[lastNameIndex] }}</p> 
                            </div>
                        </td>

                        <td class="">
                            <div v-if="user.new[groupIndex] === user.old[groupIndex]">
                                {{ user.new[groupIndex] }}
                            </div>
                            <div v-else>
                                <p class=" inline line-through">{{ user.old[groupIndex] }}</p> -> <p class="inline">{{ user.new[groupIndex] }}</p> 
                            </div>
                        </td>
                        <td class="">
                            <div v-if="user.new[dateOfBirthIndex] === user.old[dateOfBirthIndex]">
                                {{ user.new[dateOfBirthIndex] }}
                            </div>
                            <div v-else>
                                <p class=" inline line-through">{{ user.old[dateOfBirthIndex] }}</p> -> <p class="inline">{{ user.new[dateOfBirthIndex] }}</p> 
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
        </div> 

        <!-- deactive Users -->
        <div class="max-w-lg w-full py-2"
            v-if="deactiveUsers.length">
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
    </div>
</template>

<script setup>
import axios from 'axios' ;
import { ref } from 'vue';
import Papa from 'papaparse';

const props = defineProps(['csvFile']);
const currentUsers = ref();
const newUsers = ref([]);
const changedUsers = ref([]);
const deactiveUsers = ref([]);

const idIndex = 0;
const firstNameIndex = 1;
const lastNameIndex = 2;
const groupIndex=3;
const dateOfBirthIndex = 4;

const previewData = async () =>{
    // test loading preview
    await new Promise ((resolve) => setTimeout(resolve,3000));
    
}

const getUsers = async () => {
	const response = await axios.get(
		`/api/v1/user/download`
	);
    var results = Papa.parse(response.data);
    currentUsers.value = results;
};

await previewData();
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
            console.log("changeee")
            changedUsers.value.push({old: result, new: csvUser});
            dataBaseUsersArray.splice(index,1)
        }
        else{
            dataBaseUsersArray.splice(index,1)
        }


    });
    //Remaining users to be deactivated
    deactiveUsers.value = dataBaseUsersArray;

    console.log("newUsers.value")
    console.log(newUsers.value)

    console.log("changedUsers.value")
    console.log(changedUsers.value)

    console.log("deactiveUsers.value")
    console.log(deactiveUsers.value)

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
function compareValues(oldValue,newValue){

}
analyseChanges();
</script>
