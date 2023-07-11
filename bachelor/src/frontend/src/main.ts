import './assets/tailwind.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret,faBarcode,faUser, faArrowRight } from '@fortawesome/free-solid-svg-icons'

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(faUserSecret,faBarcode,faUser,faArrowRight)
const app = createApp(App).component('font-awesome-icon', FontAwesomeIcon);

app.use(router)
app.mount('#app')
