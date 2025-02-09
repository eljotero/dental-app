<script setup lang="ts">
import {CardContent, CardTitle} from "@/components/ui/card";
import {useI18n} from "vue-i18n";
import {toTypedSchema} from "@vee-validate/zod";
import {z} from "zod";
import type {LoginDto} from "@/lib/types";
import {Form} from "vee-validate";
import {Button} from "@/components/ui/button";
import router from "@/router";
import {login, resetPassword} from "@/lib/axios.ts";
import {toast} from "vue3-toastify";
import 'vue3-toastify/dist/index.css';
import store from "@/store";
import { ref } from 'vue';
import StyledFormItem from "@/components/StyledFormItem.vue";

const {t} = useI18n();
const email = ref('');

const formSchema = toTypedSchema(z.object({
  email: z.string().email(t('emailError')),
  password: z.string().nonempty(t('passwordError')).min(8, t('passwordLengthError')),
}));

const onSubmit = async (values: any) => {
  const dto = values as LoginDto;
  try {
    const response = await login(dto);
    if(response.status === 200) {
      store.commit('setUserToken', response.data.token);
      store.commit('setRole', response.data.role);
      store.commit('setLanguage', response.data.language);
      toast.success(t('loginSuccess'), {
        autoClose: 2000,
      });
      setTimeout(() => {
        router.push({name: 'home'});
      }, 2000);
    }
  } catch (error: any) {
    if(error.status === 403) {
      toast.error(t('wrongPasswordError'), {
        autoClose: 3000,
      });
      return;
    }
    const errorMessage = error.response.data.message;
    const errorMessages = Array.isArray(errorMessage) ? errorMessage.join(', ') : errorMessage;
    toast.error(errorMessages, {
      autoClose: 3000,
    });
  }
};

const refreshPassword = async() => {
  try {
    const response = await resetPassword(email.value);
    if(response.status === 200) {
      toast.success(t('resetPasswordSuccess'), {
        autoClose: 3000,
      });
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
      <CardTitle>{{ t('login') }}</CardTitle>
      <Form @submit="onSubmit" :validation-schema="formSchema" class="form-container">
        <StyledFormItem v-model="email" inputName="email" inputType="email" :inputPlaceholder="t('emailPlaceholder')" labelFor="email" :labelPlaceholder="t('emailLabel')" errorMessageName="email"/>
        <StyledFormItem inputName="password" inputType="password" :inputPlaceholder="t('passwordPlaceholder')" labelFor="password" :labelPlaceholder="t('passwordLabel')" errorMessageName="password"/>
        <Button type="submit" class="submit-button">
          {{ t('login') }}
        </Button>
        <a @click="router.push({name: 'register'})">{{ t('notRegistered') }}</a>
        <a @click="refreshPassword()">{{ t('forgotPassword') }}</a>
      </Form>
    </CardContent>
  </Card>
</template>

<style scoped>
.form-container {
  display: grid;
  grid-template-columns: 1fr;
  justify-items: center;
  grid-column-gap: 70px;
  grid-row-gap: 6px;
  margin-top: 6%;
}
.form-container > * {
  width: 100%;
}
.submit-button {
  grid-column: span 1;
  margin-top: 20px;
  width: 70%;
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