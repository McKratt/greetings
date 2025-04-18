import {describe, expect, it} from 'vitest';
import {mount} from '@vue/test-utils';
import GreetingDropdown from '../../../src/components/GreetingDropdown.vue';

describe('GreetingDropdown', () => {
    const testValues = ['Option1', 'Option2', 'Option3'];

    it('renders properly with the provided label and values', () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Test Dropdown',
                values: testValues
            }
        });

        expect(wrapper.text()).toContain('Test Dropdown');
        expect(wrapper.find('select').exists()).toBe(true);

        // Check if all options are rendered
        const options = wrapper.findAll('option');
        expect(options.length).toBe(testValues.length);

        // Check option text content
        options.forEach((option, index) => {
            expect(option.text()).toBe(testValues[index]);
        });
    });

    it('emits typeSelected event when dropdown value changes', async () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Event Type',
                values: testValues
            }
        });

        const select = wrapper.find('select');
        await select.setValue('Option2');

        expect(wrapper.emitted()).toHaveProperty('typeSelected');
        expect(wrapper.emitted('typeSelected')?.[0]).toEqual(['Option2']);
    });

    it('updates the select value when v-model changes', async () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Category',
                values: testValues
            }
        });

        const select = wrapper.find('select');
        await select.setValue('Option3');

        expect((select.element as HTMLSelectElement).value).toBe('Option3');
    });

    it('handles empty values array', () => {
        const wrapper = mount(GreetingDropdown, {
            props: {
                label: 'Empty Dropdown',
                values: []
            }
        });

        const options = wrapper.findAll('option');
        expect(options.length).toBe(0);
    });
});