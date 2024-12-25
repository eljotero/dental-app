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