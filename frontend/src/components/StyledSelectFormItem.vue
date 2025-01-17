<script setup lang="ts">
import {ErrorMessage, Field} from "vee-validate";

const props = defineProps({
  inputName: {
    type: String,
    required: true
  },
  labelFor: {
    type: String,
    required: true
  },
  labelPlaceholder: {
    type: String,
    required: true
  },
  errorMessageName: {
    type: String,
    required: true
  },
  options: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <div>
    <label :for="labelFor">{{ labelPlaceholder }}</label>
    <Field :name="inputName" as="select" class="w-full border p-2 rounded">
      <option v-for="option in options" :key="option.value" :value="option.value">
        {{ option.label }}
      </option>
    </Field>
    <ErrorMessage :name="errorMessageName"/>
  </div>
</template>