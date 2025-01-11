import axios from "axios";
import type {LoginDto, RegisterDto} from './types';
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

export const updateProfile = async (data: any) => {
    return axiosInstance.put('/api/user/update', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}