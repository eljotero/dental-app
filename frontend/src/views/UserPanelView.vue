<script setup lang="ts">
import type {UserProfile} from '@/lib/types';
import {getUserProfile} from '@/lib/axios';
import {onMounted, ref} from 'vue';
import {Card, CardContent, CardTitle} from "@/components/ui/card";
import {Button} from '@/components/ui/button';
import {useI18n} from 'vue-i18n';
import {toTypedSchema} from "@vee-validate/zod";
import {z} from "zod";
import {useCountries} from '@/lib/countries';
import {Form} from 'vee-validate';
import 'vue3-toastify/dist/index.css';
import StyledFormItem from "@/components/StyledFormItem.vue";
import StyledSelectFormItem from "@/components/StyledSelectFormItem.vue";


const {t} = useI18n();

const userProfileRef = ref<UserProfile>({} as UserProfile);
const originalData = {} as UserProfile;
const countries = useCountries();
const sexes = [
  {value: 'false', label: t('sexFemale')},
  {value: 'true', label: t('sexMale')},
]

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
  console.log(values);
  // const dto: UpdateUserProfile = {};
  // for (const key in values) {
  //   if (values[key] !== originalData[key]) {
  //     dto[key] = values[key];
  //   }
  // }
  // if (Object.keys(dto).length === 0) {
  //   toast.info(t('noDataToUpdate'), {
  //     autoClose: 2000,
  //   });
  //   return;
  // }
  // const response = await updateProfile(dto);
  // if (response.status === 200) {
  //   toast.success(t('updateDataSuccess'), {
  //     autoClose: 2000,
  //   });
  // } else {
  //   toast.error(t('updateDataError'), {
  //     autoClose: 3000,
  //   });
  // }
}
</script>

<template>
  <Card class="card">
    <CardContent>
      <CardTitle>{{ t('updateDataHeader') }}</CardTitle>
      <Form @submit="onSubmit" ref="userProfileRef" :validation-schema="formSchema" class="form-container">
        <StyledFormItem
            inputName="firstName"
            inputType="text"
            :inputPlaceholder="t('firstNamePlaceholder')"
            labelFor="firstName"
            :labelPlaceholder="t('firstNameLabel')"
            errorMessageName="firstName"
        />
        <StyledFormItem
            inputName="lastName"
            inputType="text"
            :inputPlaceholder="t('lastNamePlaceholder')"
            labelFor="lastName"
            :labelPlaceholder="t('lastNameLabel')"
            errorMessageName="lastName"
        />
        <StyledFormItem
            inputName="email"
            inputType="email"
            :inputPlaceholder="t('emailPlaceholder')"
            labelFor="email"
            :labelPlaceholder="t('emailLabel')"
            errorMessageName="email"
        />
        <StyledFormItem
            inputName="phoneNumber"
            inputType="text"
            :inputPlaceholder="t('phoneNumberPlaceholder')"
            labelFor="phoneNumber"
            :labelPlaceholder="t('phoneNumberLabel')"
            errorMessageName="phoneNumber"
        />
        <StyledSelectFormItem :options="sexes" inputName="sex" labelFor="sex" labelPlaceholder="Sex" errorMessageName="sex"/>
        <StyledSelectFormItem :options="countries" inputName="country" labelFor="country" labelPlaceholder="Country" errorMessageName="country"/>
        <StyledFormItem
            inputName="city"
            inputType="text"
            :inputPlaceholder="t('cityPlaceholder')"
            labelFor="city"
            :labelPlaceholder="t('cityLabel')"
            errorMessageName="city"
        />
        <StyledFormItem
            inputName="address"
            inputType="text"
            :inputPlaceholder="t('addressLinePlaceholder')"
            labelFor="address"
            :labelPlaceholder="t('addressLineLabel')"
            errorMessageName="address"
        />
        <StyledFormItem
            inputName="zipCode"
            inputType="text"
            :inputPlaceholder="t('zipCodePlaceholder')"
            labelFor="zipCode"
            :labelPlaceholder="t('zipCodeLabel')"
            errorMessageName="zipCode"
        />
        <StyledFormItem
            inputName="dateOfBirth"
            inputType="date"
            :inputPlaceholder="t('dateOfBirthPlaceholder')"
            labelFor="dateOfBirth"
            :labelPlaceholder="t('dateOfBirthLabel')"
            errorMessageName="dateOfBirth"
        />
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