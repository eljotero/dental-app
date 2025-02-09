<script setup lang="ts">
import {useI18n} from "vue-i18n";

const {t} = useI18n();
</script>

<template>
  <div class="home-container">
    <h1>{{t('welcomeText')}}</h1>
    <p>{{t('welcomeHeader')}}</p>
    <div class="buttons">
      <button class="primary-button">{{t('createAppointmentText')}}</button>
      <button class="secondary-button">{{t('contactUsText')}}</button>
    </div>
  </div>
</template>


<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 89vh;
  text-align: center;
  padding: 2em;
}

h1 {
  font-size: 2.5em;
  margin-bottom: 0.5em;
}

p {
  font-size: 1.2em;
  margin-bottom: 1.5em;
}

.buttons {
  display: flex;
  gap: 1em;
}

.primary-button, .secondary-button {
  padding: 0.75em 1.5em;
  font-size: 1em;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.primary-button {
  background-color: #007bff;
  color: white;
}

.secondary-button {
  background-color: #6c757d;
  color: white;
}
</style>