<script setup lang="ts">
import GreetingInput from "../components/GreetingInput.vue";
import {Greeting} from "../models/greeting.model.ts";
import {EventType} from "../models/event-type.model.ts";
import GreetingDropdown from "../components/GreetingDropdown.vue";

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
}
</script>

<template>
  <form class="main-form">
    <GreetingInput label="Name" @update="updateName"/>
    <GreetingDropdown :values="types" label="Choose a type" @typeSelected="updateType"/>
    <button class="form-button" type="button" @click="submit">
      Generate Message
    </button>
  </form>
</template>

<style scoped>
.main-form {
  @apply w-72 flex flex-col space-y-4 mx-auto
}

.form-button {
  @apply align-middle select-none font-sans font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-3 px-6 rounded-lg bg-gray-900 text-white shadow-md shadow-gray-900/10 hover:shadow-lg hover:shadow-gray-900/20 focus:opacity-[0.85] focus:shadow-none active:opacity-[0.85] active:shadow-none
}
</style>