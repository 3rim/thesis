<template>
    <div>

        <!-- New Users -->

            <div class=" max-w-lg w-full py-2"
             v-if="newUsers.length">
            <div class="flex flex-row text-gray-700 text-s font-bold">
                <div class=""> Neue Users </div>
            </div>
                <ul class="bg-gray-200  w-full shadow-md py-2 px-1 top-[66px]">
                    <li v-for="user in newUsers"
                    class="py-2 ">
                        <div class="flex flex-row">
                            <div class="w-5/6 flex flex-row" >                                  <!--flex justify-end => position right -->
                                <div class=""> {{ user[idIndex]}}</div> <div class="w-1/2 pl-10 "> {{ user[firstNameIndex]}}</div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
    

        <!-- changed Users -->
        <div class=" max-w-lg w-full py-2"
             v-if="changedUsers.length">
            <div class="flex flex-row text-gray-700 text-s font-bold">
                <div class=""> Änderungen </div>
            </div>
                <ul class="bg-gray-200  w-full shadow-md py-2 px-1 top-[66px]">
                    <li v-for="user in changedUsers"
                    class="py-2 ">
                        <div class="flex flex-row">
                            <div class="w-5/6 flex flex-row" >                                  <!--flex justify-end => position right -->
                                <div class=""> {{ user.new[idIndex]}}</div> <div class="w-1/2 pl-10 "> {{ user.new[firstNameIndex]}}</div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>  

        <!-- deactive Users -->
        <div>

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

    csvUsersArray.forEach( (csvUser )=> {
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
analyseChanges();
</script>
