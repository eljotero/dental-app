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
  <FormField v-slot="{ field }" :name="name">
    <FormItem>
      <FormLabel>{{ label }}</FormLabel>
      <FormControl>
        <Input :type="type" :placeholder="placeholder" v-bind="field"/>
      </FormControl>
      <FormMessage/>
    </FormItem>
  </FormField>
</template>