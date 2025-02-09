<script setup lang="ts">
import {useI18n} from "vue-i18n";
import {onMounted, ref} from "vue";
import type {CreateSupply, SupplyDetails, UpdateSupply} from '@/lib/types';
import {createSupply, deleteSupply, getSupplies, updateSupply} from "@/lib/axios.ts";
import {Button} from "@/components/ui/button";
import {Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger} from "@/components/ui/dialog";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {Form} from "@/components/ui/form";
import StyledFormItem from "@/components/StyledFormItem.vue";
import {toTypedSchema} from "@vee-validate/zod";
import * as z from "zod";
import {handleRequest, handleSubmit} from "@/lib/functions.ts";
import {toast} from "vue3-toastify";
import 'vue3-toastify/dist/index.css';

const {t} = useI18n();
const supplies = ref<SupplyDetails[]>([]);
const selectedSupplyId = ref<number>(0);
const editFormRef = ref({} as UpdateSupply);
const originalData = {} as UpdateSupply;

const createFormSchema = toTypedSchema(z.object({
  name: z.string().nonempty(t('supplyNameError')),
  quantity: z.number().positive(t('supplyQuantityError')),
  link: z.string().optional()
}));

const editFormSchema = toTypedSchema(z.object({
  name: z.string().nonempty(t('supplyNameError')),
  quantity: z.number().positive(t('supplyQuantityError')),
  link: z.string().optional()
}));


onMounted(async () => {
  try {
    const response = await getSupplies();
    if (response.status === 200) {
      supplies.value = response.data;
    } else {
      console.error('Error while fetching supplies');
    }
  } catch (e) {
    console.error('Error while fetching supplies');
  }
});

const onSubmitCreate = async (values: any) => {
  const dto = values as CreateSupply;
  await handleRequest(
      (dto) => createSupply(dto),
      dto,
      t('supplyCreated'),
      t('supplyCreateError'),
      t,
      201
  );
};

const onSubmitEdit = async (values: any) => {
  await handleSubmit(values, originalData, updateSupply, selectedSupplyId.value, t('updateDataSuccess'), t('updateDataError'), t);
};

const setEditFormValues = (supply: SupplyDetails) => {
  editFormRef.value = {
    name: supply.name,
    quantity: supply.quantity,
    link: supply.link
  };
  selectedSupplyId.value = supply.supplyId;
  originalData.name = supply.name;
  originalData.quantity = supply.quantity;
  originalData.link = supply.link;
};

const removeSupply = async (supplyId: number) => {
  try {
    const response = await deleteSupply(supplyId);
    if (response.status === 200) {
      toast.success(t('supplyDeleted'), {
        autoClose: 2000,
      });
    } else {
      toast.error(t('supplyDeleteError'), {
        autoClose: 3000,
      });
    }
    setTimeout(() => {
      window.location.reload();
    }, 2000);
  } catch (e) {
    console.error('Error while deleting supply');
  }
};

</script>

<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">{{ t('suppliesList') }}</h1>
    <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createFormSchema">
      <Dialog>
        <DialogTrigger as-child>
          <Button>
            {{ t('createButton') }}
          </Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>{{ t('createButton') }}</DialogTitle>
          </DialogHeader>
          <form id="createDialogForm" @submit="handleSubmit($event, onSubmitCreate)">
            <StyledFormItem inputName="name" inputType="text" :inputPlaceholder="t('supplyNameInputPlaceholder')"
                            labelFor="name" :labelPlaceholder="t('supplyNameLabelPlaceholder')"
                            errorMessageName="name"/>
            <StyledFormItem inputName="quantity" inputType="number"
                            :inputPlaceholder="t('supplyQuantityInputPlaceholder')" labelFor="quantity"
                            :labelPlaceholder="t('supplyQuantityLabelPlaceholder')" errorMessageName="quantity"/>
            <StyledFormItem inputName="link" inputType="text" :inputPlaceholder="t('supplyLinkInputPlaceholder')"
                            labelFor="link" :labelPlaceholder="t('supplyLinkLabelPlaceholder')"
                            errorMessageName="link"/>
            <DialogFooter>
              <DialogTrigger as-child>
                <Button type="submit" form="createDialogForm">
                  {{ t('saveChanges') }}
                </Button>
              </DialogTrigger>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </Form>
    <Table class="min-w-full bg-white border border-gray-200">
      <TableHeader>
        <TableRow class="bg-gray-100">
          <TableHead class="py-2 px-4 border-b">{{ t('supplyNameLabelPlaceholder') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('supplyQuantityLabelPlaceholder') }}</TableHead>
          <TableHead class="py-2 px-4 border-b">{{ t('supplyLinkLabelPlaceholder') }}</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow v-for="supply in supplies" :key="supply.supplyId" class="hover:bg-gray-50">
          <TableCell class="py-2 px-4 border-b">{{ supply.name }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ supply.quantity }}</TableCell>
          <TableCell class="py-2 px-4 border-b">{{ supply.link }}</TableCell>
          <Form v-slot="{ handleSubmit }" as="" :validation-schema="editFormSchema" keep-values>
            <Dialog>
              <DialogTrigger as-child>
                <Button variant="outline" @click="setEditFormValues(supply)">
                  {{ t('editButton') }}
                </Button>
              </DialogTrigger>
              <DialogContent class="sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle>{{ t('editButton') }}</DialogTitle>
                </DialogHeader>
                <form id="editDialogForm" @submit="handleSubmit($event, onSubmitEdit)">
                  <StyledFormItem inputName="name" inputType="text" :inputPlaceholder="t('supplyNameInputPlaceholder')"
                                  labelFor="name" :labelPlaceholder="t('supplyNameLabelPlaceholder')"
                                  errorMessageName="name" :model-value="editFormRef.name"/>
                  <StyledFormItem inputName="quantity" inputType="number"
                                  :inputPlaceholder="t('supplyQuantityInputPlaceholder')" labelFor="quantity"
                                  :labelPlaceholder="t('supplyQuantityLabelPlaceholder')" errorMessageName="quantity" :model-value="editFormRef.quantity"/>
                  <StyledFormItem inputName="link" inputType="text" :inputPlaceholder="t('supplyLinkInputPlaceholder')"
                                  labelFor="link" :labelPlaceholder="t('supplyLinkLabelPlaceholder')"
                                  errorMessageName="link" :model-value="editFormRef.link"/>
                  <DialogFooter>
                    <DialogTrigger as-child>
                      <Button type="submit" form="editDialogForm">
                        {{ t('saveChanges') }}
                      </Button>
                    </DialogTrigger>
                  </DialogFooter>
                </form>
              </DialogContent>
            </Dialog>
          </Form>
          <Button variant="destructive" @click="removeSupply(supply.supplyId)">
            {{ t('deleteButton') }}
          </Button>
        </TableRow>
      </TableBody>
    </Table>
  </div>
</template>