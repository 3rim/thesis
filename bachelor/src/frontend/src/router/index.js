import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LendUserSearchView from '../views/LendUserSearchView.vue'
import LoanView from '../views/LoanView.vue'
import InventoryView from '../views/InventoryView.vue'
import MediaSeriesDetailsView from '../views/MediaSeriesDetailsView.vue'
import MediumView from '../views/MediumView.vue'
import AddMediaSeriesView from '../views/AddMediaSeriesView.vue'
import InventoryDeleteView from '../views/InventoryDeleteView.vue'
import AddMediaView from '../views/AddMediaView.vue'
import UserImportView from '../views/UserImportView.vue'
import UserView from '../views/UserView.vue'
import UserRolesView from '../views/UserRolesView.vue'
import LoginView from '../views/LoginView.vue'
import qs from 'qs'
import store from '../store'

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
      name: 'profile',
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
      name: 'loanView',
      component: LoanView
    },
    {
      path: '/inventory',
      name: 'inventory',
      component: InventoryView,
      children:[
        {path: ':mediumID' , component: MediumView},
        {path: 'series' , component: AddMediaSeriesView},
        {path: 'series/:seriesID/media' , component: AddMediaView},
        
      ]
    },
    {
      path: '/inventory/series/:seriesID',
      name: 'seriesDetetails',
      component: MediaSeriesDetailsView,
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
    },
    {
      path: '/admin/users',
      name: 'users',
      component: UserView,
    },
    {
      path: '/admin/users/:id/roles',
      name: 'userRoles',
      component: UserRolesView,
    }
  ],
  parseQuery: qs.parse,
  stringifyQuery: qs.stringify,
})

router.beforeEach(async(to, from) => {
  const loggedIn = store.state.auth.status.loggedIn
  if(!loggedIn && 
    // ❗️ Avoid an infinite redirect
    to.name !== 'login'
    ){
    return {name: 'login'}
  }
})

export default router
