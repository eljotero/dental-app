<script setup lang="ts">
import {createTreatment, deleteTreatment, getTreatments, updateTreatment} from '@/lib/axios';
import type {CreateTreatment, Treatment, UpdateTreatment} from '@/lib/types';
import {onMounted, ref} from 'vue';
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from '@/components/ui/table/index.ts';
import {Button} from '@/components/ui/button';
import {useI18n} from 'vue-i18n';
import store from '../store/index.ts';
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import {toTypedSchema} from "@vee-validate/zod";
import * as z from 'zod';
import {Form} from '@/components/ui/form';
import {Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger} from '@/components/ui/dialog';
import {handleRequest, handleSubmit} from '@/lib/functions';
import StyledFormItem from "@/components/StyledFormItem.vue";

const treatments = ref<Treatment[]>([]);
const selectedTreatmentId = ref<number>(0);
const editFormRef = ref({} as UpdateTreatment);
const originalData = {} as UpdateTreatment;

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

const editFormSchema = toTypedSchema(z.object({
  treatmentName: z.string().nonempty(t('treatmentNameError')),
  treatmentDescription: z.string().nonempty(t('treatmentDescriptionError')),
  treatmentPrice: z.number().positive(t('treatmentPriceErrorNegative')),
}));

const onSubmitCreate = async (values: any) => {
  const dto = values as CreateTreatment;
  await handleRequest(
      (dto) => createTreatment(dto),
      dto,
      t('treatmentCreated'),
      t('treatmentCreateError'),
      t,
      201
  );
};

const onSubmitEdit = async (values: any) => {
  await handleSubmit(values, originalData, updateTreatment, selectedTreatmentId.value, t('updateDataSuccess'), t('updateDataError'), t);
};

const setEditFormValues = (treatment: Treatment) => {
  editFormRef.value = {
    treatmentName: treatment.treatmentName,
    treatmentDescription: treatment.treatmentDescription,
    treatmentPrice: treatment.treatmentPrice,
  };
  selectedTreatmentId.value = treatment.treatmentId;
  originalData.treatmentName = treatment.treatmentName;
  originalData.treatmentDescription = treatment.treatmentDescription;
  originalData.treatmentPrice = treatment.treatmentPrice;
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

<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">{{ t('priceList') }}</h1>
    <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createFormSchema" v-if="role === 'DOCTOR'">
      <Dialog>
        <DialogTrigger as-child>
          <Button>
            {{ t('createButton') }}
          </Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>{{ t('createButton') }}</DialogTitle>
          </DialogHeader>
          <form id="createDialogForm" @submit="handleSubmit($event, onSubmitCreate)">
            <StyledFormItem inputName="treatmentName" inputType="text" :inputPlaceholder="t('treatmentName')" labelFor="treatmentName" :labelPlaceholder="t('treatmentName')" errorMessageName="treatmentName"/>
            <StyledFormItem inputName="treatmentDescription" inputType="text" :inputPlaceholder="t('treatmentDescription')" labelFor="treatmentDescription" :labelPlaceholder="t('treatmentDescription')" errorMessageName="treatmentDescription"/>
            <StyledFormItem inputName="treatmentPrice" inputType="number" :inputPlaceholder="t('treatmentPrice')" labelFor="treatmentPrice" :labelPlaceholder="t('treatmentPrice')" errorMessageName="treatmentPrice"/>
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
          <Form v-slot="{ handleSubmit }" as="" :validation-schema="editFormSchema" keep-values>
            <Dialog>
              <DialogTrigger as-child>
                <Button variant="outline" @click="setEditFormValues(treatment)" v-if="role === 'DOCTOR'">
                  {{ t('editButton') }}
                </Button>
              </DialogTrigger>
              <DialogContent class="sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle>{{ t('editButton') }}</DialogTitle>
                </DialogHeader>
                <form id="editDialogForm" @submit="handleSubmit($event, onSubmitEdit)">
                  <StyledFormItem inputName="treatmentName" inputType="text" :inputPlaceholder="t('treatmentName')" labelFor="treatmentName" :labelPlaceholder="t('treatmentName')" errorMessageName="treatmentName" :modelValue="editFormRef.treatmentName"/>
                  <StyledFormItem inputName="treatmentDescription" inputType="text" :inputPlaceholder="t('treatmentDescription')" labelFor="treatmentDescription" :labelPlaceholder="t('treatmentDescription')" errorMessageName="treatmentDescription" :modelValue="editFormRef.treatmentDescription"/>
                  <StyledFormItem inputName="treatmentPrice" inputType="number" :inputPlaceholder="t('treatmentPrice')" labelFor="treatmentPrice" :labelPlaceholder="t('treatmentPrice')" errorMessageName="treatmentPrice" :modelValue="editFormRef.treatmentPrice"/>
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
          <Button variant="destructive" v-if="role === 'DOCTOR'" @click="removeTreatment(treatment.treatmentId)">
            {{ t('deleteButton') }}
          </Button>
        </TableRow>
      </TableBody>
    </Table>
  </div>
</template>