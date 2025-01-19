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
    [key: string]: any;
    treatmentId: number;
    treatmentName: string;
    treatmentDescription: string;
    treatmentPrice: number;
}

export interface CreateTreatment {
    [key: string]: any;
    treatmentName: string;
    treatmentDescription: string;
    treatmentPrice: number;
}

export interface UpdateTreatment {
    [key: string]: any;
    treatmentName?: string;
    treatmentDescription?: string;
    treatmentPrice?: number;
}

export interface UserProfile {
    [key: string]: any;
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
    [key: string]: any;
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
    [key: string]: any;
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
    files: FileDetails[];
}

export interface AppointmentDetailsDoctor {
    [key: string]: any;
    patientName: string;
    patientLastName: string;
    patientPhoneNumber: string;
    appointmentDate: string;
    appointmentStartTime: string;
    appointmentEndTime: string;
    description: string;
    cancelled: boolean;
    paid: boolean;
    confirmed: boolean;
    prescriptions: Prescription[];
    referrals: Referral[];
    files: FileDetails[];
}

export interface DoctorAppointments {
    [key: string]: any;
    appointmentId: number;
    patientInfo: string;
    appointmentDate: string;
    appointmentStartTime: string;
    appointmentEndTime: string;
}

export interface Prescription {
    prescriptionId: number;
    medicineName: string;
    dosage: string;
}

export interface CreatePrescription {
    appointmentId: number;
    medicineName: string;
    dosage: string;
}

export interface UpdatePrescription {
    medicineName?: string;
    dosage?: string;
}

export interface Referral {
    referralId: number;
    procedureName: string;
    procedureDescription: string;
    doctorName: string;
    clinicName: string;
    clinicAddress: string;
}

export interface UpdateReferral {
    procedureName?: string;
    procedureDescription?: string;
    doctorName?: string;
    clinicName?: string;
    clinicAddress?: string;
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

export interface FileDetails {
    fileId: number;
    fileName: string;
    uploadedAt: string;
    updatedAt: string;
}