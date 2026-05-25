<script lang="ts" setup>
import {ref, watch} from 'vue';
import Select from 'primevue/select';

const props = defineProps<{
  label: string,
  values: string[]
}>()

const emit = defineEmits<{
  (e: 'typeSelected', value: string): void
}>()

const selectedType = ref<string>();

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
      :placeholder="props.label"
      class="w-full md:w-14rem"
      data-cy="greeting-type"
  />
</template>
