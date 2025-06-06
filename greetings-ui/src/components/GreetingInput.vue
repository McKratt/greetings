<script lang="ts" setup>
import InputText from 'primevue/inputtext';
import FloatLabel from 'primevue/floatlabel';
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
  <FloatLabel>
    <InputText id="name" v-model="value" class="w-full" data-cy="greeting-name"/>
    <label class="font-bold" for="name">{{ label }}</label>
  </FloatLabel>
</template>

<style scoped>
/* No custom styles needed - using PrimeVue components with default Tailwind CSS */
</style>
