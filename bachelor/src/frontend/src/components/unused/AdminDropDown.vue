<template>
    <div class="relative">
        <button @click="isOpen = !isOpen"  class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-navbar-buttons">Admin</button>
        <button v-if="isOpen" @click="isOpen = false" tabindex="-2" class="fixed bg-black inset-0 h-full w-full opacity-40"></button>
        <div v-if="isOpen" class="absolute left-0 mt-2 py-2 w-48 rounded-lg shadow-xl bg-white">
            <router-link :to="{name: 'users'}">
                <p @click="isOpen = false" class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-slate-500">Users</p>
            </router-link>
            
            <router-link :to="{ name: 'userImport'}">
                <p @click="isOpen = false" class="mt-1 block px-2 py-1 font-semibold rounded hover:bg-slate-500">User Import</p>
            </router-link>
        </div>
    </div>
</template>

<script  lang="ts">


export default {
    data(){
        return{
            isOpen:false,
        }
    },
    created(){
        const handleEscape = (e:KeyboardEvent) =>{
            if(e.key=== 'Esc' || e.key === 'Escape'){
                this.isOpen = false
            }
        }
        document.addEventListener('keydown',handleEscape)

        
    }
    
}
/**https://michaelnthiessen.com/hover-in-vue/  funktioniert aber nicht wie gewünscht  */


</script>
