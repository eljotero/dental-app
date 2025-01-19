<script setup lang="ts">
import { Card } from "@/components/ui/card";
import type { CreateAppointment, GetDoctorDto, Option, TimeSlots } from "@/lib/types.ts";
import { onMounted, ref } from "vue";
import { createAppointment, getDoctorsAvailability, getDoctorsForAppointment } from "@/lib/axios.ts";
import { toTypedSchema } from '@vee-validate/zod';
import { z } from 'zod';
import { Form } from "@/components/ui/form";
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import { useI18n } from "vue-i18n";
import {Button} from "@/components/ui/button";
import StyledSelectFormItem from "@/components/StyledSelectFormItem.vue";

const { t } = useI18n();

const formSchema = toTypedSchema(z.object({
  doctorId: z.string(),
  date: z.string()
}));

const finalSchema = toTypedSchema(z.object({
  doctorId: z.string(),
  date: z.string(),
  time: z.string()
}));

const doctors = ref<GetDoctorDto[]>([]);
const doctorOptions = ref<Option[]>([]);
const slots = ref<TimeSlots>({});
const selectedDoctorId = ref("");
const selectedDate = ref("");
const selectedTime = ref("");
const startDate = ref(new Date());
const endDate = ref(new Date());

onMounted(async () => {
  const response = await getDoctorsForAppointment();
  if (response.status === 200) {
    doctors.value = response.data;
    doctorOptions.value = doctors.value.map(doc => ({
      value: doc.doctorId.toString(),
      label: `${doc.firstName} ${doc.lastName}`
    }));
  } else {
    console.error("Error while fetching doctors for appointment");
  }
  endDate.value.setDate(endDate.value.getDate() + 5);
});

const formatDate = (date: Date): string => {
  return date.toISOString().split('T')[0];
};


const updateDateRange = (days: number) => {
  startDate.value.setDate(startDate.value.getDate() + days);
  endDate.value.setDate(endDate.value.getDate() + days);
  fetchTimeSlots({ doctorId: selectedDoctorId.value });
};

const fetchTimeSlots = async (values: any) => {
  if (values.doctorId) {
    const formattedStartDate = formatDate(startDate.value);
    const formattedEndDate = formatDate(endDate.value);
    const response = await getDoctorsAvailability(values.doctorId, formattedStartDate, formattedEndDate);
    if (response.status === 200) {
      slots.value = response.data;
      selectedDoctorId.value = values.doctorId;
      console.log(slots.value);
    } else {
      console.error("Error while fetching time slots");
    }
  }
};

const selectTime = (time: number, date: string) => {
  selectedTime.value = time.toString();
  selectedDate.value = date.toString();
};

const submitFinalForm = async (values: any) => {
  console.log(selectedTime);
  const appointmentStartTime = values.time;
  const appointmentEndTime = addOneHour(appointmentStartTime);

  const appointment: CreateAppointment = {
    doctorId: Number(selectedDoctorId.value),
    appointmentDate: selectedDate.value,
    appointmentStartTime: appointmentStartTime,
    appointmentEndTime: appointmentEndTime,
  };
  const response = await createAppointment(appointment);

  if (response.status === 201) {
    toast.success(t('appointmentCreated'), {
      autoClose: 2000,
    });
  } else {
    toast.error(t('appointmentCreateError'), {
      autoClose: 3000,
    });
  }
};

const addOneHour = (time: string): string => {
  const [hours, minutes] = time.split(":").map(Number);
  return `${hours + 1}:${minutes.toString().padStart(2, "0")}`;
};
</script>

<template>
  <div class="container mx-auto p-4 mt-16 h-full">
    <Card class="flex h-full justify-center items-center">
      <div class="w-1/2 pr-4 flex-1 flex flex-col">
        <h1 class="text-2xl font-bold mb-4 text-center">{{ t('createAppointmentHeader') }}</h1>
        <Form :schema="formSchema" @submit="fetchTimeSlots" class="space-y-4">
          <StyledSelectFormItem :options="doctorOptions" inputName="doctorId" labelFor="doctorId" :labelPlaceholder="t('selectDoctor')" :label="t('selectDoctor')" errorMessageName="doctorId" />
          <div class="flex justify-center">
            <Button type="submit" class="mt-4">{{ t('searchButton') }}</Button>
          </div>
        </Form>
      </div>
      <div class="w-1/2 pl-4 flex-1 flex flex-col">
        <div class="flex-1">
          <h2 class="text-xl font-bold text-center mb-4">{{ t('availableTimeSlots') }}</h2>
          <div v-for="(times, date) in slots" :key="date" class="mb-4">
            <h3 class="text-lg font-semibold">{{ date }}</h3>
            <div class="grid grid-rows-3 gap-4 content-center">
              <Button
                  v-for="(endTime, startTime) in times"
                  :key="startTime"
                  @click="selectTime(startTime, date.toString())"
                  class="p-4 bg-blue-500 text-white rounded-lg hover:bg-blue-700 w-1/2 mx-auto"
              >
                {{ startTime }} - {{ endTime }}
              </Button>
            </div>
          </div>
          <Form :schema="finalSchema" @submit="submitFinalForm" class="space-y-4 mt-4">
            <input type="hidden" name="time" v-model="selectedTime" />
            <input type="hidden" name="doctorId" v-model="selectedDoctorId" />
            <input type="hidden" name="date" v-model="selectedDate" />
            <div class="flex justify-center">
              <Button type="submit" class="mt-4">{{ t('confirmAppointmentCreateButton') }}</Button>
            </div>
          </Form>
          <div class="flex justify-between mt-4">
            <Button @click="updateDateRange(-5)" class="bg-gray-500 text-white rounded-lg hover:bg-gray-700">{{ t('previous5Days') }}</Button>
            <Button @click="updateDateRange(+5)" class="bg-gray-500 text-white rounded-lg hover:bg-gray-700">{{ t('next5Days') }}</Button>
          </div>
        </div>
      </div>
    </Card>
  </div>
</template>