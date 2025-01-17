<script setup lang="ts">
import {Button} from '@/components/ui/button';
import {Form} from '@/components/ui/form';
import {toTypedSchema} from '@vee-validate/zod';
import {z} from 'zod';
import {useI18n} from "vue-i18n";
import {Card, CardContent, CardTitle} from "@/components/ui/card";
import type {RegisterDto} from '@/lib/types';
import {register} from '@/lib/axios';
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import router from "@/router";
import store from "@/store";
import {useCountries} from '@/lib/countries';
import StyledFormItem from "@/components/StyledFormItem.vue";
import StyledSelectFormItem from "@/components/StyledSelectFormItem.vue";

const {t} = useI18n();

const countries = useCountries();

const formSchema = toTypedSchema(z.object({
  firstName: z.string().nonempty(t('firstNameError')),
  lastName: z.string().nonempty(t('lastNameError')),
  email: z.string().email(t('emailError')),
  password: z.string().nonempty(t('passwordError')).min(8, t('passwordLengthError')),
  confirmPassword: z.string().nonempty(t('passwordConfirmError')).min(8, t('passwordLengthError')),
  sex: z.string().nonempty(t('sexError')),
  phoneNumber: z.string().nonempty(t('phoneNumberError')).regex(/^(\+\d{1,3})?(\d{3})?\d{3}\d{3}$/, {
    message: t('phoneNumberLengthError'),
  }),
  personalIdNumber: z.string().nonempty(t('personalIdNumberError')).regex(/^\d{11}$/, {
    message: t('personalIdNumberLengthError'),
  }),
  country: z.string().nonempty(t('countryError')),
  city: z.string().nonempty(t('cityError')),
  addressLine: z.string().nonempty(t('addressLineError')),
  zipCode: z.string().nonempty(t('zipCodeError')).regex(/^\d{5}$/, {
    message: t('zipCodeInvalid'),
  }),
  dateOfBirth: z.string().nonempty(t('dateOfBirthError')).refine(data => {
    const date = new Date(data);
    const now = new Date();
    return date < now;
  }, {
    message: t('dateOfBirthInvalidError'),
  }),
}).refine(data => data.password === data.confirmPassword, {
  message: t('passwordMatchError'), path: ["confirmPassword"]
}));

const onSubmit = async (values: any) => {
  const { confirmPassword, ...formValues } = values;
  const dto = formValues as RegisterDto;
  dto.language = store.getters.getLanguage;
  try {
    const response = await register(dto);
    if (response.status === 200) {
      toast.success(t('registerSuccess'), {
        autoClose: 2000,
      });
      setTimeout(() => {
        router.push({ name: 'home' });
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

</script>

<template>
  <Card class="card">
    <CardContent>
      <CardTitle>{{ t('register') }}</CardTitle>
      <Form @submit="onSubmit" :validation-schema="formSchema" class="form-container">
        <StyledFormItem inputName="firstName" inputType="text" :inputPlaceholder="t('firstNameLabel')" labelFor="firstName" :labelPlaceholder="t('firstNamePlaceholder')" :label="t('firstNamePlaceholder')" errorMessageName="firstName"/>
        <StyledFormItem inputName="lastName" inputType="text" :inputPlaceholder="t('lastNameLabel')" labelFor="lastName" :labelPlaceholder="t('lastNamePlaceholder')" :label="t('lastNamePlaceholder')" errorMessageName="lastName"/>
        <StyledFormItem inputName="email" inputType="email" :inputPlaceholder="t('emailLabel')" labelFor="email" :labelPlaceholder="t('emailPlaceholder')" :label="t('emailPlaceholder')" errorMessageName="email"/>
        <StyledFormItem inputName="password" inputType="password" :inputPlaceholder="t('passwordLabel')" labelFor="password" :labelPlaceholder="t('passwordPlaceholder')" :label="t('passwordPlaceholder')" errorMessageName="password"/>
        <StyledFormItem inputName="confirmPassword" inputType="password" :inputPlaceholder="t('passwordConfirmLabel')" labelFor="confirmPassword" :labelPlaceholder="t('passwordConfirmPlaceholder')" :label="t('passwordConfirmPlaceholder')" errorMessageName="confirmPassword"/>
        <StyledFormItem inputName="phoneNumber" inputType="text" :inputPlaceholder="t('phoneNumberLabel')" labelFor="phoneNumber" :labelPlaceholder="t('phoneNumberPlaceholder')" :label="t('phoneNumberPlaceholder')" errorMessageName="phoneNumber"/>
        <StyledSelectFormItem inputName="sex" labelFor="sex" :label="t('sexLabel')" :labelPlaceholder="t('sexPlaceholder')" :options="[{ value: 'false', label: t('sexFemale') }, { value: 'true', label: t('sexMale') }]" errorMessageName="sex"/>
        <StyledFormItem inputName="personalIdNumber" inputType="text" :inputPlaceholder="t('personalIdNumberLabel')" labelFor="personalIdNumber" :labelPlaceholder="t('personalIdNumberPlaceholder')" :label="t('personalIdNumberPlaceholder')" errorMessageName="personalIdNumber"/>
        <StyledSelectFormItem inputName="country" labelFor="country" :label="t('countryLabel')" :labelPlaceholder="t('countryPlaceholder')" :options="countries" errorMessageName="country"/>        <StyledFormItem inputName="city" inputType="text" :inputPlaceholder="t('cityLabel')" labelFor="city" :labelPlaceholder="t('cityPlaceholder')" :label="t('cityPlaceholder')" errorMessageName="city"/>
        <StyledFormItem inputName="addressLine" inputType="text" :inputPlaceholder="t('addressLineLabel')" labelFor="addressLine" :labelPlaceholder="t('addressLinePlaceholder')" :label="t('addressLinePlaceholder')" errorMessageName="addressLine"/>
        <StyledFormItem inputName="zipCode" inputType="text" :inputPlaceholder="t('zipCodeLabel')" labelFor="zipCode" :labelPlaceholder="t('zipCodePlaceholder')" :label="t('zipCodePlaceholder')" errorMessageName="zipCode"/>
        <StyledFormItem inputName="dateOfBirth" inputType="date" :inputPlaceholder="t('dateOfBirthLabel')" labelFor="dateOfBirth" :labelPlaceholder="t('dateOfBirthPlaceholder')" :label="t('dateOfBirthPlaceholder')" errorMessageName="dateOfBirth"/>
        <Button type="submit" class="submit-button">
          {{ t('register') }}
        </Button>
        <a @click="router.push({name: 'login'})">{{ t('alreadyRegistered') }}</a>
      </Form>
    </CardContent>
  </Card>
</template>

<style scoped>
.form-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  justify-items: center;
  grid-column-gap: 70px;
  grid-row-gap: 6px;
  margin-top: 6%;
}

.form-container > * {
  width: 100%;
}

.submit-button {
  grid-column: span 2;
  margin-top: 20px;
  width: 40%;
}

.card {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 80vh;
}
</style>