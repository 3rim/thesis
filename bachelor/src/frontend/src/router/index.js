import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LendUserSearchView from '../views/LendUserSearchView.vue'
import LendView from '../views/LendView.vue'
import InventoryView from '../views/InventoryView.vue'
import InventoryDetailsView from '../views/InventoryDetailsView.vue'
import MediumView from '../views/MediumView.vue'
import InventoryAddView from '../views/InventoryAddView.vue'
import InventoryDeleteView from '../views/InventoryDeleteView.vue'
import UserImportView from '../views/UserImportView.vue'
import LoginView from '../views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/myProfile',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: "login",
      component: LoginView
    },
    {
      path: '/ausleihe',
      name: 'ausleihe',
      component: LendUserSearchView,
    },
    {
      path: '/ausleihe/user',
      name: 'ausleiheUser',
      component: LendView
    },
    {
      path: '/inventory',
      name: 'inventory',
      component: InventoryView,
      children:[
        {path: ':mediumID' , component: MediumView},
        {path: 'title/:title' ,component: InventoryDetailsView}
      ]
    },
    {
      path: '/inventory/add',
      name: 'inventoryAdd',
      component: InventoryAddView,
    },
    {
      path: '/inventory/delete',
      name: 'inventoryDelete',
      component: InventoryDeleteView,
    },
    {
      path: '/admin/userImport',
      name: 'userImport',
      component: UserImportView,
    }
  ]
})

export default router
