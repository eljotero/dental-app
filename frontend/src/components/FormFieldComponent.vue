<script setup lang="ts">
import { defineProps } from 'vue';
import { FormItem, FormLabel, FormControl, FormMessage, FormField } from '@/components/ui/form';
import { Input } from '@/components/ui/input';

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  label: String,
  placeholder: String,
  type: {
    type: String,
    default: 'text'
  }
});
</script>

<template>
  <FormField v-slot="{ field }" :name="name" class="form-field">
    <FormItem>
      <FormLabel>{{ label }}</FormLabel>
      <FormControl>
        <Input :type="type" :placeholder="placeholder" v-bind="field"/>
      </FormControl>
      <FormMessage/>
    </FormItem>
  </FormField>
</template>

<style>
.form-field {
  width: 300px;
  margin-bottom: 15px;
}
.form-field label {
  display: block;
  text-align: center;
  margin-bottom: 5px;
}

.form-field input,
.form-field select {
  width: 100%;
}

</style>