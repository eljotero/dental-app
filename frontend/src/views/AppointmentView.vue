<script setup lang="ts">
import {
  cancelAppointment,
  confirmAppointment,
  createPrescription,
  createReferral,
  deleteFile,
  deletePrescription,
  deleteReferral,
  downloadFile,
  fetchAppointment, payForAppointment,
  updateAppointment, updateAppointmentPrice,
  updatePrescription,
  updateReferral,
  uploadFile
} from '@/lib/axios';
import type {
  AppointmentDetails,
  AppointmentDetailsDoctor,
  CreatePrescription,
  CreateReferral,
  Prescription,
  Referral,
  UpdateAppointment,
  UpdatePrescription,
  UpdateReferral,
  PayAppointment
} from '@/lib/types';
import {onMounted, ref} from 'vue';
import {Card, CardContent, CardHeader} from '@/components/ui/card';
import {Button} from '@/components/ui/button';
import {useI18n} from 'vue-i18n';
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import store from '@/store';
import {Table, TableBody, TableCell, TableRow} from "@/components/ui/table";
import {Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger} from "@/components/ui/dialog";
import StyledFormItem from "@/components/StyledFormItem.vue";
import {Form} from "@/components/ui/form";
import {toTypedSchema} from "@vee-validate/zod";
import * as z from 'zod';
import {handleRequest, handleSubmit} from '@/lib/functions';
import StyledSelectFormItem from "@/components/StyledSelectFormItem.vue";

const {t} = useI18n();

const props = defineProps<{
  appointmentId: string;
}>();

const appointment = ref<AppointmentDetails | AppointmentDetailsDoctor>({} as AppointmentDetails | AppointmentDetailsDoctor);
const role = store.getters.getRole;

const editPrescriptionFormRef = ref({} as UpdatePrescription);
const editPrescriptionOriginalData = {} as UpdatePrescription;
const selectedMedicineId = ref<number>(0);

const editReferralFormRef = ref({} as UpdateReferral);
const editReferralOriginalData = {} as UpdateReferral;
const selectedReferralId = ref<number>(0);

const editAppointmentFormRef = ref({} as UpdateAppointment);
const editAppointmentOriginalData = {} as UpdateAppointment;

const createMedicineFormSchema = toTypedSchema(z.object({
  medicineName: z.string().nonempty(t('medicineNameError')),
  dosage: z.string().nonempty(t('dosageError')),
}));

const createReferralFormSchema = toTypedSchema(z.object({
  procedureName: z.string().nonempty(t('procedureNameError')),
  procedureDescription: z.string().nonempty(t('procedureDescriptionError')),
  doctorName: z.string().nonempty(t('doctorNameError')),
  clinicName: z.string().nonempty(t('clinicNameError')),
  clinicAddress: z.string().nonempty(t('clinicAddressError')),
}));

const createPaymentFormSchema = toTypedSchema(z.object({
  price: z.number().positive(t('priceError')),
  paymentType: z.string().nonempty(t('paymentTypeError'))
}));

const editMedicineFormSchema = toTypedSchema(z.object({
  medicineName: z.string().nonempty(t('medicineNameError')),
  dosage: z.string().nonempty(t('dosageError')),
}));

const editReferralFormSchema = toTypedSchema(z.object({
  procedureName: z.string().nonempty(t('procedureNameError')),
  procedureDescription: z.string().nonempty(t('procedureDescriptionError')),
  doctorName: z.string().nonempty(t('doctorNameError')),
  clinicName: z.string().nonempty(t('clinicNameError')),
  clinicAddress: z.string().nonempty(t('clinicAddressError')),
}));

const uploadFileFormSchema = toTypedSchema(z.object({
  file: z.any()
}));

