import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
    plugins: [vue(), tailwindcss()],
    server: {
        proxy: {
            '/api/greetings': {
                target: 'http://localhost:9080',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, '/rest/api/v1'),
            },
            '/api/stats': {
                target: 'http://localhost:9081',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, '/rest/api/v1'),
            },
        },
    },
})
