import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import router from "./router.ts";
import PrimeVue from 'primevue/config';
import {MyPreset} from "./presets/MyPreset.ts";


createApp(App)
    .use(router)
    .use(PrimeVue, {
        theme: {
            preset: MyPreset,
            options: {
                options: {
                    darkModeSelector: false,
                }
            }
        }
    })
    .mount('#app')


