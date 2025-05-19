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

        // Check if the Menubar component exists
        expect(wrapper.findComponent({name: 'Menubar'}).exists()).toBe(true);

        // Check if the main content area exists
        expect(wrapper.find('main').exists()).toBe(true);

        // Check if the RouterView exists
        expect(wrapper.findComponent({name: 'RouterView'}).exists()).toBe(true);
    });

    it('contains the correct navigation items', () => {
        const wrapper = mount(MainLayout, {
            global: {
                components: {
                    RouterView,
                    RouterLink
                }
            }
        });

        // Check if the component has the correct items in its data
        const vm = wrapper.vm as any;
        expect(vm.items.length).toBe(2);

        // Check if the first item is for the Form page
        expect(vm.items[0].route).toBe('/form');
        expect(vm.items[0].label).toContain('Form');

        // Check if the second item is for the Stats page
        expect(vm.items[1].route).toBe('/stats');
        expect(vm.items[1].label).toContain('Stats');
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

        // Check if the main content area has the correct classes
        const main = wrapper.find('main');
        expect(main.exists()).toBe(true);
        expect(main.classes()).toContain('p-4');
        expect(main.classes()).toContain('flex');
        expect(main.classes()).toContain('flex-col');
    });
});
