<script setup lang="ts">
import { fetchMyAppointments } from '@/lib/axios';
import { useI18n } from "vue-i18n";
import { onMounted, ref, watch } from "vue";
import type {Appointment, DoctorAppointments} from '@/lib/types';
import router from '@/router';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import store from "@/store";
import {TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";

const { t } = useI18n();

const role = store.getters.getRole;

const appointments = ref<Appointment[]>([]);
const doctorAppointments = ref<DoctorAppointments[]>([]);

type CalendarEvent = {
  id: string;
  title: string;
  start: string;
  end: string;
};
const calendarEvents = ref<CalendarEvent[]>([]);

onMounted(async () => {
  const response = await fetchMyAppointments();
  if (response.status === 200) {
    if(role === 'DOCTOR') {
      doctorAppointments.value = response.data;
      calendarEvents.value = doctorAppointments.value.map(appointment => ({
        id: appointment.appointmentId.toString(),
        title: appointment.patientInfo,
        start: new Date(`${appointment.appointmentDate}T${appointment.appointmentStartTime}`).toISOString(),
        end: new Date(`${appointment.appointmentDate}T${appointment.appointmentEndTime}`).toISOString()
      }));
    } else {
      appointments.value = response.data;
    }
  } else {
    console.error('Error while fetching appointments');
  }
});

const showMore = (appointmentId: number) => {
  const url = router.resolve({ name: 'appointment', params: { appointmentId: appointmentId } }).href;
  window.open(url, '_blank');
}

const calendarOptions = ref({
  plugins: [
    dayGridPlugin,
    timeGridPlugin,
    interactionPlugin
  ],
  headerToolbar: {
    left: 'prev,next today',
    center: 'title',
    right: 'dayGridMonth,timeGridWeek,timeGridDay'
  },
  initialView: 'timeGridWeek',
  editable: true,
  selectable: true,
  dayMaxEvents: true,
  events: calendarEvents.value,
  eventClick: (info: any) => {
    showMore(parseInt(info.event.id));
  },
  allDaySlot: false,
  slotLabelFormat: {
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  },
  eventTimeFormat: {
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  },
  dayHeaderFormat: {
    weekday: 'long'
  },
  firstDay: 1,
  locale: t('locale'),
});

watch(calendarEvents, (newEvents) => {
  calendarOptions.value.events = newEvents;
});
</script>

<template>
  <div class='calendar-container' v-if="role === 'DOCTOR'">
    <FullCalendar :options="calendarOptions" />
  </div>
  <div v-if="role=='PATIENT'">
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
  </div>
</template>

<style lang='css'>
.calendar-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 3em;
}
</style>