<script setup lang="ts">
import type {UpdateUserProfile, UserProfile} from '@/lib/types';
import {getUserProfile, updateProfile} from '@/lib/axios';
import {onMounted, ref} from 'vue';
import {Card, CardContent, CardTitle} from "@/components/ui/card";
import FormFieldComponent from '@/components/FormFieldComponent.vue';
import SelectFieldComponent from '@/components/SelectFieldComponent.vue';
import {Button} from '@/components/ui/button';
import {useI18n} from 'vue-i18n';
import {toTypedSchema} from "@vee-validate/zod";
import {z} from "zod";
import {useCountries} from '@/lib/countries';
import {Form} from "@/components/ui/form";
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const userProfileRef = ref<UserProfile>({} as UserProfile);
const originalData = {} as UserProfile;
const countries = useCountries();

const {t} = useI18n();

const formSchema = toTypedSchema(z.object({
  firstName: z.string().nonempty(t('firstNameError')),
  lastName: z.string().nonempty(t('lastNameError')),
  email: z.string().email(t('emailError')),
  sex: z.string().nonempty(t('sexError')),
  phoneNumber: z.string().nonempty(t('phoneNumberError')).regex(/^(\+\d{1,3})?(\d{3})?\d{3}\d{3}$/, {
    message: t('phoneNumberLengthError'),
  }),
  country: z.string().nonempty(t('countryError')),
  city: z.string().nonempty(t('cityError')),
  address: z.string().nonempty(t('addressLineError')),
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
}));

onMounted(async () => {
  const response = await getUserProfile();
  if (response.status === 200 && response.data) {
    userProfileRef.value.setValues({
      firstName: response.data.firstName,
      lastName: response.data.lastName,
      email: response.data.email,
      phoneNumber: response.data.phoneNumber,
      city: response.data.city,
      address: response.data.address,
      zipCode: response.data.zipCode,
      dateOfBirth: response.data.dateOfBirth,
      sex: response.data.sex.toString(),
      country: response.data.country,
    });
    originalData.firstName = response.data.firstName;
    originalData.lastName = response.data.lastName;
    originalData.email = response.data.email;
    originalData.phoneNumber = response.data.phoneNumber;
    originalData.city = response.data.city;
    originalData.address = response.data.address;
    originalData.zipCode = response.data.zipCode;
    originalData.dateOfBirth = response.data.dateOfBirth;
    originalData.sex = response.data.sex.toString();
    originalData.country = response.data.country;
  } else {
    console.error('Error while fetching user profile');
  }
});

const onSubmit = async (values: any) => {
  const dto: UpdateUserProfile = {};
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
  const response = await updateProfile(dto);
  if (response.status === 200) {
    toast.success(t('updateDataSuccess'), {
      autoClose: 2000,
    });
  } else {
    toast.error(t('updateDataError'), {
      autoClose: 3000,
    });
  }
}
</script>

<template>
  <Card class="card">
    <CardContent>
      <CardTitle>{{ t('updateDataHeader') }}</CardTitle>
      <Form @submit="onSubmit" ref="userProfileRef" :validation-schema="formSchema" class="form-container">
        <FormFieldComponent name="firstName" :label="t('firstNameLabel')" :placeholder="t('firstNamePlaceholder')"/>
        <FormFieldComponent name="lastName" :label="t('lastNameLabel')" :placeholder="t('lastNamePlaceholder')"/>
        <FormFieldComponent name="email" type="email" :label="t('emailLabel')" :placeholder="t('emailPlaceholder')"/>
        <FormFieldComponent name="phoneNumber" :label="t('phoneNumberLabel')"
                            :placeholder="t('phoneNumberPlaceholder')"/>
        <SelectFieldComponent name="sex" :label="t('sexLabel')" :placeholder="t('sexPlaceholder')"
                              :options="[{ value: 'false', label: t('sexFemale') }, { value: 'true', label: t('sexMale') }]"/>
        <SelectFieldComponent name="country" :label="t('countryLabel')" :placeholder="t('countryPlaceholder')"
                              :options="countries"/>
        <FormFieldComponent name="city" :label="t('cityLabel')" :placeholder="t('cityPlaceholder')"/>
        <FormFieldComponent name="address" :label="t('addressLineLabel')"
                            :placeholder="t('addressLinePlaceholder')"/>
        <FormFieldComponent name="zipCode" :label="t('zipCodeLabel')" :placeholder="t('zipCodePlaceholder')"/>
        <FormFieldComponent name="dateOfBirth" type="date" :label="t('dateOfBirthLabel')"
                            :placeholder="t('dateOfBirthPlaceholder')"/>
        <Button type="submit" class="submit-button">
          {{ t('updateData') }}
        </Button>
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