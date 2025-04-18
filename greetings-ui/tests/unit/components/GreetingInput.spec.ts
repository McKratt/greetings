import {describe, expect, it} from 'vitest';
import {mount} from '@vue/test-utils';
import GreetingInput from '../../../src/components/GreetingInput.vue';

describe('GreetingInput', () => {
    it('renders properly with the provided label', () => {
        const wrapper = mount(GreetingInput, {
            props: {
                label: 'Test Label'
            }
        });

        expect(wrapper.text()).toContain('Test Label');
        expect(wrapper.find('input').exists()).toBe(true);
    });

    it('emits update event when input value changes', async () => {
        const wrapper = mount(GreetingInput, {
            props: {
                label: 'Name'
            }
        });

        const input = wrapper.find('input');
        await input.setValue('John Doe');

        expect(wrapper.emitted()).toHaveProperty('update');
        expect(wrapper.emitted('update')?.[0]).toEqual(['John Doe']);
    });

    it('updates the input value when v-model changes', async () => {
        const wrapper = mount(GreetingInput, {
            props: {
                label: 'Email'
            }
        });

        const input = wrapper.find('input');
        await input.setValue('test@example.com');

        expect((input.element as HTMLInputElement).value).toBe('test@example.com');
    });

    it('handles empty input values', async () => {
        const wrapper = mount(GreetingInput, {
            props: {
                label: 'Address'
            }
        });

        const input = wrapper.find('input');
        await input.setValue('');

        expect(wrapper.emitted('update')?.[0]).toEqual(['']);
    });
});