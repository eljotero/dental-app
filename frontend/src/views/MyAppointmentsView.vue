<script setup lang="ts">
import {fetchMyAppointments} from '@/lib/axios';
import {useI18n} from "vue-i18n";
import {onMounted, ref} from "vue";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import type {Appointment} from '@/lib/types';
import {Button} from "@/components/ui/button";
import router from '@/router';

const {t} = useI18n();

const appointments = ref<Appointment[]>([]);

onMounted(async () => {
  const response = await fetchMyAppointments();
  if(response.status === 200) {
    appointments.value = response.data;
  } else {
    console.error('Error while fetching appointments');
  }
});

const showMore = (appointmentId: number) => {
  const url = router.resolve({ name: 'appointment', params: { appointmentId: appointmentId } }).href;
  window.open(url, '_blank');
}

</script>

<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">{{ t('myAppointments') }}</h1>
    <Table class="min-w-full bg-white border border-gray-200">
      <TableHeader>
        <TableRow class="bg-gray-100">
          <TableHead class="py-2 px-4 border-b">{{ t('appointmentDate') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('appointmentStartTime') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('appointmentEndTime') }}</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow v-for="appointment in appointments" :key="appointment.appointmentId" class="hover:bg-gray-50">
          <TableCell class="py-2 px-4 border-b">{{ appointment.appointmentDate }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ appointment.appointmentStartTime }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ appointment.appointmentEndTime }}</TableCell>
          <TableCell class="py-2 px-4 border-b text-center">
            <Button @click="showMore(appointment.appointmentId)" class="py-2 px-4 w-1/2">{{ t('showMore') }}</Button>
          </TableCell>
        </TableRow>
      </TableBody>
    </Table>
  </div>
</template>