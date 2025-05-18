import {describe, expect, it} from 'vitest';
import {mount} from '@vue/test-utils';
import MainLayout from '../../../src/layout/MainLayout.vue';

// Create mock components for RouterLink and RouterView
const RouterLink = {
    name: 'RouterLink',
    props: ['to'],
    template: '<a :href="to"><slot></slot></a>'
};

const RouterView = {
    name: 'RouterView',
    template: '<div class="router-view"><slot></slot></div>'
};

describe('MainLayout', () => {
    it('renders properly', () => {
        const wrapper = mount(MainLayout, {
            global: {
                components: {
                    RouterView,
                    RouterLink
                }
            }
        });

        // Check if the component renders
        expect(wrapper.exists()).toBe(true);

        // Check if the navigation menu exists
        expect(wrapper.find('nav').exists()).toBe(true);

        // Check if the main content area exists
        expect(wrapper.find('main').exists()).toBe(true);

        // Check if the RouterView exists
        expect(wrapper.find('.router-view').exists()).toBe(true);
    });

    it('contains the correct navigation links', () => {
        const wrapper = mount(MainLayout, {
            global: {
                components: {
                    RouterView,
                    RouterLink
                }
            }
        });

        // Check if the navigation menu contains the expected links
        const links = wrapper.findAll('a');
        expect(links.length).toBe(2);

        // Check if the first link is to the Form page
        expect(links[0].attributes('href')).toBe('/form');
        expect(links[0].text()).toContain('Form');

        // Check if the second link is to the Stats page
        expect(links[1].attributes('href')).toBe('/stats');
        expect(links[1].text()).toContain('Stats');
    });

    it('has the correct CSS classes', () => {
        const wrapper = mount(MainLayout, {
            global: {
                components: {
                    RouterView,
                    RouterLink
                }
            }
        });

        // Check if the navigation menu has the correct CSS classes
        expect(wrapper.find('nav').classes()).toContain('main-nav');

        // Check if the main content area has the correct CSS classes
        expect(wrapper.find('main').classes()).toContain('main-content');

        // Check if the navigation menu container has the correct CSS classes
        expect(wrapper.find('.nav-container').exists()).toBe(true);

        // Check if the navigation menu list has the correct CSS classes
        expect(wrapper.find('.main-menu-list').exists()).toBe(true);
    });
});
