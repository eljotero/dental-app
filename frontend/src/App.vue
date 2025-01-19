<script setup lang="ts">
import { RouterView } from 'vue-router'
import LanguageSwitcher from "@/views/NavigationView.vue";
</script>

<template>
  <div class="mt-24">
    <LanguageSwitcher/>
    <RouterView />
  </div>
</template>