const editAppointmentFormSchema = toTypedSchema(z.object({
  appointmentDate: z.string().nonempty(t('appointmentDateError')),
  appointmentStartTime: z.string().nonempty(t('appointmentStartTimeError')),
  appointmentEndTime: z.string().nonempty(t('appointmentEndTimeError')),
  appointmentDescription: z.string().nonempty(t('descriptionError')),
  price: z.number().positive(t('priceError'))
}).refine(data => {
  const startTime = new Date(`${data.appointmentDate}T${data.appointmentStartTime}`);
  const endTime = new Date(`${data.appointmentDate}T${data.appointmentEndTime}`);
  return startTime < endTime;
}, {
  message: t('appointmentTimeError'),
  path: ['appointmentEndTime'],
}));

onMounted(async () => {
  const response = await fetchAppointment(Number(props.appointmentId));
  if (response.status === 200) {
    appointment.value = response.data;
  } else {
    console.error('Error while fetching appointment');
  }
});

const confirm = async (id: number) => {
  const response = await confirmAppointment(id);
  if (response.status === 200) {
    toast.success(t('appointmentConfirmed'), {
      autoClose: 2000,
    });
    appointment.value!.confirmed = true;
  } else {
    toast.error(t('appointmentConfirmError'), {
      autoClose: 2000,
    });
  }
};

const cancel = async (id: number) => {
  const response = await cancelAppointment(id);
  if (response.status === 200) {
    toast.success(t('appointmentCanceled'), {
      autoClose: 2000,
    });
    appointment.value!.cancelled = true;
  } else {
    toast.error(t('appointmentCancelError'), {
      autoClose: 2000,
    });
  }
};

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString();
};

const addPrescription = async (values: any) => {
  const dto = values as CreatePrescription;
  dto.appointmentId = Number(props.appointmentId);
  await handleRequest(
      (dto) => createPrescription(dto),
      dto,
      t('prescriptionCreated'),
      t('prescriptionCreateError'),
      t,
      201
  );
};

const addReferral = async (values: any) => {
  const dto = values as CreateReferral;
  dto.appointmentId = Number(props.appointmentId);
  await handleRequest(
      (dto) => createReferral(dto),
      dto,
      t('referralCreated'),
      t('referralCreateError'),
      t,
      201
  );
};

const setEditMedicineFormValues = (medicine: Prescription) => {
  editPrescriptionFormRef.value = {
    medicineName: medicine.medicineName,
    dosage: medicine.dosage,
  };
  selectedMedicineId.value = medicine.prescriptionId;
  editPrescriptionOriginalData.medicineName = medicine.medicineName;
  editPrescriptionOriginalData.dosage = medicine.dosage;
};

const setEditAppointmentFormValues = (appointment: AppointmentDetailsDoctor | AppointmentDetails) => {
  editAppointmentFormRef.value = {
    appointmentDate: appointment.appointmentDate,
    appointmentStartTime: appointment.appointmentStartTime,
    appointmentEndTime: appointment.appointmentEndTime,
    appointmentDescription: appointment.description,
    price: appointment.paymentAmount
  };
  editAppointmentOriginalData.appointmentDate = appointment.appointmentDate;
  editAppointmentOriginalData.appointmentStartTime = appointment.appointmentStartTime;
  editAppointmentOriginalData.appointmentEndTime = appointment.appointmentEndTime;
  editAppointmentOriginalData.appointmentDescription = appointment.description;
  editAppointmentOriginalData.price = appointment.paymentAmount;
};

const setEditReferralFormValues = (referral: Referral) => {
  editReferralFormRef.value = {
    procedureName: referral.procedureName,
    procedureDescription: referral.procedureDescription,
    doctorName: referral.doctorName,
    clinicName: referral.clinicName,
    clinicAddress: referral.clinicAddress,
  };
  selectedReferralId.value = referral.referralId;
  editReferralOriginalData.procedureName = referral.procedureName;
  editReferralOriginalData.procedureDescription = referral.procedureDescription;
  editReferralOriginalData.doctorName = referral.doctorName;
  editReferralOriginalData.clinicName = referral.clinicName;
  editReferralOriginalData.clinicAddress = referral.clinicAddress;
};

