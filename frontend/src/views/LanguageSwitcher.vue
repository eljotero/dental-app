<template>
  <div>
    <button @click="setLanguage('en')" style="margin-right: 8px">English</button>
    <button @click="setLanguage('pl')">Polish</button>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n';
import { watchEffect } from 'vue';
import store from '../store/index.ts';

const { locale } = useI18n();

const setLanguage = (newLang) => {
  store.dispatch('setLanguage', newLang);
};

watchEffect(() => {
  const storedLang = store.getters.getLanguage;
  if (storedLang) {
    locale.value = storedLang;
  }
});
</script>