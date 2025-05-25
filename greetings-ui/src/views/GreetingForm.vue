<script lang="ts" setup>
import GreetingInput from "../components/GreetingInput.vue";
import {Greeting} from "../models/greeting.model.ts";
import {EventType} from "../models/event-type.model.ts";
import GreetingDropdown from "../components/GreetingDropdown.vue";
import {greetingRepository} from "../composables/GreetingsRepository.ts";
import router from "../router.ts";
import Button from 'primevue/button';

let name: string = '';
let type: string = '';

const types: string[] = Object.values(EventType) as string[]

function updateName(value: string): void {
  name = value.trim()
}

function updateType(value: string): void {
  type = value.trim();
}

function submit(): void {
  const payload: Greeting = new Greeting(EventType[type as keyof typeof EventType], name)
  console.log(JSON.stringify(payload))
  let message = greetingRepository.createGreeting(payload)
  router.push(`/messages/${message.id}`)
}
</script>

<template>
  <form class="flex flex-col gap-4 w-full max-w-md mx-auto">
    <GreetingInput label="Name" @update="updateName"/>
    <GreetingDropdown :values="types" label="Choose a type" @typeSelected="updateType"/>
    <Button label="Generate Message" @click="submit"/>
  </form>
</template>

<style scoped>
/* No custom styles needed - using PrimeVue components with default Tailwind CSS */
</style>