const onSubmitEditMedicine = async (values: any) => {
  await handleSubmit(values, editPrescriptionOriginalData, updatePrescription, selectedMedicineId.value, t('updateDataSuccess'), t('updateDataError'), t);
};

const onSubmitEditReferral = async (values: any) => {
  await handleSubmit(values, editReferralOriginalData, updateReferral, selectedReferralId.value, t('updateDataSuccess'), t('updateDataError'), t);
};

const removePrescription = async (id: number) => {
  const response = await deletePrescription(id);
  if (response.status === 200) {
    toast.success(t('prescriptionDeleted'), {
      autoClose: 2000,
    });
  } else {
    toast.error(t('prescriptionDeleteError'), {
      autoClose: 2000,
    });
  }
};

const removeReferral = async (id: number) => {
  const response = await deleteReferral(id);
  if (response.status === 200) {
    toast.success(t('referralDeleted'), {
      autoClose: 2000,
    });
    setTimeout(() => {
      window.location.reload();
    }, 2000);
  } else {
    toast.error(t('referralDeleteError'), {
      autoClose: 2000,
    });
  }
}

const addFile = async (values: any) => {
  const formData = new FormData();
  formData.append('file', values.file);
  formData.append('fileName', values.fileName);
  await handleRequest(
      () => uploadFile(Number(props.appointmentId), formData),
      formData,
      t('fileUploaded'),
      t('fileUploadError'),
      t,
      201
  );
};

const removeFile = async (id: number) => {
  const response = await deleteFile(id);
  if (response.status === 200) {
    toast.success(t('fileDeleted'), {
      autoClose: 2000,
    });
  } else {
    toast.error(t('fileDeleteError'), {
      autoClose: 3000,
    });
  }
  setTimeout(() => {
    window.location.reload();
  }, 2000);
}

const getFile = async (id: number) => {
  const response = await downloadFile(id);
  if (response.status === 200) {
    const fileName = response.data.fileName;
    const content = atob(response.data.fileData);
    const byteArray = new Uint8Array(content.length);
    for (let i = 0; i < content.length; i++) {
      byteArray[i] = content.charCodeAt(i);
    }
    const blob = new Blob([byteArray], {type: 'application/octet-stream'});
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  } else {
    console.error('Error while fetching file');
  }
};

const onSubmitEditAppointment = async (values: any) => {
  if (values.price !== editAppointmentOriginalData.price) {
    const price = Number(values.price);
    await handleRequest(
        () => updateAppointmentPrice(Number(props.appointmentId), price),
        { price },
        t('updateDataSuccess'),
        t('updateDataError'),
        t,
        200
    );
    delete values.price;
  }
  const newDto = { ...values };
  await handleSubmit(newDto, editAppointmentOriginalData, updateAppointment, Number(props.appointmentId), t('updateDataSuccess'), t('updateDataError'), t);
};

const paymentTypes = [
  {value: 'CASH', label: t('paymentTypeCash')},
  {value: 'CARD', label: t('paymentTypeCard')}
];

const onSubmitCreatePayment = async(values: any) => {
  const dto = values as PayAppointment;
  dto.paymentDate = new Date().toISOString().split('T')[0];
  await handleRequest(
      () => payForAppointment(Number(props.appointmentId), dto),
      dto,
      t('paymentSuccess'),
      t('paymentError'),
      t,
      200
  );
}

</script>

