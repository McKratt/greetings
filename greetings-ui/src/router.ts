import {createRouter, createWebHistory} from "vue-router";
import GreetingForm from "./views/GreetingForm.vue";


export default createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/form',
        },
        {
            path: '/form',
            component: GreetingForm
        },
        {
            path: '/stats',
            component: () => import('./views/Stats.vue'),
        },
        {
            path: '/messages/:id',
            component: ()=> import('./views/Message.vue')
        }
    ]
})