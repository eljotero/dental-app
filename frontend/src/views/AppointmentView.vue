<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">{{ t('appointmentDetails') }}</h1>
    <Card v-if="appointment" class="mb-4 shadow-lg rounded-lg overflow-hidden">
      <CardHeader class="bg-blue-500 text-white p-4">
        <h2 class="text-xl font-semibold">{{ t('appointmentDate') }}: {{ appointment.appointmentDate }}</h2>
      </CardHeader>
      <CardContent class="p-4">
        <Form @submit="onSubmit" :validation-schema="formSchema" ref="appointment" class="form-container">
          <FormFieldComponent name="appointmentStartTime" :label="t('appointmentStartTime')"/>
          <FormFieldComponent name="appointmentEndTime" :label="t('appointmentEndTime')"/>
          <FormFieldComponent name="doctorName" :label="t('doctorName')"/>
          <FormFieldComponent name="doctorLastName" :label="t('doctorLastName')"/>
          <FormFieldComponent name="doctorPhoneNumber" :label="t('doctorPhoneNumber')"/>
          <FormFieldComponent name="description" :label="t('description')" v-if="role === 'DOCTOR'" />
          <p v-else>{{ appointment.description }}</p>
          <p class="mb-2"><strong>{{ t('canceled') }}:</strong> {{ appointment.cancelled ? t('yes') : t('no') }}</p>
          <p class="mb-2"><strong>{{ t('paid') }}:</strong> {{ appointment.paid ? t('yes') : t('no') }}</p>
          <p class="mb-2"><strong>{{ t('confirmed') }}:</strong> {{ appointment.confirmed ? t('yes') : t('no') }}</p>
          <div v-if="!appointment.confirmed && !appointment.cancelled">
            <Button @click="confirm(Number(appointmentId))" class="mt-4">{{ t('appointmentConfirmButton') }}</Button>
          </div>
          <div v-if="!appointment.cancelled">
            <Button @click="cancel(Number(appointmentId))" class="mt-4" variant='destructive'>{{ t('appointmentCancelButton') }}</Button>
          </div>
          <div v-if="appointment.prescriptions">
            <h3 class="text-lg font-semibold mt-4">{{ t('prescriptions') }}</h3>
            <ul>
              <li v-for="prescription in appointment.prescriptions" :key="prescription.medicineName" class="mb-2">
                <strong>{{ t('medicineName') }}:</strong> {{ prescription.medicineName }}<br>
                <strong>{{ t('dosage') }}:</strong> {{ prescription.dosage }}
              </li>
            </ul>
          </div>
          <div v-if="appointment.referrals">
            <h3 class="text-lg font-semibold mt-4">{{ t('referrals') }}</h3>
            <ul>
              <li v-for="referral in appointment.referrals" :key="referral.procedureName" class="mb-2">
                <strong>{{ t('procedureName') }}:</strong> {{ referral.procedureName }}<br>
                <strong>{{ t('procedureDescription') }}:</strong> {{ referral.procedureDescription }}<br>
                <strong>{{ t('doctorName') }}:</strong> {{ referral.doctorName }}<br>
                <strong>{{ t('clinicName') }}:</strong> {{ referral.clinicName }}<br>
                <strong>{{ t('clinicAddress') }}:</strong> {{ referral.clinicAddress }}
              </li>
            </ul>
          </div>
        </Form>
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import {fetchAppointment, confirmAppointment, cancelAppointment} from '@/lib/axios';
import type {AppointmentDetails} from '@/lib/types';
import {onMounted, ref} from "vue";
import {Card, CardHeader, CardContent} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {useI18n} from "vue-i18n";
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import store from '@/store';
import {Form} from "@/components/ui/form";
import FormFieldComponent from '@/components/FormFieldComponent.vue';
import {toTypedSchema} from "@vee-validate/zod";
import {z} from "zod";

const {t} = useI18n();

const props = defineProps<{
  appointmentId: string;
}>();

const appointment = ref<AppointmentDetails>({} as AppointmentDetails);
const originalData = {} as AppointmentDetails;
const role = store.getters.getRole;

const formSchema = toTypedSchema(z.object({
  appointmentStartTime: z.string().nonempty(t('appointmentStartTimeError')),
  appointmentEndTime: z.string().nonempty(t('appointmentEndTimeError')),
  appointmentDate: z.string().nonempty(t('appointmentDateError')),
  doctorName: z.string().nonempty(t('doctorNameError')),
  doctorLastName: z.string().nonempty(t('doctorLastNameError')),
  doctorPhoneNumber: z.string().nonempty(t('doctorPhoneNumberError')),
  description: z.string().nonempty(t('descriptionError')),
  cancelled: z.boolean(),
  paid: z.boolean(),
  confirmed: z.boolean(),
  prescriptions: z.array(z.object({
    medicineName: z.string().nonempty(t('medicineNameError')),
    dosage: z.string().nonempty(t('dosageError')),
  })),
  referrals: z.array(z.object({
    procedureName: z.string().nonempty(t('procedureNameError')),
    procedureDescription: z.string().nonempty(t('procedureDescriptionError')),
    doctorName: z.string().nonempty(t('doctorNameError')),
    clinicName: z.string().nonempty(t('clinicNameError')),
    clinicAddress: z.string().nonempty(t('clinicAddressError')),
  })),
}));

onMounted(async () => {
  const response = await fetchAppointment(Number(props.appointmentId));
  if(response.status === 200) {
    console.log(response.data);
    appointment.value.setValues({
      appointmentDate: response.data.appointmentDate,
      appointmentStartTime: response.data.appointmentStartTime,
      appointmentEndTime: response.data.appointmentEndTime,
      doctorName: response.data.doctorName,
      doctorLastName: response.data.doctorLastName,
      doctorPhoneNumber: response.data.doctorPhoneNumber,
      description: response.data.description,
      cancelled: response.data.cancelled,
      paid: response.data.paid,
      confirmed: response.data.confirmed,
      prescriptions: response.data.prescriptions,
      referrals: response.data.referrals
    });
    console.log(appointment.value.prescriptions);
    console.log(appointment.value.confirmed);
  } else {
    console.error('Error while fetching appointment');
  }
});

const confirm = async (id: number) => {
  const response = await confirmAppointment(id);
  if(response.status === 200) {
    toast.success(t('appointmentConfirmed'), {
      autoClose: 2000,
    });
    appointment.value!.confirmed = true;
  } else {
    toast.error(t('appointmentConfirmError'), {
      autoClose: 2000,
    });
  }
}

const cancel = async(id: number) => {
  const response = await cancelAppointment(id);
  if(response.status === 200) {
    toast.success(t('appointmentCanceled'), {
      autoClose: 2000,
    });
    appointment.value!.cancelled = true;
  } else {
    toast.error(t('appointmentCancelError'), {
      autoClose: 2000,
    });
  }
}

const onSubmit = async (values: any) => {
  console.log(values);
}
</script>