import {defineConfig} from 'vitest/config';
import vue from '@vitejs/plugin-vue';
import {fileURLToPath} from 'url';

export default defineConfig({
    plugins: [vue()],
    test: {
        globals: true,
        environment: 'jsdom',
        include: ['tests/**/*.spec.ts'],
    },
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url)),
        },
    },
});