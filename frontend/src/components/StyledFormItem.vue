<script setup lang="ts">
import {ErrorMessage, Field} from "vee-validate";

const props = defineProps({
  inputName: {
    type: String,
    required: true
  },
  inputType: {
    type: String,
    required: true
  },
  inputPlaceholder: {
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
  modelValue: {
    type: String,
    required: false
  }
});

const emit = defineEmits(['update:modelValue']);
</script>

<template>
  <div>
    <label :for="labelFor">{{ labelPlaceholder }}</label>
    <Field
        :name="inputName"
        :type="inputType"
        :placeholder="inputPlaceholder"
        class="w-full border p-2 rounded"
        :value="modelValue"
        @input="event => emit('update:modelValue', event.target.value)"
    />
    <ErrorMessage :name="errorMessageName" class="text-red-500"/>
  </div>
</template>