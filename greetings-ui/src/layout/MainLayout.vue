<script lang="ts" setup>
import Menubar from 'primevue/menubar';
import {ref} from 'vue';
import {useRouter} from "vue-router";

const items = ref([
  {
    label: 'Form (Home)',
    icon: 'pi pi-home',
    route: '/form'
  },
  {
    label: 'Stats',
    icon: 'pi pi-chart-bar',
    route: '/stats'
  }
]);
const router = useRouter();
</script>

<template>
  <Menubar :model="items">
    <template #item="{ item, props }">
      <router-link v-slot="{ href, navigate }" :to="item.route" custom>
        <a :href="href" v-bind="props.action" @click="navigate">
          <span :class="item.icon">{{ item.label }}</span>
        </a>
      </router-link>
    </template>
  </Menubar>
  <main class="p-4 flex flex-col justify-around h-screen w-full max-w-7xl mx-auto">
    <RouterView/>
  </main>
</template>

<style scoped>
/* No custom styles needed - using PrimeVue components with default Tailwind CSS */
</style>
