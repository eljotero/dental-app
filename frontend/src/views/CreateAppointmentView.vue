<script setup lang="ts">
import type {CreateAppointment, GetDoctorDto, Option, TimeSlots} from "@/lib/types.ts";
import {onMounted, ref} from "vue";
import {createAppointment, getDoctorsAvailability, getDoctorsForAppointment} from "@/lib/axios.ts";
import {toTypedSchema} from '@vee-validate/zod';
import {z} from 'zod';
import FormFieldComponent from "@/components/FormFieldComponent.vue";
import SelectFieldComponent from "@/components/SelectFieldComponent.vue";
import {Form} from "@/components/ui/form";
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import {useI18n} from "vue-i18n";

const {t} = useI18n();

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
const isSlotsVisible = ref(false);
const selectedDoctorId = ref("");
const selectedDate = ref("");

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
});

const fetchTimeSlots = async (values: any) => {
  if (values.doctorId && values.date) {
    const response = await getDoctorsAvailability(values.doctorId, values.date);
    console.log(response);
    if (response.status === 200) {
      slots.value = response.data;
      selectedDoctorId.value = values.doctorId;
      selectedDate.value = values.date;
      if (Object.keys(response.data).length > 0) {
        isSlotsVisible.value = true;
      } else {
        toast.error(t('noSlotsError'), {
          autoClose: 3000,
        });
      }
    } else {
      console.error("Error while fetching time slots");
    }
  }
};

const submitFinalForm = async (values: any) => {
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
  <div>
    <h1 class="text-2xl font-bold mb-4">{{ t('createAppointmentHeader') }}</h1>
    <Form :schema="formSchema" @submit="fetchTimeSlots">
      <SelectFieldComponent
          name="doctorId"
          :label="t('selectDoctor')"
          :placeholder="t('selectDoctor')"
          :options="doctorOptions"
      />
      <FormFieldComponent
          name="date"
          type="date"
          :label="t('selectDate')"
      />
      <Button type="submit">{{ t('searchButton') }}</Button>
    </Form>

    <div v-if="isSlotsVisible" class="mt-4">
      <h2 class="text-xl font-bold">Available Time Slots</h2>
      <Form :schema="finalSchema" @submit="submitFinalForm">
        <SelectFieldComponent
            name="time"
            :label="t('selectTime')"
            :placeholder="t('selectTime')"
            :options="Object.keys(slots).map(time => ({ value: time, label: time }))"
        />
        <input type="hidden" name="doctorId" v-model="selectedDoctorId"/>
        <input type="hidden" name="date" v-model="selectedDate"/>
        <Button type="submit">{{ t('confirmAppointmentCreateButton') }}</Button>
      </Form>
    </div>
  </div>
</template>
