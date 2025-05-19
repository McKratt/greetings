import {describe, expect, it} from 'vitest';
import {mount} from '@vue/test-utils';
import GreetingDropdown from '../../../src/components/GreetingDropdown.vue';

describe('GreetingDropdown', () => {
    const testValues = ['Option1', 'Option2', 'Option3'];

    it('renders properly with the provided values', () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Test Dropdown',
                values: testValues
            }
        });

        // Check if the Select component exists
        expect(wrapper.findComponent({name: 'Select'}).exists()).toBe(true);

        // Check if the placeholder is set correctly
        expect(wrapper.text()).toContain('Select a type');

        // Check if the values prop is passed correctly
        const selectComponent = wrapper.findComponent({name: 'Select'});
        expect(selectComponent.props('options')).toEqual(testValues);
    });

    it('emits typeSelected event when model value changes', async () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Event Type',
                values: testValues
            }
        });

        // Find the Select component and trigger a model update
        const select = wrapper.findComponent({name: 'Select'});
        await select.vm.$emit('update:modelValue', 'Option2');

        // Trigger the watch function
        await wrapper.vm.$nextTick();

        // Check if the event was emitted with the correct value
        expect(wrapper.emitted()).toHaveProperty('typeSelected');
        expect(wrapper.emitted('typeSelected')?.[0]).toEqual(['Option2']);
    });

    it('updates the model value correctly', async () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Category',
                values: testValues
            }
        });

        // Find the Select component and trigger a model update
        const select = wrapper.findComponent({name: 'Select'});
        await select.vm.$emit('update:modelValue', 'Option3');

        // Trigger the watch function
        await wrapper.vm.$nextTick();

        // Check if the Select component received the correct value
        expect(select.props('modelValue')).toBe('Option3');
    });

    it('handles empty values array', () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Empty Dropdown',
                values: []
            }
        });

        // Check if the Select component exists
        expect(wrapper.findComponent({name: 'Select'}).exists()).toBe(true);

        // Check if the values prop is passed correctly
        const selectComponent = wrapper.findComponent({name: 'Select'});
        expect(selectComponent.props('options')).toEqual([]);
    });
});
