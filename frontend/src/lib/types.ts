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

export interface Appointment {
    appointmentId: number;
    appointmentDate: string;
    appointmentStartTime: string;
    appointmentEndTime: string;
}

export interface AppointmentDetails {
    doctorName: string;
    doctorLastName: string;
    doctorPhoneNumber: string;
    appointmentDate: string;
    appointmentStartTime: string;
    appointmentEndTime: string;
    description: string;
    cancelled: boolean;
    paid: boolean;
    confirmed: boolean;
    prescriptions: Prescription[];
    referrals: Referral[];
}

export interface Prescription {
    medicineName: string;
    dosage: string;
}

export interface Referral {
    procedureName: string;
    procedureDescription: string;
    doctorName: string;
    clinicName: string;
    clinicAddress: string;
}

export interface GetDoctorDto {
    doctorId: number;
    firstName: string;
    lastName: string;
}

export interface TimeSlots {
    [startTime: string]: string;
}

export interface CreateAppointment {
    doctorId: number;
    appointmentDate: string;
    appointmentStartTime: string;
    appointmentEndTime: string;
}