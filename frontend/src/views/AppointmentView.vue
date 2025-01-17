<script setup lang="ts">
import {cancelAppointment, confirmAppointment, fetchAppointment} from '@/lib/axios';
import type {AppointmentDetails} from '@/lib/types';
import {onMounted, ref} from 'vue';
import {Card, CardContent, CardHeader} from '@/components/ui/card';
import {Button} from '@/components/ui/button';
import {useI18n} from 'vue-i18n';
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import store from '@/store';
import {Table, TableBody, TableCell, TableRow} from "@/components/ui/table";

const {t} = useI18n();

const props = defineProps<{
  appointmentId: string;
}>();

const appointment = ref<AppointmentDetails>({} as AppointmentDetails);
const role = store.getters.getRole;

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
</script>

<template>
  <div class="container mx-auto p-4 mt-14">
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <Card v-if="appointment" class="shadow-lg rounded-lg overflow-hidden">
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
              <TableRow>
                <TableCell class="font-bold">{{ t('doctorName') }}</TableCell>
                <TableCell>{{ appointment.doctorName }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('doctorLastName') }}</TableCell>
                <TableCell>{{ appointment.doctorLastName }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('doctorPhoneNumber') }}</TableCell>
                <TableCell>{{ appointment.doctorPhoneNumber }}</TableCell>
              </TableRow>
              <TableRow v-if="role === 'DOCTOR'">
                <TableCell class="font-bold">{{ t('description') }}</TableCell>
                <TableCell>{{ appointment.description }}</TableCell>
              </TableRow>
              <TableRow v-else>
                <TableCell colspan="2">{{ appointment.description }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('canceled') }}</TableCell>
                <TableCell>{{ appointment.cancelled ? t('yes') : t('no') }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('paid') }}</TableCell>
                <TableCell>{{ appointment.paid ? t('yes') : t('no') }}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell class="font-bold">{{ t('confirmed') }}</TableCell>
                <TableCell>{{ appointment.confirmed ? t('yes') : t('no') }}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
          <div v-if="!appointment.confirmed && !appointment.cancelled" class="flex gap-4 justify-center">
            <Button @click="confirm(Number(appointmentId))" class="mt-4">{{ t('appointmentConfirmButton') }}</Button>
            <Button @click="cancel(Number(appointmentId))" class="mt-4" variant='destructive'>
              {{ t('appointmentCancelButton') }}
            </Button>
            <Button class="mt-4" variant='edit'>
              {{ t('appointmentPayButton') }}
            </Button>
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
                <TableCell class="flex justify-end gap-2">
                  <Button variant='edit'>{{ t('editButton') }}</Button>
                  <Button variant='destructive'>{{ t('deleteButton') }}</Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
          <div class="flex justify-center mt-auto">
            <Button variant='default'>{{ t('createButton') }}</Button>
          </div>
        </CardContent>
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
                <TableCell class="flex justify-end gap-2">
                  <Button variant='edit'>{{ t('editButton') }}</Button>
                  <Button variant='destructive'>{{ t('deleteButton') }}</Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
          <div class="flex justify-center mt-auto">
            <Button variant='default'>{{ t('createButton') }}</Button>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>