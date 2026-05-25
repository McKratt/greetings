vi.mock('../../../src/api/stats.api', () => ({
    getStats: vi.fn(),
}));

import {beforeEach, describe, expect, it, vi} from 'vitest';
import {flushPromises, mount} from '@vue/test-utils';
import Stats from '../../../src/views/Stats.vue';
import {getStats} from '../../../src/api/stats.api';

describe('Stats', () => {
    beforeEach(() => vi.clearAllMocks());

    it('displays counters for each greeting type', async () => {
        vi.mocked(getStats).mockResolvedValue({
            counters: {BIRTHDAY: 10, ANNIVERSARY: 5, CHRISTMAS: 2}
        });
        const wrapper = mount(Stats);
        await flushPromises();

        expect(wrapper.text()).toContain('BIRTHDAY');
        expect(wrapper.text()).toContain('10');
        expect(wrapper.text()).toContain('ANNIVERSARY');
        expect(wrapper.text()).toContain('5');
        expect(wrapper.text()).toContain('CHRISTMAS');
        expect(wrapper.text()).toContain('2');
    });

    it('displays empty state when no stats available (204)', async () => {
        vi.mocked(getStats).mockResolvedValue(null);
        const wrapper = mount(Stats);
        await flushPromises();

        expect(wrapper.text()).toContain('No statistics available yet');
    });

    it('shows loading state before data arrives', () => {
        vi.mocked(getStats).mockReturnValue(new Promise(() => {}));
        const wrapper = mount(Stats);
        expect(wrapper.text()).toContain('Loading');
    });

    it('shows error when API call fails', async () => {
        vi.mocked(getStats).mockRejectedValue(new Error('API error: 500'));
        const wrapper = mount(Stats);
        await flushPromises();

        expect(wrapper.text()).toContain('Failed to load statistics');
    });
});
