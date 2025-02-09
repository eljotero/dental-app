<script setup lang="ts">
import {ref} from 'vue';
import {Card, CardContent, CardTitle} from '@/components/ui/card';
import {useI18n} from "vue-i18n";
import * as z from 'zod';
import {toTypedSchema} from "@vee-validate/zod";
import {Form} from "vee-validate";
import StyledFormItem from "@/components/StyledFormItem.vue";
import {Button} from '@/components/ui/button';
import {getStatistics} from '@/lib/axios';
import {BarChart} from '@/components/ui/chart-bar';
import {DonutChart} from '@/components/ui/chart-donut';
import {LineChart} from '@/components/ui/chart-line';

const {t} = useI18n();

const formSchema = toTypedSchema(z.object({
  startDate: z.string().nonempty(t('startDateRequired')),
}));

const reportData = ref<StatsData | null>(null);

const onSubmit = async (values: any) => {
  try {
    const response = await getStatistics(values.startDate);
    if (response.status === 200) {
      const data = response.data;
      reportData.value = {
        ...data,
        paymentTypeRatio: Object.entries(data.paymentTypeRatio).map(([name, total]) => ({
          name: t(`paymentType${name.charAt(0).toUpperCase() + name.slice(1)}`),
          total
        })),
        patientVisitsByAgeGroup: Object.entries(data.patientVisitsByAgeGroup).map(([name, total]) => ({name, total})),
        weeklyRevenue: Object.entries(data.weeklyRevenue).map(([week, revenue]) => ({week, revenue})),
      };
      console.log(reportData.value);
    } else {
      console.error('Error while fetching statistics');
    }
  } catch (e) {
    console.error('Error while fetching statistics', e);
  }
};

const valueFormatter = (tick: number | Date) => typeof tick === 'number' ? `$ ${new Intl.NumberFormat('us').format(tick).toString()}` : '';

</script>

<template>
  <div class="container mx-auto p-4 mt-20">
    <Card class="p-4 shadow-lg">
      <CardContent>
        <CardTitle>{{ t('reportsHeader') }}</CardTitle>
        <Form @submit="onSubmit" :validation-schema="formSchema">
          <StyledFormItem inputName="startDate" inputType="date" :inputPlaceholder="t('startDatePlaceholder')"
                          labelFor="startDate" :labelPlaceholder="t('startDateLabel')" errorMessageName="startDate"/>
          <Button type="submit" class="submit-button">
            {{ t('generateButton') }}
          </Button>
        </Form>

        <div v-if="reportData" class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <Card class="p-4 shadow-lg">
            <CardContent>
              <h3 class="text-center">{{ t('paymentTypeRatio') }}</h3>
              <DonutChart
                  :data="reportData.paymentTypeRatio"
                  index="name"
                  :category="'total'"
                  :colors="['#FF6384', '#36A2EB', '#FFCE56']"
              />
              <p class="text-center">{{ t('paymentTypeRatio') }}</p>
            </CardContent>
          </Card>
          <Card class="p-4 shadow-lg">
            <CardContent>
              <BarChart
                  :data="reportData.patientVisitsByAgeGroup"
                  index="name"
                  :categories="['total']"
                  :y-formatter="(tick, i) => {
                return typeof tick === 'number'
                  ? `$ ${new Intl.NumberFormat('us').format(tick).toString()}`
                  : ''
              }"
                  :colors="['#4BC0C0', '#FF9F40', '#9966FF']"
                  :show-values="false"
                  :x-axis-label="t('ageGroup')"
              />
              <p class="text-center">{{ t('ageRatio') }}</p>
            </CardContent>
          </Card>
        </div>

        <div v-if="reportData" class="mt-4">
          <Card class="p-4 shadow-lg">
            <CardContent>
              <h3 class="text-center">{{ t('weeklyRevenue') }}</h3>
              <LineChart
                  :data="reportData.weeklyRevenue"
                  index="week"
                  :categories="['revenue']"
                  :y-formatter="(tick, i) => {
                return typeof tick === 'number'
                  ? `$ ${new Intl.NumberFormat('us').format(tick).toString()}`
                  : ''
              }"
                  :colors="['#4BC0C0', '#FF9F40', '#9966FF']"
                  class="line-chart"
              />
              <p class="text-center">{{ t('weeklyRevenue') }}</p>
            </CardContent>
          </Card>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.line-chart {
  height: 400px;
  width: 100%;
}
</style>

<script lang="ts">
export interface StatsData {
  patientVisitsByAgeGroup: { name: string, total: number }[];
  paymentTypeRatio: { name: string, total: number }[];
  patientVisitsByGender: { name: string, total: number }[];
  weeklyRevenue: { week: string, revenue: number }[];
}
</script>