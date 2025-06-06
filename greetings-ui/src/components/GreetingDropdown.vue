<script lang="ts" setup>
import {ref, watch} from 'vue';
import Select from 'primevue/select';

defineProps<{
  label: string,
  values: string[]
}>()

const emit = defineEmits<{
  (e: 'typeSelected', value: string): void
}>()

let selectedType = ref<string>();

watch(selectedType, (newValue) => {
  if (newValue) {
    emit('typeSelected', newValue);
  }
});
</script>

<template>
  <Select
      id="type"
      v-model="selectedType"
      :options="values"
      class="w-full md:w-14rem"
      placeholder="Select a type"
      data-cy="greeting-type"
  />
</template>
