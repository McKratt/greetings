// Mock dependencies before imports
vi.mock('vue-router', () => ({
    useRoute: vi.fn(() => ({
        params: {
            id: '1'
        }
    }))
}));

// Mock the repository
vi.mock('../../../src/composables/GreetingsRepository', () => ({
    greetingRepository: {
        getGreetingById: vi.fn()
    }
}));

import {beforeEach, describe, expect, it, vi} from 'vitest';
import {flushPromises, mount} from '@vue/test-utils';
import Message from '../../../src/views/Message.vue';
import {greetingRepository} from '../../../src/composables/GreetingsRepository';
import {useRoute} from 'vue-router';

describe('Message', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('displays the greeting message when found', async () => {
        // Setup mock to return a message
        const testMessage = {id: '1', message: 'Hello John, happy BIRTHDAY!'};
        vi.mocked(greetingRepository.getGreetingById).mockReturnValue(testMessage);

        const wrapper = mount(Message);
        await flushPromises();

        expect(greetingRepository.getGreetingById).toHaveBeenCalledWith('1');
        expect(wrapper.text()).toContain('Hello John, happy BIRTHDAY!');
        expect(wrapper.find('.error-message').exists()).toBe(false);
    });

    it('displays an error message when message not found', async () => {
        // Setup mock to return undefined (message not found)
        vi.mocked(greetingRepository.getGreetingById).mockReturnValue(undefined);

        const wrapper = mount(Message);
        await flushPromises();

        expect(greetingRepository.getGreetingById).toHaveBeenCalledWith('1');
        expect(wrapper.find('.error-message').exists()).toBe(true);
        expect(wrapper.find('.error-message').text()).toContain('No message found with ID: 1');
    });

    it('displays an error message when no ID provided', async () => {
        // Setup mock to return no ID
        vi.mocked(useRoute).mockReturnValue({
            params: {}
        });

        const wrapper = mount(Message);
        await flushPromises();

        expect(greetingRepository.getGreetingById).not.toHaveBeenCalled();
        expect(wrapper.find('.error-message').exists()).toBe(true);
        expect(wrapper.find('.error-message').text()).toContain('No message ID provided');
    });

    it('applies error styling to error messages', async () => {
        // Setup mock to return undefined (message not found)
        vi.mocked(greetingRepository.getGreetingById).mockReturnValue(undefined);

        const wrapper = mount(Message);
        await flushPromises();

        const errorElement = wrapper.find('.error-message');
        expect(errorElement.exists()).toBe(true);

        // Check that the error styling classes are applied
        // Note: We can't directly test the applied CSS, but we can check the class is present
        expect(errorElement.classes()).toContain('error-message');
    });

    it('shows loading indicator initially', () => {
        // Don't resolve the repository promise yet
        vi.mocked(greetingRepository.getGreetingById).mockReturnValue(undefined);

        // Mount without waiting for onMounted to complete
        const wrapper = mount(Message, {
            shallow: true // Use shallow mount to prevent onMounted from running immediately
        });

        // Before onMounted completes, it should show loading
        expect(wrapper.text()).toContain('Loading');
    });
});
