<script setup lang="ts">
import { defineProps } from 'vue';
import { FormItem, FormLabel, FormControl, FormMessage, FormField } from '@/components/ui/form';
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import type {Option} from '@/lib/types';

const props = defineProps<{
  name: string;
  label: string;
  placeholder: string;
  options: Option[];
}>();
</script>

<template>
  <FormField v-slot="{ field }" :name="name">
    <FormItem>
      <FormLabel>{{ label }}</FormLabel>
      <Select v-bind="field">
        <FormControl>
          <SelectTrigger>
            <SelectValue :placeholder="placeholder"/>
          </SelectTrigger>
        </FormControl>
        <SelectContent>
          <SelectGroup>
            <SelectItem v-for="option in options" :key="option.value" :value="option.value">
              {{ option.label }}
            </SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>
      <FormMessage/>
    </FormItem>
  </FormField>
</template>