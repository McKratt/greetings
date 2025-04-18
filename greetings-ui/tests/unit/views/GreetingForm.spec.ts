// Mock dependencies before imports
vi.mock('../../../src/router', () => ({
    default: {
        push: vi.fn()
    }
}));

// Mock the repository
vi.mock('../../../src/composables/GreetingsRepository', () => ({
    greetingRepository: {
        createGreeting: vi.fn().mockReturnValue({id: '1', message: 'Test message'})
    }
}));

import {beforeEach, describe, expect, it, vi} from 'vitest';
import {flushPromises, mount} from '@vue/test-utils';
import GreetingForm from '../../../src/views/GreetingForm.vue';
import GreetingInput from '../../../src/components/GreetingInput.vue';
import GreetingDropdown from '../../../src/components/GreetingDropdown.vue';
import {EventType} from '../../../src/models/event-type.model';
import {greetingRepository} from '../../../src/composables/GreetingsRepository';
import router from '../../../src/router';

describe('GreetingForm', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('renders properly with input components', () => {
        const wrapper = mount(GreetingForm);

        expect(wrapper.findComponent(GreetingInput).exists()).toBe(true);
        expect(wrapper.findComponent(GreetingDropdown).exists()).toBe(true);
        expect(wrapper.find('button').exists()).toBe(true);
        expect(wrapper.find('button').text()).toContain('Generate Message');
    });

    it('updates name when GreetingInput emits update event', async () => {
        const wrapper = mount(GreetingForm);

        const input = wrapper.findComponent(GreetingInput);
        await input.vm.$emit('update', 'John Doe');

        // We can't directly test the internal state, but we can test the behavior
        // by triggering the submit and checking if the repository was called with the correct name
        await wrapper.find('button').trigger('click');

        expect(greetingRepository.createGreeting).toHaveBeenCalledWith(
            expect.objectContaining({
                name: 'John Doe'
            })
        );
    });

    it('updates type when GreetingDropdown emits typeSelected event', async () => {
        const wrapper = mount(GreetingForm);

        const dropdown = wrapper.findComponent(GreetingDropdown);
        await dropdown.vm.$emit('typeSelected', 'BIRTHDAY');

        // Test behavior by submitting and checking repository call
        await wrapper.find('button').trigger('click');

        expect(greetingRepository.createGreeting).toHaveBeenCalledWith(
            expect.objectContaining({
                type: EventType.BIRTHDAY
            })
        );
    });

    it('creates greeting and navigates on submit', async () => {
        const wrapper = mount(GreetingForm);

        // Set up form values
        await wrapper.findComponent(GreetingInput).vm.$emit('update', 'Jane Smith');
        await wrapper.findComponent(GreetingDropdown).vm.$emit('typeSelected', 'CHRISTMAS');

        // Submit the form
        await wrapper.find('button').trigger('click');
        await flushPromises();

        // Check repository call
        expect(greetingRepository.createGreeting).toHaveBeenCalledWith(
            expect.objectContaining({
                name: 'Jane Smith',
                type: EventType.CHRISTMAS
            })
        );

        // Check router navigation
        expect(router.push).toHaveBeenCalledWith('/messages/1');
    });

    it('trims whitespace from name and type', async () => {
        const wrapper = mount(GreetingForm);

        // Set up form values with whitespace
        await wrapper.findComponent(GreetingInput).vm.$emit('update', '  Bob  ');
        await wrapper.findComponent(GreetingDropdown).vm.$emit('typeSelected', ' ANNIVERSARY ');

        // Submit the form
        await wrapper.find('button').trigger('click');

        // Check repository call with trimmed values
        expect(greetingRepository.createGreeting).toHaveBeenCalledWith(
            expect.objectContaining({
                name: 'Bob',
                type: EventType.ANNIVERSARY
            })
        );
    });
});
