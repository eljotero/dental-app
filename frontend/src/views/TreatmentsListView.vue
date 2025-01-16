<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">{{ t('priceList') }}</h1>
    <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createFormSchema">
      <Dialog>
        <DialogTrigger as-child>
          <Button variant="outline">
            {{ t('createTreatment') }}
          </Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>{{ t('createTreatment') }}</DialogTitle>
          </DialogHeader>
          <form id="createDialogForm" @submit="handleSubmit($event, onSubmitCreate)">
            <FormFieldComponent name="treatmentName" :label="t('treatmentName')" :placeholder="t('treatmentName')"/>
            <FormFieldComponent name="treatmentDescription" :label="t('treatmentDescription')"
                                :placeholder="t('treatmentDescription')"/>
            <FormFieldComponent name="treatmentPrice" type="number" :label="t('treatmentPrice')"
                                :placeholder="t('treatmentPrice')"/>
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
    <Table class="min-w-full bg-white border border-gray-200">
      <TableHeader>
        <TableRow class="bg-gray-100">
          <TableHead class="py-2 px-4 border-b">{{ t('treatmentName') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('treatmentDescription') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('treatmentPrice') }}</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow v-for="treatment in treatments" :key="treatment.treatmentId" class="hover:bg-gray-50">
          <TableCell class="py-2 px-4 border-b">{{ treatment.treatmentName }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ treatment.treatmentDescription }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ treatment.treatmentPrice }}</TableCell>
          <Button variant="destructive" v-if="role === 'DOCTOR'" @click="removeTreatment(treatment.treatmentId)">
            {{ t('deleteTreatment') }}
          </Button>
        </TableRow>
      </TableBody>
    </Table>
  </div>
</template>

<script setup lang="ts">
import {createTreatment, deleteTreatment, getTreatments} from '@/lib/axios';
import type {CreateTreatment, Treatment} from '@/lib/types';
import {onMounted, ref} from 'vue';
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from '@/components/ui/table/index.ts';
import {Button} from '@/components/ui/button';
import {useI18n} from 'vue-i18n';
import store from '../store/index.ts';
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import {toTypedSchema} from "@vee-validate/zod";
import * as z from 'zod';
import {Form} from '@/components/ui/form'
import FormFieldComponent from "@/components/FormFieldComponent.vue";
import {Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger,} from '@/components/ui/dialog';

const treatments = ref<Treatment[]>([]);
const selectedTreatmentId = ref<number | null>(null);

const {t} = useI18n();

const role = store.getters.getRole;

onMounted(async () => {
  const response = await getTreatments();
  if (response.status === 200) {
    treatments.value = response.data;
  } else {
    console.error('Error while fetching treatments');
  }
});

const createFormSchema = toTypedSchema(z.object({
  treatmentName: z.string().nonempty(t('treatmentNameError')),
  treatmentDescription: z.string().nonempty(t('treatmentDescriptionError')),
  treatmentPrice: z.number().positive(t('treatmentPriceErrorNegative')),
}));

const onSubmitCreate = async (values: any) => {
  const dto = values as CreateTreatment;
  try {
    const response = await createTreatment(dto);
    if (response.status === 201) {
      toast.success(t('treatmentCreated'), {
        autoClose: 2000,
      });
      setTimeout(() => {
        window.location.reload();
      }, 2000);
    }
  } catch (error: any) {
    const errorMessage = error.response.data.message;
    const errorMessages = Array.isArray(errorMessage) ? errorMessage.join(', ') : errorMessage;
    toast.error(errorMessages, {
      autoClose: 3000,
    });

  }
};


const removeTreatment = async (treatmentId: number) => {
  const response = await deleteTreatment(treatmentId);
  if (response.status === 200) {
    toast.success(t('treatmentDeleted'), {
      autoClose: 2000,
    });
  } else {
    toast.error(t('treatmentDeleteError'), {
      autoClose: 3000,
    });
  }
  setTimeout(() => {
    window.location.reload();
  }, 2000);
};
</script>