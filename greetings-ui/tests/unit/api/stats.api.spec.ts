import {beforeEach, describe, expect, it, vi} from 'vitest';
import {getStats} from '../../../src/api/stats.api';

const mockFetch = vi.fn();
vi.stubGlobal('fetch', mockFetch);

describe('stats.api', () => {
    beforeEach(() => vi.clearAllMocks());

    it('GETs /api/stats and returns counters', async () => {
        mockFetch.mockResolvedValue({
            ok: true,
            status: 200,
            json: () => Promise.resolve({counters: {BIRTHDAY: 5, ANNIVERSARY: 3}}),
        });

        const result = await getStats();

        expect(mockFetch).toHaveBeenCalledWith('/api/stats', {
            headers: {'Accept': 'application/json'},
        });
        expect(result).toEqual({counters: {BIRTHDAY: 5, ANNIVERSARY: 3}});
    });

    it('returns null on 204 No Content', async () => {
        mockFetch.mockResolvedValue({ok: true, status: 204});
        const result = await getStats();
        expect(result).toBeNull();
    });

    it('throws on non-ok response', async () => {
        mockFetch.mockResolvedValue({ok: false, status: 500});
        await expect(getStats()).rejects.toThrow('API error: 500');
    });
});
