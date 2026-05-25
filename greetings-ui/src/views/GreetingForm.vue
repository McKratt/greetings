<script lang="ts" setup>
import GreetingInput from "../components/GreetingInput.vue";
import {Greeting} from "../models/greeting.model.ts";
import {EventType} from "../models/event-type.model.ts";
import GreetingDropdown from "../components/GreetingDropdown.vue";
import {greetingsService} from "../services/greetings.service.ts";
import router from "../router.ts";
import Button from 'primevue/button';
import Message from 'primevue/message';
import {ref} from 'vue';

const name = ref('');
const type = ref('');
const errorMessage = ref('');
const isLoading = ref(false);

const types: string[] = Object.values(EventType) as string[];

function updateName(value: string): void {
  name.value = value.trim();
}

function updateType(value: string): void {
  type.value = value.trim();
}

async function submit(): Promise<void> {
  errorMessage.value = '';

  if (!name.value) {
    errorMessage.value = 'Name is required';
    return;
  }
  if (!type.value) {
    errorMessage.value = 'Please select a type';
    return;
  }

  isLoading.value = true;
  try {
    const payload = new Greeting(EventType[type.value as keyof typeof EventType], name.value);
    const message = await greetingsService.createGreeting(payload);
    await router.push(`/messages/${message.id}`);
  } catch {
    errorMessage.value = 'Failed to create greeting. Please try again.';
  } finally {
    isLoading.value = false;
  }
}
</script>

<template>
  <form class="flex flex-col gap-4 w-full max-w-md mx-auto">
    <GreetingInput label="Name" @update="updateName"/>
    <GreetingDropdown :values="types" label="Choose a type" @typeSelected="updateType"/>
    <Message v-if="errorMessage" :closable="false" severity="error">{{ errorMessage }}</Message>
    <Button data-cy="create-greeting" label="Generate Message" :loading="isLoading" @click="submit"/>
  </form>
</template>

<style scoped>
</style>
