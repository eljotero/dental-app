<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">{{ t('priceList') }}</h1>
    <Table class="min-w-full bg-white border border-gray-200">
      <TableHeader>
        <TableRow class="bg-gray-100">
          <TableHead class="py-2 px-4 border-b">{{ t('treatmentName') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('treatmentDescription') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('treatmentPrice') }}</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow v-for="treatment in treatments" :key="treatment.treatmentId" class="hover:bg-gray-50">
          <TableCell class="py-2 px-4 border-b">{{ treatment.treatmentName }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ treatment.treatmentDescription }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ treatment.treatmentPrice }}</TableCell>
        </TableRow>
      </TableBody>
    </Table>
  </div>
</template>

<script setup lang="ts">
import {getTreatments} from '@/lib/axios';
import type {Treatment} from '@/lib/types';
import {onMounted, ref} from 'vue';
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow,} from '@/components/ui/table/index.ts';
import {useI18n} from "vue-i18n";

const treatments = ref<Treatment[]>([]);

const {t} = useI18n();

onMounted(async () => {
  const response = await getTreatments();
  if (response.status === 200) {
    treatments.value = response.data;
  } else {
    console.error('Error while fetching treatments');
  }
});
</script>