<script setup lang="ts">
import {fetchAppointment, confirmAppointment, cancelAppointment} from '@/lib/axios';
import type {AppointmentDetails} from '@/lib/types';
import {onMounted, ref} from "vue";
import {Card, CardHeader, CardContent} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {useI18n} from "vue-i18n";
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const {t} = useI18n();

const props = defineProps<{
  appointmentId: string;
}>();

const appointment = ref<AppointmentDetails | null>(null);

onMounted(async () => {
  const response = await fetchAppointment(Number(props.appointmentId));
  console.log(response);
  if(response.status === 200) {
    appointment.value = response.data;
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
</script>

<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">{{ t('appointmentDetails') }}</h1>
    <Card v-if="appointment" class="mb-4 shadow-lg rounded-lg overflow-hidden">
      <CardHeader class="bg-blue-500 text-white p-4">
        <h2 class="text-xl font-semibold">{{ t('appointmentDate') }}: {{ appointment.appointmentDate }}</h2>
      </CardHeader>
      <CardContent class="p-4">
        <p class="mb-2"><strong>{{ t('appointmentStartTime') }}:</strong> {{ appointment.appointmentStartTime }}</p>
        <p class="mb-2"><strong>{{ t('appointmentEndTime') }}:</strong> {{ appointment.appointmentEndTime }}</p>
        <p class="mb-2"><strong>{{ t('doctorName') }}:</strong> {{ appointment.doctorName }}</p>
        <p class="mb-2"><strong>{{ t('doctorLastName') }}:</strong> {{ appointment.doctorLastName }}</p>
        <p class="mb-2"><strong>{{ t('doctorPhoneNumber') }}:</strong> {{ appointment.doctorPhoneNumber }}</p>
        <p class="mb-2"><strong>{{ t('description') }}:</strong> {{ appointment.description }}</p>
        <p class="mb-2"><strong>{{ t('canceled') }}:</strong> {{ appointment.cancelled ? t('yes') : t('no') }}</p>
        <p class="mb-2"><strong>{{ t('paid') }}:</strong> {{ appointment.paid ? t('yes') : t('no') }}</p>
        <p class="mb-2"><strong>{{ t('confirmed') }}:</strong> {{ appointment.confirmed ? t('yes') : t('no') }}</p>
        <div v-if="appointment.confirmed == false">
          <Button @click="confirm(Number(appointmentId))" class="mt-4">{{ t('appointmentConfirmButton') }}</Button>
        </div>
        <div v-if="appointment.cancelled == false">
          <Button @click="cancel(Number(appointmentId))" class="mt-4">{{ t('appointmentCancelButton') }}</Button>
        </div>
        <div v-if="appointment.prescriptions.length">
          <h3 class="text-lg font-semibold mt-4">{{ t('prescriptions') }}</h3>
          <ul>
            <li v-for="prescription in appointment.prescriptions" :key="prescription.medicineName" class="mb-2">
              <strong>{{ t('medicineName') }}:</strong> {{ prescription.medicineName }}<br>
              <strong>{{ t('dosage') }}:</strong> {{ prescription.dosage }}
            </li>
          </ul>
        </div>
        <div v-if="appointment.referrals.length">
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
      </CardContent>
    </Card>
  </div>
</template>