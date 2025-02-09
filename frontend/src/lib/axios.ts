import axios from "axios";
import type {
    LoginDto,
    RegisterDto,
    UpdateUserProfile,
    CreateAppointment,
    UpdateTreatment,
    CreateTreatment,
    UpdatePrescription,
    CreatePrescription,
    CreateReferral,
    UpdateAppointment,
    PayAppointment,
    CreateSupply, UpdateSupply, CreateAvailability
} from './types';
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

export const createTreatment = async(data: CreateTreatment) => {
    return axiosInstance.post('/api/services/create', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const updateTreatment = async(id: number, data:UpdateTreatment) => {
    return axiosInstance.put(`/api/services/${id}`, data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const deleteTreatment = async(id: number) => {
    return axiosInstance.delete(`/api/services/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
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
    if(store.getters.getRole === 'PATIENT') {
        return axiosInstance.get(`/api/appointments/${id}`, {
            headers: {
                Authorization: `Bearer ${store.getters.getUserToken}`
            }
        });
    } else {
        return axiosInstance.get(`/api/appointments/doctor/${id}`, {
            headers: {
                Authorization: `Bearer ${store.getters.getUserToken}`
            }
        });
    }
}

export const confirmAppointment = async (id: number) => {
    return axiosInstance.get(`/api/appointments/confirm/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const cancelAppointment = async (id: number) => {
    return axiosInstance.post(`/api/appointments/cancel/${id}`, {}, {
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

export const getDoctorsAvailability = async(id: number, startDate: string, endDate: string) => {
    return axiosInstance.get(`/api/availability/slots/${id}?startDate=${startDate}&endDate=${endDate}`, {
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

export const changeLanguage = async(language: string) => {
    return axiosInstance.post('/api/user/change-language', {language}, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const createPrescription = async(data: CreatePrescription) => {
    return axiosInstance.post('/api/prescriptions/add', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const updatePrescription = async(id: number, data: UpdatePrescription) => {
    return axiosInstance.put(`/api/prescriptions/${id}`, data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const deletePrescription = async(id: number) => {
    return axiosInstance.delete(`/api/prescriptions/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const updateReferral = async(id: number, data: any) => {
    return axiosInstance.put(`/api/referrals/${id}`, data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const deleteReferral = async(id: number) => {
    return axiosInstance.delete(`/api/referrals/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const createReferral = async(data: CreateReferral) => {
    return axiosInstance.post('/api/referrals/add', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const deleteFile = async (id: number) => {
    return axiosInstance.delete(`/api/files/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const uploadFile = async (id: number, formData: FormData) => {
    return axiosInstance.post(`/api/files/upload?id=${id}`, formData, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`,
            'Content-Type': 'multipart/form-data',
        }
    });
};

export const downloadFile = async (id: number) => {
    return axiosInstance.get(`/api/files/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const updateAppointment = async (id: number, data: UpdateAppointment) => {
    return axiosInstance.put(`/api/appointments/${id}`, data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const setNewPassword = async (password: string, token: string) => {
    return axiosInstance.post(`/api/user/change-password?token=${token}`, { password });
}

export const payForAppointment = async (id: number, data: PayAppointment) => {
    return axiosInstance.post(`/api/payments/${id}/pay`, data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const updateAppointmentPrice = async (id: number, price: number) => {
    return axiosInstance.patch(`/api/payments/${id}/price`, { price }, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const getSupplies = async () => {
    return axiosInstance.get('/api/supply/all', {
            headers: {
                Authorization: `Bearer ${store.getters.getUserToken}`
            }
        }
    );
}

export const createSupply = async (data: CreateSupply) => {
    return axiosInstance.post('/api/supply/add', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const updateSupply = async (id: number, data: UpdateSupply) => {
    return axiosInstance.put(`/api/supply/${id}`, data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const deleteSupply = async (id: number) => {
    return axiosInstance.delete(`/api/supply/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const getAvailabilities = async () => {
    return axiosInstance.get('/api/availability/doctor', {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const createAvailability = async (data: CreateAvailability) => {
    return axiosInstance.post('/api/availability/add', data, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const deleteAvailability = async (id: number) => {
    return axiosInstance.delete(`/api/availability/${id}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}

export const getStatistics = async (startDate: Date) => {
    return axiosInstance.get(`/api/statistics/all?startDate=${startDate}`, {
        headers: {
            Authorization: `Bearer ${store.getters.getUserToken}`
        }
    });
}