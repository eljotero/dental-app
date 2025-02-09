<script setup lang="ts">

import type {Availability, CalendarEvent, CreateAvailability} from "@/lib/types";
import {onMounted, ref, watch} from "vue";
import {createAvailability, deleteAvailability, getAvailabilities} from "@/lib/axios.ts";
import type {CalendarOptions} from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import store from "@/store";
import FullCalendar from '@fullcalendar/vue3';
import {useI18n} from "vue-i18n";
import {handleRequest} from "@/lib/functions.ts";
import 'vue3-toastify/dist/index.css';
import {toast} from "vue3-toastify";
import {Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger} from "@/components/ui/dialog";
import {Button} from "@/components/ui/button";
import StyledFormItem from "@/components/StyledFormItem.vue";
import {Form} from "@/components/ui/form";
import {toTypedSchema} from "@vee-validate/zod";
import * as z from "zod";

const {t} = useI18n();

const availabilities = ref<Availability[]>([]);
const calendarEvents = ref<CalendarEvent[]>([]);
const showModal = ref(false);
const showConfirmModal = ref(false);
const formData = ref({
  date: '',
  startTime: '',
  endTime: ''
});
const availabilityToDelete = ref<string | null>(null);

onMounted(async () => {
  try {
    const response = await getAvailabilities();
    if (response.status === 200) {
      availabilities.value = response.data;
    }
    calendarEvents.value = availabilities.value.map(availability => ({
      id: availability.availabilityId.toString(),
      title: '',
      start: new Date(`${availability.date}T${availability.startTime}`).toISOString(),
      end: new Date(`${availability.date}T${availability.endTime}`).toISOString()
    }));
  } catch (error) {
    console.error('Error while fetching availabilities');
  }
});

const addAvailability = async (values : any) => {
  const dto = {
    date: values.date,
    startTime: values.startTime,
    endTime: values.endTime
  } as CreateAvailability;
  await handleRequest(
      (dto) => createAvailability(dto),
      dto,
      t('availabilityCreated'),
      t('availabilityCreateError'),
      t,
      201
  );
  showModal.value = false;
}

const confirmRemoveAvailability = async () => {
  if (availabilityToDelete.value) {
    try {
      const response = await deleteAvailability(Number(availabilityToDelete.value));
      if (response.status === 200) {
        toast.success(t('availabilityDeleted'), {
          autoClose: 2000,
        });
        setTimeout(() => {
          location.reload();
        }, 2000);
      } else {
        toast.error(t('availabilityDeleteError'), {
          autoClose: 2000,
        });
      }
    } catch (error) {
      console.error('Error while deleting availability');
    }
    showConfirmModal.value = false;
  }
}

const createFormSchema = toTypedSchema(z.object({
  date: z.string().nonempty(t('availabilityDateError')),
  startTime: z.string().nonempty(t('availabilityStartTimeError')),
  endTime: z.string().nonempty(t('availabilityEndTimeError')),
}));

const calendarOptions = ref<CalendarOptions>({
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
  locale: 'pl',
  select: function(info) {
    formData.value.date = info.startStr.split('T')[0];
    formData.value.startTime = info.startStr.split('T')[1].substring(0, 5);
    formData.value.endTime = info.endStr.split('T')[1].substring(0, 5);
    showModal.value = true;
  },
  eventClick: function(info) {
    availabilityToDelete.value = info.event._def.publicId;
    showConfirmModal.value = true;
  }
});

watch(calendarEvents, (newEvents) => {
  calendarOptions.value.events = newEvents;
});

watch(() => {
  return store.getters.getLanguage;
}, (newLang) => {
  if(newLang === 'en') {
    calendarOptions.value.locale = 'en';
  } else {
    calendarOptions.value.locale = 'pl';
  }
});

</script>

<template>
  <Form v-slot="{ handleSubmit }" as="" :validation-schema="createFormSchema" keep-values>
    <Dialog v-model:open="showModal">
      <DialogTrigger as-child>
        <Button variant="outline" class="mt-4 flex-1 w-full">
          {{ t('createButton') }}
        </Button>
      </DialogTrigger>
      <DialogContent class="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>{{ t('editButton') }}</DialogTitle>
        </DialogHeader>
        <form id="editDialogForm" @submit="handleSubmit($event, addAvailability)">
          <StyledFormItem inputName="date" inputType="date"
                          :inputPlaceholder="t('availabilityDate')" labelFor="date"
                          :labelPlaceholder="t('availabilityDate')" errorMessageName="date"
                          v-model="formData.date"/>
          <StyledFormItem inputName="startTime" inputType="time"
                          :inputPlaceholder="t('availabilityStartTime')" labelFor="startTime"
                          :labelPlaceholder="t('availabilityStartTime')" errorMessageName="startTime"
                          v-model="formData.startTime"/>
          <StyledFormItem inputName="endTime" inputType="time"
                          :inputPlaceholder="t('availabilityEndTime')" labelFor="endTime"
                          :labelPlaceholder="t('availabilityEndTime')" errorMessageName="endTime"
                          v-model="formData.endTime"/>
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

  <Dialog v-model:open="showConfirmModal">
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>{{ t('confirmDeleteTitle') }}</DialogTitle>
      </DialogHeader>
      <DialogFooter>
        <Button variant="outline" @click="showConfirmModal.value = false">
          {{ t('cancelButton') }}
        </Button>
        <Button variant="destructive" @click="confirmRemoveAvailability">
          {{ t('deleteButton') }}
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>

  <div class='calendar-container'>
    <FullCalendar :options="calendarOptions"/>
  </div>
</template>

<style lang='css'>
.calendar-container {
  max-width: 1100px;
  max-height: 500px;
  margin: 0 auto;
  padding: 3em;
}
</style>