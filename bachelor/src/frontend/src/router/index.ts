import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AusleihView from '../views/AusleihView.vue'

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
      component: AusleihView
    },
  ]
})

export default router
