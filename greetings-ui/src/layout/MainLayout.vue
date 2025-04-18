<script lang="ts" setup>

</script>

<template>
  <nav class="main-nav">
    <div class="nav-container">
      <div class="nav-menu-container">
        <ul class="main-menu-list">
          <li class="menu-list-item">
            <RouterLink class="menu-item"
                        to="/form">
              Form (Home)
            </RouterLink>
          </li>
          <li class="menu-list-item">
            <RouterLink class="menu-item"
                        to="/stats">
              Stats
            </RouterLink>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <main class="main-content">
    <RouterView/>
  </main>
</template>

<style scoped>
.main-content {
  @apply flex flex-col justify-around w-screen h-screen block w-full max-w-screen-xl mx-auto
}

.main-nav {
  @apply block w-full max-w-screen-xl px-6 py-3 mx-auto text-white bg-white border shadow-md rounded-xl border-white/80 bg-opacity-80 backdrop-blur-2xl backdrop-saturate-200
}

.nav-container {
  @apply flex items-center justify-between text-blue-gray-900
}

.nav-menu-container {
  @apply hidden lg:block
}

.main-menu-list {
  @apply flex flex-col gap-2 my-2 lg:mb-0 lg:mt-0 lg:flex-row lg:items-center lg:gap-6
}

.menu-list-item {
  @apply block p-1 font-sans text-sm antialiased font-medium leading-normal text-blue-gray-900
}

.menu-item {
  @apply flex items-center transition-colors hover:text-blue-500
}
</style>