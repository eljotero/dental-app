<template>
  <Card class="card">
    <CardContent>
      <CardTitle>{{ t('editTreatment') }}</CardTitle>
      <Form @submit="onSubmit" ref="treatmentRef" :validation-schema="formSchema" class="form-container">
        <FormFieldComponent name="treatmentName" :label="t('treatmentName')" :placeholder="t('treatmentName')"/>
        <FormFieldComponent name="treatmentDescription" :label="t('treatmentDescription')"
                            :placeholder="t('treatmentDescription')"/>
        <FormFieldComponent name="treatmentPrice" type="number" :label="t('treatmentPrice')"
                            :placeholder="t('treatmentPrice')"/>
        <Button type="submit">{{ t('saveChanges') }}</Button>
      </Form>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {toTypedSchema} from "@vee-validate/zod";
import {z} from "zod";
import {Card, CardContent, CardTitle} from "@/components/ui/card";
import {Form} from "@/components/ui/form";
import FormFieldComponent from "@/components/FormFieldComponent.vue";
import {useI18n} from "vue-i18n";
import {getTreatment, updateTreatment} from "@/lib/axios";
import type {Treatment, UpdateTreatment} from "@/lib/types";
import {Button} from "@/components/ui/button";
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css'

const route = useRoute();
const treatmentId = Number(ref(route.params.treatmentId).value);
const {t} = useI18n();

const treatmentRef = ref<Treatment>({} as Treatment);
const originalData = {} as Treatment;

const formSchema = toTypedSchema(z.object({
  treatmentName: z.string().nonempty(t('treatmentNameError')),
  treatmentDescription: z.string().nonempty(t('treatmentDescriptionError')),
  treatmentPrice: z.number().positive(t('treatmentPriceErrorNegative')),
}));

const onSubmit = async (values: any) => {
  const dto = {} as UpdateTreatment;
  for (const key in values) {
    if (values[key] !== originalData[key]) {
      dto[key] = values[key];
    }
  }
  if (Object.keys(dto).length === 0) {
    toast.info(t('noDataToUpdate'), {
      autoClose: 2000,
    });
    return;
  }
  const response = await updateTreatment(treatmentId, dto);
  if (response.status === 200) {
    toast.success(t('updateDataSuccess'), {
      autoClose: 2000,
    });
  } else {
    toast.error(t('updateDataError'), {
      autoClose: 3000,
    });
  }
};

onMounted(async () => {
  const response = await getTreatment(treatmentId);
  if (response.status === 200 && response.data) {
    console.log(response.data)
    treatmentRef.value.setValues({
      treatmentId: Number(response.data.treatmentId),
      treatmentName: response.data.treatmentName,
      treatmentDescription: response.data.treatmentDescription,
      treatmentPrice: response.data.treatmentPrice,
    });
    originalData.treatmentId = treatmentId;
    originalData.treatmentName = response.data.name;
    originalData.treatmentDescription = response.data.description;
    originalData.treatmentPrice = response.data.price;
  }
});
</script>