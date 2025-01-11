export interface Option {
    value: string;
    label: string;
}

export interface RegisterDto {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phoneNumber: string;
    sex: boolean;
    personalIdNumber: string;
    country: string;
    city: string;
    addressLine: string;
    zipCode: string;
    dateOfBirth: string;
    language: string;
}

export interface LoginDto {
    email: string;
    password: string;
}

export interface State {
    userToken: string | null;
    role: string | null;
    language: string;
}

export interface Treatment {
    treatmentId: number;
    treatmentName: string;
    treatmentDescription: string;
    treatmentPrice: number;
    isActive: boolean;
}

export interface UserProfile {
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    sex: boolean;
    country: string;
    city: string;
    address: string;
    zipCode: string;
    dateOfBirth: string;
}

export interface UpdateUserProfile {
    firstName?: string;
    lastName?: string;
    email?: string;
    phoneNumber?: string;
    sex?: boolean;
    country?: string;
    city?: string;
    address?: string;
    zipCode?: string;
    dateOfBirth?: string;
}