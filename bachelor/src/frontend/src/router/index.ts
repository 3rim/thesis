import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LendUserSearchView from '../views/LendUserSearchView.vue'
import LendView from '../views/LendView.vue'
import InventoryView from '../views/InventoryView.vue'
import InventoryDetailsView from '../views/InventoryDetailsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/myProfil',
      name: 'home',
      component: HomeView
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
        {path: 'title/:title' ,component: InventoryDetailsView}
      ]
    }
  ]
})

export default router
