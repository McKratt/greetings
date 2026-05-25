<script lang="ts" setup>
import {getStats} from "../api/stats.api.ts";
import {onMounted, ref} from 'vue';
import Message from 'primevue/message';
import Card from 'primevue/card';

interface StatRow {
  type: string;
  count: number;
}

const statsRows = ref<StatRow[]>([]);
const isLoading = ref(true);
const errorMessage = ref('');
const isEmpty = ref(false);

onMounted(async () => {
  try {
    const data = await getStats();
    if (!data) {
      isEmpty.value = true;
    } else {
      statsRows.value = Object.entries(data.counters).map(([type, count]) => ({type, count}));
    }
  } catch {
    errorMessage.value = 'Failed to load statistics. Please try again later.';
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div class="w-full max-w-md mx-auto">
    <h2 class="text-xl font-bold mb-4">Greeting Statistics</h2>

    <p v-if="isLoading" class="text-center text-gray-500">Loading statistics...</p>

    <Message v-else-if="errorMessage" :closable="false" severity="error">{{ errorMessage }}</Message>

    <Card v-else-if="isEmpty">
      <template #content>
        <p class="text-center text-gray-500">No statistics available yet</p>
      </template>
    </Card>

    <Card v-else>
      <template #content>
        <table class="w-full text-left">
          <thead>
            <tr class="border-b">
              <th class="py-2 pr-4 font-semibold">Type</th>
              <th class="py-2 font-semibold">Count</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in statsRows" :key="row.type" class="border-b last:border-0">
              <td class="py-2 pr-4">{{ row.type }}</td>
              <td class="py-2 font-mono">{{ row.count }}</td>
            </tr>
          </tbody>
        </table>
      </template>
    </Card>
  </div>
</template>

<style scoped>
</style>
