import {config} from '@vue/test-utils';
import PrimeVue from 'primevue/config';
import {vi} from 'vitest';
import {createRouter, createWebHistory} from 'vue-router';

// Mock window.matchMedia - required for PrimeVue components
Object.defineProperty(window, 'matchMedia', {
    writable: true,
    value: vi.fn().mockImplementation(query => ({
        matches: false,
        media: query,
        onchange: null,
        addListener: vi.fn(), // deprecated
        removeListener: vi.fn(), // deprecated
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
    })),
});

// Create a mock router
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: '/', component: {template: '<div>Home</div>'}},
        {path: '/form', component: {template: '<div>Form</div>'}},
        {path: '/message/:id', component: {template: '<div>Message</div>'}, props: true},
        {path: '/stats', component: {template: '<div>Stats</div>'}}
    ]
});

// Setup PrimeVue and router for tests
config.global.plugins = [
    [PrimeVue, {
        theme: {
            options: {
                darkModeSelector: false,
            }
        }
    }],
    [router]
];
