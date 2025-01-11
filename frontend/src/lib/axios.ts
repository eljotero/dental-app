import axios from "axios";
import type {LoginDto, RegisterDto, UpdateUserProfile, CreateAppointment} from './types';
import store from "@/store";

const axiosInstance = axios.create({
    baseURL: 'https://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true
});

export default axiosInstance;

export const register = async (data: RegisterDto) => {
    return axiosInstance.post('/api/user/register', data);
}

export const login = async (data: LoginDto) => {
    return axiosInstance.post('/api/user/login', data);
}

export const resetPassword = async (email: string) => {
    return axiosInstance.post('/api/user/reset-password', {email});
}

export const getTreatments = async () => {
    return axiosInstance.get('/api/services/all');
}

export const getTreatment = async (id: number) => {
    return axiosInstance.get(`/api/services/${id}`);
}

export const getUserProfile = async () => {
    return axiosInstance.get('/api/user/me', {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const updateProfile = async (data: UpdateUserProfile) => {
    return axiosInstance.put('/api/user/update', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const fetchMyAppointments = async () => {
    if (store.getters.getRole === 'PATIENT') {
        return axiosInstance.get('/api/appointments/patient', {
            headers: {
                Authorization: `Bearer ${store.getters.getUserToken}`
            }
        });
    } else {
        return axiosInstance.get('/api/appointments/doctor', {
            headers: {
                Authorization: `Bearer ${store.getters.getUserToken}`
            }
        });
    }
}

export const fetchAppointment = async (id: number) => {
    return axiosInstance.get(`/api/appointments/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const confirmAppointment = async (id: number) => {
    return axiosInstance.get(`/api/appointments/confirm/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const cancelAppointment = async (id: number) => {
    return axiosInstance.post(`/api/appointments/cancel/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const getDoctorsForAppointment = async() => {
    return axiosInstance.get('/api/user/doctors', {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const getDoctorsAvailability = async(id: number, date: string) => {
    return axiosInstance.get(`/api/availability/slots/${id}/${date}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const createAppointment = async(data: CreateAppointment) => {
    return axiosInstance.post('/api/appointments/add', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}