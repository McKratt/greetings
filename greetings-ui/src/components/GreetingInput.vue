<script lang="ts" setup>
import InputText from 'primevue/inputtext';
import {ref, watch} from 'vue';

defineProps<{
  label: string;
}>()
const emit = defineEmits<{
  update: [value: string]
}>()
const value = ref('');

watch(value, (newValue) => {
  emit('update', newValue);
});
</script>

<template>
  <div class="field">
    <label class="font-bold block mb-2" for="name">{{ label }}</label>
    <InputText id="name" v-model="value" class="w-full"/>
  </div>
</template>

<style scoped>
/* No custom styles needed - using PrimeVue components with default Tailwind CSS */
</style>
