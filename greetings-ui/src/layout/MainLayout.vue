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
  <div class="card">
    <Menubar :model="items">
      <template #item="{ item, props, hasSubmenu }">
        <router-link v-if="item.route" v-slot="{ href, navigate }" :to="item.route" custom>
          <a :href="href" v-bind="props.action" @click="navigate">
            <span :class="item.icon"/>
            <span>{{ item.label }}</span>
          </a>
        </router-link>
        <a v-else :href="item.url" :target="item.target" v-bind="props.action">
          <span :class="item.icon"/>
          <span>{{ item.label }}</span>
          <span v-if="hasSubmenu" class="pi pi-fw pi-angle-down"/>
        </a>
      </template>
    </Menubar>
  </div>
  <main class="p-4 flex flex-col justify-around h-screen w-full max-w-7xl mx-auto">
    <RouterView/>
  </main>
</template>

<style scoped>
/* No custom styles needed - using PrimeVue components with default Tailwind CSS */
</style>
