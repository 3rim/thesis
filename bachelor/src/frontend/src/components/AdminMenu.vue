<template>
    <Menu as="div" class="relative inline-block text-left">
      <div>
        <MenuButton class="inline-flex w-full justify-center gap-x-1.5 mt-1  px-2 py-1 font-semibold rounded hover:bg-navbar-buttons  text-s text-gray-900  ring-gray-300 ">
          Admin
          <ChevronDownIcon class="-mr-1 h-6 w-5 text-gray-400" aria-hidden="true" />
        </MenuButton>
      </div>
  
      <transition enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100" leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
        <MenuItems class="absolute right-0 z-10 mt-2 w-56 origin-top-right divide-y divide-gray-100 rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
          
          <MenuItem v-for="item in navigation" :key="item.name" v-slot="{ active }">
              <a @click="navigateTo(item.path)"  class=" hover:cursor-pointer" :class="[active ? 'bg-gray-100 text-gray-900' : 'text-gray-700', 'block px-4 py-2 text-sm']" >
                  {{ item.name }}
              </a>
          </MenuItem>
        
        </MenuItems>
      </transition>
    </Menu>
</template>
  
<script setup> 
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/vue'
import { ChevronDownIcon } from '@heroicons/vue/20/solid'
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateTo = (path) =>{
  console.log(path);
  router.push(path);
}

let navigation = [
  {
    name: "Users",
    path: "/admin/users"
  },
  {
    name: "User import",
    path: "/admin/userImport"
  },
]


</script>