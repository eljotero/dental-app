<template>
  <Card class="card">
    <CardContent>
      <CardTitle>{{ t('createTreatment') }}</CardTitle>
      <Form @submit="onSubmit" :validation-schema="formSchema" class="form-container">
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
import {Card, CardContent, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Form} from "@/components/ui/form";
import FormFieldComponent from "@/components/FormFieldComponent.vue";
import {toTypedSchema} from "@vee-validate/zod";
import {z} from "zod";
import {useI18n} from "vue-i18n";
import type {CreateTreatment} from "@/lib/types";
import {createTreatment} from "@/lib/axios";
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const {t} = useI18n();

const formSchema = toTypedSchema(z.object({
  treatmentName: z.string().nonempty(t('treatmentNameError')),
  treatmentDescription: z.string().nonempty(t('treatmentDescriptionError')),
  treatmentPrice: z.number().positive(t('treatmentPriceErrorNegative')),
}));

const onSubmit = async (values: any) => {
  const dto = values as CreateTreatment;
  try {
    const response = await createTreatment(dto);
    if (response.status === 201) {
      toast.success(t('treatmentCreated'), {
        autoClose: 2000,
      });
    }
  } catch (error: any) {
    const errorMessage = error.response.data.message;
    const errorMessages = Array.isArray(errorMessage) ? errorMessage.join(', ') : errorMessage;
    toast.error(errorMessages, {
      autoClose: 3000,
    });
  }
}

</script>