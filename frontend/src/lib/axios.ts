import axios from "axios";
import type {RegisterDto} from './types';

const axiosInstance = axios.create({
    baseURL: 'https://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true,
});

export default axiosInstance;

export const register = async(data: RegisterDto) => {
    return axiosInstance.post('/api/user/register', data);
}