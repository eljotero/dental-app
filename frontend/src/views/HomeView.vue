<script setup lang="ts">
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
} from '@/components/ui/navigation-menu'
</script>

<template>
  <NavigationMenu>
    <NavigationMenuList>
      <NavigationMenuItem>
        <NavigationMenuLink>
          <a href="/">Home</a>
        </NavigationMenuLink>
      </NavigationMenuItem>
      <NavigationMenuItem>
        <NavigationMenuLink>
          <a href="/register">Register</a>
        </NavigationMenuLink>
      </NavigationMenuItem>
    </NavigationMenuList>
  </NavigationMenu>
</template>
