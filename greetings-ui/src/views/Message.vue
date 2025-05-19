<script lang="ts" setup>
import {greetingRepository} from "../composables/GreetingsRepository";
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {GreetingMessage} from "../models/GreetingMessage";
import Message from 'primevue/message';
import Card from 'primevue/card';

const route = useRoute();
const greetingMessage = ref<GreetingMessage | undefined>(undefined);
const errorMessage = ref<string>('');

onMounted(() => {
  const id = route.params.id as string;
  if (id) {
    const message = greetingRepository.getGreetingById(id);
    if (message) {
      greetingMessage.value = message;
    } else {
      errorMessage.value = `No message found with ID: ${id}`;
    }
  } else {
    errorMessage.value = 'No message ID provided';
  }
});
</script>

<template>
  <div class="card flex justify-center">
    <Card class="w-full max-w-md mx-auto">
      <template #content>
        <p v-if="greetingMessage" class="text-center">{{ greetingMessage.message }}</p>
        <Message v-else-if="errorMessage" :closable="false" severity="error">{{ errorMessage }}</Message>
        <p v-else class="text-center">Loading...</p>
      </template>
    </Card>
  </div>
</template>

<style scoped>
/* No custom styles needed - using PrimeVue components with default Tailwind CSS */
</style>