<template>
  <div>
    <div class="grid grid-cols-1 md:grid-cols-4 lg:grid-cols-4 gap-4">

      <Card v-if="appointment" class="shadow-lg rounded-lg overflow-hidden flex flex-col h-full">
        <CardHeader class="bg-blue-500 text-white p-4">
          <h2 class="text-xl font-semibold">{{ t('appointmentDetails') }}</h2>
        </CardHeader>
        <CardContent class="p-4">
          <Table class="w-full mb-4">
            <TableBody>
              <TableRow>
                <TableCell class="font-bold">{{ t('appointmentDate') }}</TableCell>
                <TableCell>{{ appointment.appointmentDate }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('appointmentStartTime') }}</TableCell>
                <TableCell>{{ appointment.appointmentStartTime }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('appointmentEndTime') }}</TableCell>
                <TableCell>{{ appointment.appointmentEndTime }}</TableCell>
              </TableRow>
              <TableRow v-if="role === 'DOCTOR'">
                <TableCell class="font-bold">{{ t('patientName') }}</TableCell>
                <TableCell>{{ appointment.patientName }}</TableCell>
              </TableRow>
              <TableRow v-if="role === 'DOCTOR'">
                <TableCell class="font-bold">{{ t('patientLastName') }}</TableCell>
                <TableCell>{{ appointment.patientLastName }}</TableCell>
              </TableRow>
              <TableRow v-if="role === 'DOCTOR'">
                <TableCell class="font-bold">{{ t('patientPhoneNumber') }}</TableCell>
                <TableCell>{{ appointment.patientPhoneNumber }}</TableCell>
              </TableRow>
              <TableRow v-else>
                <TableCell class="font-bold">{{ t('doctorName') }}</TableCell>
                <TableCell>{{ appointment.doctorName }}</TableCell>
              </TableRow>
              <TableRow v-else>
                <TableCell class="font-bold">{{ t('doctorLastName') }}</TableCell>
                <TableCell>{{ appointment.doctorLastName }}</TableCell>
              </TableRow>
              <TableRow v-else>
                <TableCell class="font-bold">{{ t('doctorPhoneNumber') }}</TableCell>
                <TableCell>{{ appointment.doctorPhoneNumber }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('description') }}</TableCell>
                <TableCell>{{ appointment.description }}</TableCell>
              </TableRow>
              <TableRow v-if="!appointment.paid">
                <TableCell class="font-bold">{{ t('paid') }}</TableCell>
                <TableCell>{{ appointment.paid ? t('yes') : t('no') }}</TableCell>
              </TableRow>
              <TableRow v-if="appointment.paymentDate">
                <TableCell class="font-bold">{{ t('paymentDateLabel') }}</TableCell>
                <TableCell>{{ appointment.paymentDate }}</TableCell>
              </TableRow>
              <TableRow v-if="appointment.paymentAmount > -1">
                <TableCell class="font-bold" >{{ t('paymentAmountLabel') }}</TableCell>
                <TableCell>{{ appointment.paymentAmount }} zł</TableCell>
              </TableRow>
              <TableRow v-if="appointment.paymentMethod">
                <TableCell class="font-bold">{{ t('paymentTypeLabel') }}</TableCell>
                <TableCell>{{ appointment.paymentMethod === 'Cash' ? t('paymentTypeCash') : t('paymentTypeCard') }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('canceled') }}</TableCell>
                <TableCell>{{ appointment.cancelled ? t('yes') : t('no') }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('confirmed') }}</TableCell>
                <TableCell>{{ appointment.confirmed ? t('yes') : t('no') }}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
          <div v-if="!appointment.confirmed && !appointment.cancelled" class="flex gap-4 justify-center">
            <Button @click="confirm(Number(appointmentId))" class="mt-4 flex-1 w-full">{{
                t('appointmentConfirmButton')
              }}
            </Button>
            <Button @click="cancel(Number(appointmentId))" class="mt-4 flex-1 w-full" variant='destructive'>
              {{ t('appointmentCancelButton') }}
            </Button>
            <div v-if="role === 'DOCTOR'" class="flex-1 w-full">
              <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createPaymentFormSchema">
                <Dialog>
                  <DialogTrigger as-child>
                    <Button class="mt-4 flex-1 w-full">
                      {{ t('appointmentPayButton') }}
                    </Button>
                  </DialogTrigger>
                  <DialogContent class="sm:max-w-[425px]">
                    <DialogHeader>
                      <DialogTitle>{{ t('appointmentPayButton') }}</DialogTitle>
                    </DialogHeader>
                    <form id="createDialogForm" @submit="handleSubmit($event, onSubmitCreatePayment)">
                      <StyledFormItem
                          inputName="price"
                          inputType="number"
                          :inputPlaceholder="t('paymentPricePlaceholder')"
                          labelFor="price"
                          :labelPlaceholder="t('paymentPriceLabelPlaceholder')"
                          errorMessageName="price"
                      />
                      <StyledSelectFormItem inputName="paymentType" labelFor="paymentType" :label="t('paymentLabel')" :labelPlaceholder="t('paymentLabelPlaceholder')" :options="paymentTypes" errorMessageName="paymentType"/>
                      <DialogFooter>
                        <DialogTrigger as-child>
                          <Button type="submit" form="createDialogForm">
                            {{ t('saveChanges') }}
                          </Button>
                        </DialogTrigger>
                      </DialogFooter>
                    </form>
                  </DialogContent>
                </Dialog>
              </Form>
            </div>
            <div v-if="role === 'DOCTOR'" class="flex-1 w-full">
              <Form v-slot="{ handleSubmit }" as="" :validation-schema="editAppointmentFormSchema" keep-values>
                <Dialog>
                  <DialogTrigger as-child>
                    <Button variant="outline" @click="setEditAppointmentFormValues(appointment)"
                            v-if="role === 'DOCTOR'" class="mt-4 flex-1 w-full">
                      {{ t('editButton') }}
                    </Button>
                  </DialogTrigger>
                  <DialogContent class="sm:max-w-[425px]">
                    <DialogHeader>
                      <DialogTitle>{{ t('editButton') }}</DialogTitle>
                    </DialogHeader>
                    <form id="editDialogForm" @submit="handleSubmit($event, onSubmitEditAppointment)">
                      <StyledFormItem inputName="appointmentDate" inputType="date"
                                      :inputPlaceholder="t('appointmentDate')" labelFor="appointmentDate"
                                      :labelPlaceholder="t('appointmentDate')" errorMessageName="appointmentDate"
                                      :modelValue="editAppointmentFormRef.appointmentDate"/>
                      <StyledFormItem inputName="appointmentStartTime" inputType="time"
                                      :inputPlaceholder="t('appointmentStartTime')" labelFor="appointmentStartTime"
                                      :labelPlaceholder="t('appointmentStartTime')"
                                      errorMessageName="appointmentStartTime"
                                      :modelValue="editAppointmentFormRef.appointmentStartTime"/>
                      <StyledFormItem inputName="appointmentEndTime" inputType="time"
                                      :inputPlaceholder="t('appointmentEndTime')" labelFor="appointmentEndTime"
                                      :labelPlaceholder="t('appointmentEndTime')" errorMessageName="appointmentEndTime"
                                      :modelValue="editAppointmentFormRef.appointmentEndTime"/>
                      <StyledFormItem inputName="appointmentDescription" inputType="text"
                                      :inputPlaceholder="t('appointmentDescription')" labelFor="appointmentDescription"
                                      :labelPlaceholder="t('appointmentDescription')"
                                      errorMessageName="appointmentDescription"
                                      :modelValue="editAppointmentFormRef.appointmentDescription"/>
                      <StyledFormItem inputName="price" inputType="number"
                                      :inputPlaceholder="t('paymentPricePlaceholder')" labelFor="price"
                                      :labelPlaceholder="t('paymentPricePlaceholder')"
                                      errorMessageName="price"
                                      :modelValue="editAppointmentFormRef.price"/>
                      <DialogFooter>
                        <DialogTrigger as-child>
                          <Button type="submit" form="editDialogForm" class="w-full">
                            {{ t('saveChanges') }}
                          </Button>
                        </DialogTrigger>
                      </DialogFooter>
                    </form>
                  </DialogContent>
                </Dialog>
              </Form>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card v-if="appointment.prescriptions" class="shadow-lg rounded-lg overflow-hidden flex flex-col h-full">
        <CardHeader class="bg-green-500 text-white p-4">
          <h2 class="text-xl font-semibold">{{ t('prescriptions') }}</h2>
        </CardHeader>
        <CardContent class="p-4 flex flex-col flex-grow">
          <Table class="w-full mb-4 flex-grow overflow-auto">
            <TableBody>
              <TableRow v-for="prescription in appointment.prescriptions" :key="prescription.medicineName" class="mb-2">
                <TableCell>
                  <strong>{{ t('medicineName') }}:</strong> {{ prescription.medicineName }}<br>
                  <strong>{{ t('dosage') }}:</strong> {{ prescription.dosage }}
                </TableCell>
                <TableCell class="flex justify-end gap-2" v-if="role === 'DOCTOR'">
                  <Form v-slot="{ handleSubmit }" as="" :validation-schema="editMedicineFormSchema" keep-values>
                    <Dialog>
                      <DialogTrigger as-child>
                        <Button variant="outline" @click="setEditMedicineFormValues(prescription)"
                                v-if="role === 'DOCTOR'">
                          {{ t('editButton') }}
                        </Button>
                      </DialogTrigger>
                      <DialogContent class="sm:max-w-[425px]">
                        <DialogHeader>
                          <DialogTitle>{{ t('editButton') }}</DialogTitle>
                        </DialogHeader>
                        <form id="editDialogForm" @submit="handleSubmit($event, onSubmitEditMedicine)">
                          <StyledFormItem inputName="medicineName" inputType="text"
                                          :inputPlaceholder="t('medicineName')" labelFor="medicineName"
                                          :labelPlaceholder="t('medicineName')" errorMessageName="medicineName"
                                          :modelValue="editPrescriptionFormRef.medicineName"/>
                          <StyledFormItem inputName="dosage" inputType="text" :inputPlaceholder="t('dosage')"
                                          labelFor="dosage" :labelPlaceholder="t('dosage')" errorMessageName="dosage"
                                          :modelValue="editPrescriptionFormRef.dosage"/>
                          <DialogFooter>
                            <DialogTrigger as-child>
                              <Button type="submit" form="editDialogForm">
                                {{ t('saveChanges') }}
                              </Button>
                            </DialogTrigger>
                          </DialogFooter>
                        </form>
                      </DialogContent>
                    </Dialog>
                  </Form>
                  <Button variant='destructive' @click="removePrescription(prescription.prescriptionId)">
                    {{ t('deleteButton') }}
                  </Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
        <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createMedicineFormSchema"
              v-if="role === 'DOCTOR'">
          <Dialog>
            <DialogTrigger as-child>
              <Button class="m-4 mx-auto w-1/2">
                {{ t('createButton') }}
              </Button>
            </DialogTrigger>
            <DialogContent class="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>{{ t('createButton') }}</DialogTitle>
              </DialogHeader>
              <form id="createDialogForm" @submit="handleSubmit($event, addPrescription)">
                <StyledFormItem inputName="medicineName" inputType="text" :inputPlaceholder="t('medicineName')"
                                labelFor="medicineName" :labelPlaceholder="t('medicineName')"
                                errorMessageName="medicineName"/>
                <StyledFormItem inputName="dosage" inputType="text" :inputPlaceholder="t('dosage')" labelFor="dosage"
                                :labelPlaceholder="t('dosage')" errorMessageName="dosage"/>
                <DialogFooter>
                  <DialogTrigger as-child>
                    <Button type="submit" form="createDialogForm">
                      {{ t('saveChanges') }}
                    </Button>
                  </DialogTrigger>
                </DialogFooter>
              </form>
            </DialogContent>
          </Dialog>
        </Form>
      </Card>

      <Card v-if="appointment.referrals" class="shadow-lg rounded-lg overflow-hidden flex flex-col h-full">
        <CardHeader class="bg-yellow-500 text-white p-4">
          <h2 class="text-xl font-semibold">{{ t('referrals') }}</h2>
        </CardHeader>
        <CardContent class="p-4 flex flex-col flex-grow">
          <Table class="w-full mb-4 flex-grow overflow-auto">
            <TableBody>
              <TableRow v-for="referral in appointment.referrals" :key="referral.procedureName" class="mb-2">
                <TableCell>
                  <strong>{{ t('procedureName') }}:</strong> {{ referral.procedureName }}<br>
                  <strong>{{ t('procedureDescription') }}:</strong> {{ referral.procedureDescription }}<br>
                  <strong>{{ t('doctorName') }}:</strong> {{ referral.doctorName }}<br>
                  <strong>{{ t('clinicName') }}:</strong> {{ referral.clinicName }}<br>
                  <strong>{{ t('clinicAddress') }}:</strong> {{ referral.clinicAddress }}
                </TableCell>
                <TableCell class="flex justify-end gap-2" v-if="role === 'DOCTOR'">
                  <Form v-slot="{ handleSubmit }" as="" :validation-schema="editReferralFormSchema" keep-values>
                    <Dialog>
                      <DialogTrigger as-child>
                        <Button variant="outline" @click="setEditReferralFormValues(referral)" v-if="role === 'DOCTOR'">
                          {{ t('editButton') }}
                        </Button>
                      </DialogTrigger>
                      <DialogContent class="sm:max-w-[425px]">
                        <DialogHeader>
                          <DialogTitle>{{ t('editButton') }}</DialogTitle>
                        </DialogHeader>
                        <form id="editDialogForm" @submit="handleSubmit($event, onSubmitEditReferral)">
                          <StyledFormItem inputName="procedureName" inputType="text"
                                          :inputPlaceholder="t('procedureName')" labelFor="procedureName"
                                          :labelPlaceholder="t('procedureName')" errorMessageName="procedureName"
                                          :modelValue="editReferralFormRef.procedureName"/>
                          <StyledFormItem inputName="procedureDescription" inputType="text"
                                          :inputPlaceholder="t('procedureDescription')" labelFor="procedureDescription"
                                          :labelPlaceholder="t('procedureDescription')"
                                          errorMessageName="procedureDescription"
                                          :modelValue="editReferralFormRef.procedureDescription"/>
                          <StyledFormItem inputName="doctorName" inputType="text" :inputPlaceholder="t('doctorName')"
                                          labelFor="doctorName" :labelPlaceholder="t('doctorName')"
                                          errorMessageName="doctorName" :modelValue="editReferralFormRef.doctorName"/>
                          <StyledFormItem inputName="clinicName" inputType="text" :inputPlaceholder="t('clinicName')"
                                          labelFor="clinicName" :labelPlaceholder="t('clinicName')"
                                          errorMessageName="clinicName" :modelValue="editReferralFormRef.clinicName"/>
                          <StyledFormItem inputName="clinicAddress" inputType="text"
                                          :inputPlaceholder="t('clinicAddress')" labelFor="clinicAddress"
                                          :labelPlaceholder="t('clinicAddress')" errorMessageName="clinicAddress"
                                          :modelValue="editReferralFormRef.clinicAddress"/>
                          <DialogFooter>
                            <DialogTrigger as-child>
                              <Button type="submit" form="editDialogForm">
                                {{ t('saveChanges') }}
                              </Button>
                            </DialogTrigger>
                          </DialogFooter>
                        </form>
                      </DialogContent>
                    </Dialog>
                  </Form>
                  <Button variant='destructive' @click="removeReferral(referral.referralId)">{{
                      t('deleteButton')
                    }}
                  </Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
        <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createReferralFormSchema"
              v-if="role === 'DOCTOR'">
          <Dialog>
            <DialogTrigger as-child>
              <Button class="m-4 mx-auto w-1/2">
                {{ t('createButton') }}
              </Button>
            </DialogTrigger>
            <DialogContent class="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>{{ t('createButton') }}</DialogTitle>
              </DialogHeader>
              <form id="createDialogForm" @submit="handleSubmit($event, addReferral)">
                <StyledFormItem inputName="procedureName" inputType="text" :inputPlaceholder="t('procedureName')"
                                labelFor="procedureName" :labelPlaceholder="t('procedureName')"
                                errorMessageName="procedureName"/>
                <StyledFormItem inputName="procedureDescription" inputType="text"
                                :inputPlaceholder="t('procedureDescription')" labelFor="procedureDescription"
                                :labelPlaceholder="t('procedureDescription')" errorMessageName="procedureDescription"/>
                <StyledFormItem inputName="doctorName" inputType="text" :inputPlaceholder="t('doctorName')"
                                labelFor="doctorName" :labelPlaceholder="t('doctorName')"
                                errorMessageName="doctorName"/>
                <StyledFormItem inputName="clinicName" inputType="text" :inputPlaceholder="t('clinicName')"
                                labelFor="clinicName" :labelPlaceholder="t('clinicName')"
                                errorMessageName="clinicName"/>
                <StyledFormItem inputName="clinicAddress" inputType="text" :inputPlaceholder="t('clinicAddress')"
                                labelFor="clinicAddress" :labelPlaceholder="t('clinicAddress')"
                                errorMessageName="clinicAddress"/>
                <DialogFooter>
                  <DialogTrigger as-child>
                    <Button type="submit" form="createDialogForm">
                      {{ t('saveChanges') }}
                    </Button>
                  </DialogTrigger>
                </DialogFooter>
              </form>
            </DialogContent>
          </Dialog>
        </Form>

      </Card>

      <Card v-if="appointment.files" class="shadow-lg rounded-lg overflow-hidden flex flex-col h-full">
        <CardHeader class="bg-red-500 text-white p-4">
          <h2 class="text-xl font-semibold">{{ t('filesLabel') }}</h2>
        </CardHeader>
        <CardContent class="p-4 flex flex-col flex-grow">
          <Table class="w-full mb-4 flex-grow overflow-auto">
            <TableBody>
              <TableRow v-for="file in appointment.files" :key="file.fileId" class="mb-2">
                <TableCell>
                  <strong>{{ t('fileName') }}:</strong> {{ file.fileName }}<br>
                  <strong>{{ t('uploadedAtLabel') }}:</strong> {{ formatDate(file.uploadedAt) }}<br>
                  <div v-if="file.updatedAt">
                    <strong>{{ t('updatedAtLabel') }}:</strong> {{ formatDate(file.updatedAt) }}
                  </div>
                </TableCell>
                <TableCell class="flex justify-end gap-2">
                  <Button variant='destructive' @click="removeFile(file.fileId)">{{ t('deleteButton') }}</Button>
                  <Button @click="getFile(file.fileId)">{{ t('downloadButton') }}</Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
        <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="uploadFileFormSchema">
          <Dialog>
            <DialogTrigger as-child>
              <Button class="m-4 mx-auto w-1/2">
                {{ t('uploadButton') }}
              </Button>
            </DialogTrigger>
            <DialogContent class="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>{{ t('createButton') }}</DialogTitle>
              </DialogHeader>
              <form id="createDialogForm" @submit="handleSubmit($event, addFile)">
                <StyledFormItem
                    inputName="file"
                    inputType="file"
                    inputPlaceholder="Wybierz plik"
                    labelFor="file"
                    labelPlaceholder="Wybierz plik"
                    errorMessageName="file"
                />
                <DialogFooter>
                  <DialogTrigger as-child>
                    <Button type="submit" form="createDialogForm">
                      {{ t('saveChanges') }}
                    </Button>
                  </DialogTrigger>
                </DialogFooter>
              </form>
            </DialogContent>
          </Dialog>
        </Form>
      </Card>
    </div>
  </div>
</template>