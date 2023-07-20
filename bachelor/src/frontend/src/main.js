import './assets/tailwind.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret,faBarcode,faUser, faArrowRight,faCheck, faX } from '@fortawesome/free-solid-svg-icons'

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import helper from './helper/dateFormatter'

/*Impor Vuex for state management*/
import store from './store'

library.add(faUserSecret,faBarcode,faUser,faArrowRight,faCheck,faX)
const app = createApp(App).component('font-awesome-icon', FontAwesomeIcon);

app.use(router)
app.use(helper)
app.use(store)

app.mount('#app')
