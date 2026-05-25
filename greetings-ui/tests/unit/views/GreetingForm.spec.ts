// Mock dependencies before imports
vi.mock('../../../src/router', () => ({
    default: {push: vi.fn()}
}));

vi.mock('../../../src/services/greetings.service', () => ({
    greetingsService: {
        createGreeting: vi.fn().mockResolvedValue({id: 'uuid-1', message: 'Test', type: 'BIRTHDAY'})
    }
}));

import {beforeEach, describe, expect, it, vi} from 'vitest';
import {flushPromises, mount} from '@vue/test-utils';
import GreetingForm from '../../../src/views/GreetingForm.vue';
import GreetingInput from '../../../src/components/GreetingInput.vue';
import GreetingDropdown from '../../../src/components/GreetingDropdown.vue';
import {EventType} from '../../../src/models/event-type.model';
import {greetingsService} from '../../../src/services/greetings.service';
import router from '../../../src/router';

describe('GreetingForm', () => {
    beforeEach(() => vi.clearAllMocks());

    it('renders properly with input components', () => {
        const wrapper = mount(GreetingForm);
        expect(wrapper.findComponent(GreetingInput).exists()).toBe(true);
        expect(wrapper.findComponent(GreetingDropdown).exists()).toBe(true);
        expect(wrapper.find('button').exists()).toBe(true);
    });

    it('creates greeting and navigates on submit with valid data', async () => {
        const wrapper = mount(GreetingForm);
        await wrapper.findComponent(GreetingInput).vm.$emit('update', 'Jane Smith');
        await wrapper.findComponent(GreetingDropdown).vm.$emit('typeSelected', 'CHRISTMAS');
        await wrapper.find('[data-cy=create-greeting]').trigger('click');
        await flushPromises();

        expect(greetingsService.createGreeting).toHaveBeenCalledWith(
            expect.objectContaining({name: 'Jane Smith', type: EventType.CHRISTMAS})
        );
        expect(router.push).toHaveBeenCalledWith('/messages/uuid-1');
    });

    it('shows validation error when name is empty', async () => {
        const wrapper = mount(GreetingForm);
        await wrapper.findComponent(GreetingDropdown).vm.$emit('typeSelected', 'BIRTHDAY');
        await wrapper.find('[data-cy=create-greeting]').trigger('click');
        await flushPromises();

        expect(greetingsService.createGreeting).not.toHaveBeenCalled();
        expect(wrapper.text()).toContain('Name is required');
    });

    it('shows validation error when type is not selected', async () => {
        const wrapper = mount(GreetingForm);
        await wrapper.findComponent(GreetingInput).vm.$emit('update', 'Bob');
        await wrapper.find('[data-cy=create-greeting]').trigger('click');
        await flushPromises();

        expect(greetingsService.createGreeting).not.toHaveBeenCalled();
        expect(wrapper.text()).toContain('Please select a type');
    });

    it('shows API error when service throws', async () => {
        vi.mocked(greetingsService.createGreeting).mockRejectedValue(new Error('API error: 400'));
        const wrapper = mount(GreetingForm);
        await wrapper.findComponent(GreetingInput).vm.$emit('update', 'Bob');
        await wrapper.findComponent(GreetingDropdown).vm.$emit('typeSelected', 'BIRTHDAY');
        await wrapper.find('[data-cy=create-greeting]').trigger('click');
        await flushPromises();

        expect(wrapper.text()).toContain('Failed to create greeting');
    });
});
