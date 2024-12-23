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
}