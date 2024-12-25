<script setup lang="ts">
import { RouterView } from 'vue-router'
import LanguageSwitcher from "@/views/LanguageSwitcher.vue";
</script>

<template>
  <LanguageSwitcher/>
  <RouterView />
</template>