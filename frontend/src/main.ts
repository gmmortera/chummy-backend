/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'

// Composables
import { createApp } from 'vue'
import axios from 'axios'

const app = createApp(App)

axios.defaults.baseURL = import.meta.env.VITE_CHUMMY_BASEURL
axios.defaults.headers["Access-Control-Allow-Origin"] = "*"
axios.defaults.headers["Access-Control-Allow-Methods"] = "*"
axios.defaults.withCredentials = true

registerPlugins(app)

app.mount('#app')
