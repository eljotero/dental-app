import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView,
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('../views/RegisterView.vue'),
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/LoginView.vue'),
        },
        {
            path: '/priceList',
            name: 'priceList',
            component: () => import('../views/TreatmentsListView.vue'),
        },
        {
            path: '/profile',
            name: 'profile',
            component: () => import('../views/UserPanelView.vue'),
        },
        {
            path: '/appointments',
            name: 'appointments',
            component: () => import('../views/MyAppointmentsView.vue'),
        },
        {
            path: '/appointment/:appointmentId',
            name: 'appointment',
            component: () => import('../views/AppointmentView.vue'),
            props: true
        },
        {
            path: '/createAppointment',
            name: 'createAppointment',
            component: () => import('../views/CreateAppointmentView.vue')
        },
        {
            path: '/editTreatment/:treatmentId',
            name: 'treatment',
            component: () => import('../views/EditTreatmentView.vue'),
            props: true
        },
        {
            path: '/createTreatment',
            name: 'createTreatment',
            component: () => import('../views/AddTreatmentView.vue')
        }
    ],
})

export default router
