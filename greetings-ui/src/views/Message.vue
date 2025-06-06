<script lang="ts" setup>
import {greetingRepository} from "../composables/GreetingsRepository";
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {GreetingMessage} from "../models/GreetingMessage";
import Message from 'primevue/message';
import Card from 'primevue/card';
import Select from 'primevue/select';
import Button from 'primevue/button';

const route = useRoute();
const greetingMessage = ref<GreetingMessage | undefined>(undefined);
const errorMessage = ref<string>('');
const selectedType = ref<string>();

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
  <Card class="w-full max-w-md mx-auto flex justify-center">
    <template #content>
      <div v-if="greetingMessage">
        <p class="text-center" data-cy="greeting-message">{{ greetingMessage.message }}</p>
        <div data-cy="greeting-created"></div>
        <div class="mt-4">
          <p>Current type: <span data-cy="greeting-type-display">{{ greetingMessage.type }}</span></p>
          <div class="flex gap-2 mt-2">
            <Select
                id="updateType"
                v-model="selectedType"
                :options="['birthday', 'anniversary', 'christmas']"
                class="w-full md:w-14rem"
                data-cy="update-greeting-type"
                placeholder="Select a new type"
            />
            <Button data-cy="update-greeting" label="Update"/>
          </div>
        </div>
      </div>
      <Message v-else-if="errorMessage" :closable="false" class="error-message" data-cy="error-message"
               severity="error">{{
          errorMessage
        }}
      </Message>
      <p v-else class="text-center">Loading...</p>
    </template>
  </Card>
</template>

<style scoped>
/* No custom styles needed - using PrimeVue components with default Tailwind CSS */
</style>
