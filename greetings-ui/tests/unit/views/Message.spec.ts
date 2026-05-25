// Mock dependencies before imports
vi.mock('vue-router', () => ({
    useRoute: vi.fn(() => ({params: {id: 'uuid-1'}}))
}));

vi.mock('../../../src/services/greetings.service', () => ({
    greetingsService: {
        getGreetingById: vi.fn(),
        updateGreeting: vi.fn(),
    }
}));

import {beforeEach, describe, expect, it, vi} from 'vitest';
import {flushPromises, mount} from '@vue/test-utils';
import MessageView from '../../../src/views/Message.vue';
import {greetingsService} from '../../../src/services/greetings.service';
import {useRoute} from 'vue-router';

describe('Message', () => {
    beforeEach(() => vi.clearAllMocks());

    it('displays the greeting message and type when found', async () => {
        vi.mocked(greetingsService.getGreetingById).mockResolvedValue({
            id: 'uuid-1', message: 'Happy Birthday John !', type: 'BIRTHDAY'
        });
        const wrapper = mount(MessageView);
        await flushPromises();

        expect(wrapper.find('[data-cy=greeting-message]').text()).toBe('Happy Birthday John !');
        expect(wrapper.find('[data-cy=greeting-type-display]').text()).toBe('BIRTHDAY');
    });

    it('displays error when message not found', async () => {
        vi.mocked(greetingsService.getGreetingById).mockResolvedValue(undefined);
        const wrapper = mount(MessageView);
        await flushPromises();

        expect(wrapper.find('[data-cy=error-message]').exists()).toBe(true);
        expect(wrapper.find('[data-cy=error-message]').text()).toContain('No message found with ID: uuid-1');
    });

    it('updates the displayed message when Update is clicked', async () => {
        vi.mocked(greetingsService.getGreetingById).mockResolvedValue({
            id: 'uuid-1', message: 'Happy Birthday John !', type: 'BIRTHDAY'
        });
        vi.mocked(greetingsService.updateGreeting).mockResolvedValue({
            id: 'uuid-1', message: 'Joyful Anniversary John !', type: 'ANNIVERSARY'
        });

        const wrapper = mount(MessageView);
        await flushPromises();

        const select = wrapper.findComponent({name: 'Select'});
        await select.vm.$emit('update:modelValue', 'ANNIVERSARY');
        await wrapper.find('[data-cy=update-greeting]').trigger('click');
        await flushPromises();

        expect(greetingsService.updateGreeting).toHaveBeenCalledWith('uuid-1', 'ANNIVERSARY');
        expect(wrapper.find('[data-cy=greeting-message]').text()).toBe('Joyful Anniversary John !');
    });

    it('shows error when Update fails (e.g. CHRISTMAS restriction)', async () => {
        vi.mocked(greetingsService.getGreetingById).mockResolvedValue({
            id: 'uuid-1', message: 'Merry Christmas John !', type: 'CHRISTMAS'
        });
        vi.mocked(greetingsService.updateGreeting).mockRejectedValue(new Error('API error: 422'));

        const wrapper = mount(MessageView);
        await flushPromises();

        const select = wrapper.findComponent({name: 'Select'});
        await select.vm.$emit('update:modelValue', 'BIRTHDAY');
        await wrapper.find('[data-cy=update-greeting]').trigger('click');
        await flushPromises();

        expect(wrapper.find('[data-cy=error-message]').exists()).toBe(true);
    });
});
