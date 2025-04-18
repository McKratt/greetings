<script lang="ts" setup>
import {greetingRepository} from "../composables/GreetingsRepository";
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {GreetingMessage} from "../models/GreetingMessage";

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
  <article class="main-content">
    <p v-if="greetingMessage">{{ greetingMessage.message }}</p>
    <p v-else-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p v-else>Loading...</p>
  </article>
</template>

<style scoped>
.main-content {
  @apply mx-auto
}

.error-message {
  @apply text-red-500 font-semibold
}
</style>
