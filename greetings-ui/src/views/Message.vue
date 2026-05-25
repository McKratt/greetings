<script lang="ts" setup>
import {greetingsService} from "../services/greetings.service.ts";
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {GreetingMessage} from "../models/GreetingMessage.ts";
import {EventType} from "../models/event-type.model.ts";
import MessageComponent from 'primevue/message';
import Card from 'primevue/card';
import Select from 'primevue/select';
import Button from 'primevue/button';

const route = useRoute();
const greetingMessage = ref<GreetingMessage | undefined>(undefined);
const errorMessage = ref('');
const selectedType = ref<string>();
const isUpdating = ref(false);

const typeOptions: string[] = Object.values(EventType) as string[];

onMounted(async () => {
  const id = route.params.id as string;
  if (!id) {
    errorMessage.value = 'No message ID provided';
    return;
  }
  try {
    const message = await greetingsService.getGreetingById(id);
    if (message) {
      greetingMessage.value = message;
    } else {
      errorMessage.value = `No message found with ID: ${id}`;
    }
  } catch {
    errorMessage.value = `Failed to load greeting with ID: ${id}`;
  }
});

async function handleUpdate(): Promise<void> {
  if (!selectedType.value || !greetingMessage.value) return;
  errorMessage.value = '';
  isUpdating.value = true;
  try {
    const updated = await greetingsService.updateGreeting(greetingMessage.value.id, selectedType.value);
    greetingMessage.value = updated;
    selectedType.value = undefined;
  } catch {
    errorMessage.value = 'Failed to update greeting. This type may not support updates.';
  } finally {
    isUpdating.value = false;
  }
}
</script>

<template>
  <Card class="w-full max-w-md mx-auto flex justify-center">
    <template #content>
      <div v-if="greetingMessage">
        <p class="text-center" data-cy="greeting-message">{{ greetingMessage.message }}</p>
        <div class="mt-2 text-sm text-green-600 font-medium" data-cy="greeting-created">
          ✓ Greeting created
        </div>
        <div class="mt-4">
          <p>Current type: <span data-cy="greeting-type-display">{{ greetingMessage.type }}</span></p>
          <div class="flex gap-2 mt-2">
            <Select
                id="updateType"
                v-model="selectedType"
                :options="typeOptions"
                class="w-full md:w-14rem"
                data-cy="update-greeting-type"
                placeholder="Select a new type"
            />
            <Button
                data-cy="update-greeting"
                label="Update"
                :loading="isUpdating"
                :disabled="!selectedType"
                @click="handleUpdate"
            />
          </div>
        </div>
        <MessageComponent
            v-if="errorMessage"
            :closable="false"
            class="error-message mt-2"
            data-cy="error-message"
            severity="error"
        >{{ errorMessage }}</MessageComponent>
      </div>
      <MessageComponent
          v-else-if="errorMessage"
          :closable="false"
          class="error-message"
          data-cy="error-message"
          severity="error"
      >{{ errorMessage }}</MessageComponent>
      <p v-else class="text-center">Loading...</p>
    </template>
  </Card>
</template>

<style scoped>
</style>
