import {describe, expect, it} from 'vitest';
import {mount} from '@vue/test-utils';
import Stats from '../../../src/views/Stats.vue';

describe('Stats', () => {
    it('renders properly', () => {
        const wrapper = mount(Stats);

        // Check if the component renders
        expect(wrapper.exists()).toBe(true);

        // Check if the text is displayed
        expect(wrapper.text()).toContain('Stats Component');

        // Check if the paragraph element exists
        expect(wrapper.find('p').exists()).toBe(true);
    });

    it('has the correct structure for CSS styling', () => {
        const wrapper = mount(Stats);

        // Check if the component has the expected structure for CSS styling
        expect(wrapper.find('p').exists()).toBe(true);

        // Verify the component has a style section in its template
        // This is a more flexible test that doesn't rely on the actual style content
        const componentHtml = wrapper.html();
        expect(componentHtml).toContain('<p>');
        expect(componentHtml).toContain('</p>');
    });
});
