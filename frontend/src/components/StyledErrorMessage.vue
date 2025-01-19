<script setup lang="ts">
import {ErrorMessage} from "vee-validate";
const props = defineProps({
  name: {
    type: String,
    required: true
  }
});
</script>

<template>
  <ErrorMessage :name="name" class="text-red-500" />
</template>