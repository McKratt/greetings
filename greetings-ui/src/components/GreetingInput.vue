<script lang="ts" setup>
import InputText from 'primevue/inputtext';
import FloatLabel from 'primevue/floatlabel';
import {computed, ref, watch} from 'vue';

const props = defineProps<{
  label: string;
}>()
const emit = defineEmits<{
  update: [value: string]
}>()
const value = ref('');
const inputId = computed(() => props.label.toLowerCase().replace(/\s+/g, '-'));

watch(value, (newValue) => {
  emit('update', newValue);
});
</script>

<template>
  <FloatLabel>
    <InputText :id="inputId" v-model="value" class="w-full" data-cy="greeting-name"/>
    <label class="font-bold" :for="inputId">{{ label }}</label>
  </FloatLabel>
</template>

<style scoped>
</style>
