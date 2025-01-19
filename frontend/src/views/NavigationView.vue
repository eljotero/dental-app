<script setup>
import { useI18n } from 'vue-i18n';
import { computed, watchEffect } from 'vue';
import store from '../store/index.ts';
import { NavigationMenu, NavigationMenuItem, NavigationMenuList, NavigationMenuLink } from "@/components/ui/navigation-menu/index.ts";
import { useRoute } from "vue-router";
import router from "@/router";
import {changeLanguage} from '@/lib/axios';

const { t, locale } = useI18n();

const setLanguage = async (newLang) => {
  await store.dispatch('setLanguage', newLang);
  locale.value = newLang;
  if(isLoggedIn.value) {
    await changeLanguage(newLang);
  }
};

const logout = () => {
  store.dispatch('clearUserToken');
  store.dispatch('clearRole');
  router.push({name: 'home'});
}

const route = useRoute();
const currentRoute = computed(() => route.path);
const isLoggedIn = computed(() => !!store.getters.getUserToken);
const currentLang = computed(() => store.getters.getLanguage);
const role = computed(() => store.getters.getRole);

watchEffect(() => {
  const storedLang = store.getters.getLanguage;
  if (storedLang) {
    locale.value = storedLang;
  }
});
</script>

<template>
  <NavigationMenu class="bg-gray-100 p-4 rounded-lg shadow-md w-full fixed top-0 left-0">
    <NavigationMenuList>
      <NavigationMenuItem>
        <NavigationMenuLink href="/" class="nav-link">{{ t('homePage') }}</NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="!isLoggedIn && currentRoute !== '/login' && currentRoute !== '/register'">
        <NavigationMenuLink href="/login" class="nav-link">{{ t('alreadyRegistered') }}</NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="!isLoggedIn && currentRoute !== '/login' && currentRoute !== '/register'">
        <NavigationMenuLink href="/register" class="nav-link">{{ t('notRegistered') }}</NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="currentRoute !== '/login' && currentRoute !== '/register'">
        <NavigationMenuLink href="/profile" class="nav-link">{{ t('profilePanel') }}</NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="currentRoute !== '/login' && currentRoute !== '/register' && currentRoute !== '/priceList'">
        <NavigationMenuLink href="/priceList" class="nav-link">{{ t('priceList') }}</NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="currentRoute !== '/login' && currentRoute !== '/register' && role === 'PATIENT'">
        <NavigationMenuLink href="/createAppointment" class="nav-link">{{ t('createAppointment') }}</NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="currentRoute !== '/login' && currentRoute !== '/register' && currentRoute !== '/appointments' && isLoggedIn && currentRoute !== '/adminPanel'">
        <NavigationMenuLink href="/appointments" class="nav-link">{{ t('myAppointments') }}</NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="currentLang !== 'en'">
        <button @click="setLanguage('en')" class="lang-button mr-2">{{t('enLang')}}</button>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="currentLang !== 'pl'">
        <button @click="setLanguage('pl')" class="lang-button">{{t('plLang')}}</button>
      </NavigationMenuItem>
      <NavigationMenuItem v-if="isLoggedIn && currentRoute !== '/login' && currentRoute !== '/register'">
        <button @click="logout()" class="lang-button">{{t('logout')}}</button>
      </NavigationMenuItem>
    </NavigationMenuList>
  </NavigationMenu>
</template>

<style scoped>
.lang-button {
  @apply bg-blue-500 text-white border-none py-2 px-4 rounded cursor-pointer transition-colors duration-300;
}

.lang-button:hover {
  @apply bg-blue-700;
}

.nav-link {
  @apply text-blue-500 no-underline py-2 px-4 rounded transition-colors duration-300;
}

.nav-link:hover {
  @apply bg-gray-200;
}
</style>