<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {useI18n} from "vue-i18n";
import {toTypedSchema} from "@vee-validate/zod";
import {z} from "zod";
import {CardContent, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import StyledFormItem from "@/components/StyledFormItem.vue";
import {Form} from "vee-validate";
import {setNewPassword} from "@/lib/axios.ts";
import {toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import router from "@/router";

const route = useRoute();
const token = ref('');
const {t} = useI18n();

const formSchema = toTypedSchema(z.object({
  password: z.string().nonempty(t('passwordError')).min(8, t('passwordLengthError')),
  passwordConfirmation: z.string().nonempty(t('passwordError')).min(8, t('passwordLengthError')),
}).refine(data => data.password === data.passwordConfirmation, {
  message: t('passwordMatchError'),
}));

onMounted(() => {
  token.value = route.query.token as string || '';
});

const onSubmit = async (values: any) => {
  const password = values.password;
  console.log(token.value, password);
  try {
    const response = await setNewPassword(password, token.value);
    if (response.status === 200) {
      toast.success(t('setNewPasswordSuccess'), {
        autoClose: 2000,
      });
      setTimeout(() => {
        router.push({ name: 'login' });
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
      <CardTitle>{{ t('resetPassword') }}</CardTitle>
      <Form @submit="onSubmit" :validation-schema="formSchema" class="form-container">
        <StyledFormItem inputName="password" inputType="password" :inputPlaceholder="t('passwordPlaceholder')" labelFor="password" :labelPlaceholder="t('passwordPlaceholder')" errorMessageName="password"/>
        <StyledFormItem inputName="passwordConfirmation" inputType="password" :inputPlaceholder="t('passwordConfirmPlaceholder')" labelFor="passwordConfirmation" :labelPlaceholder="t('passwordConfirmPlaceholder')" errorMessageName="passwordConfirmation"/>
        <Button type="submit" class="submit-button">
          {{ t('login') }}
        </Button>
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