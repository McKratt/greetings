import {beforeEach, describe, expect, it, vi} from 'vitest';
import {createGreeting, getGreetingById, updateGreeting} from '../../../src/api/greetings.api';

const mockFetch = vi.fn();
vi.stubGlobal('fetch', mockFetch);

describe('greetings.api', () => {
    beforeEach(() => vi.clearAllMocks());

    describe('createGreeting', () => {
        it('POSTs to /api/greetings and returns id and message', async () => {
            mockFetch.mockResolvedValue({
                ok: true,
                json: () => Promise.resolve({id: 'abc-123', message: 'Happy Birthday John !'}),
            });

            const result = await createGreeting({type: 'BIRTHDAY', name: 'John'});

            expect(mockFetch).toHaveBeenCalledWith('/api/greetings', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({type: 'BIRTHDAY', name: 'John'}),
            });
            expect(result).toEqual({id: 'abc-123', message: 'Happy Birthday John !'});
        });

        it('throws on non-ok response', async () => {
            mockFetch.mockResolvedValue({ok: false, status: 400});
            await expect(createGreeting({type: 'UNKNOWN', name: 'X'})).rejects.toThrow('API error: 400');
        });
    });

    describe('getGreetingById', () => {
        it('GETs /api/greetings/:id and returns type and name', async () => {
            mockFetch.mockResolvedValue({
                ok: true,
                status: 200,
                json: () => Promise.resolve({type: 'BIRTHDAY', name: 'John'}),
            });

            const result = await getGreetingById('abc-123');

            expect(mockFetch).toHaveBeenCalledWith('/api/greetings/abc-123');
            expect(result).toEqual({type: 'BIRTHDAY', name: 'John'});
        });

        it('returns null on 404', async () => {
            mockFetch.mockResolvedValue({ok: false, status: 404});
            const result = await getGreetingById('unknown');
            expect(result).toBeNull();
        });
    });

    describe('updateGreeting', () => {
        it('PUTs to /api/greetings/:id with newType and returns updated message', async () => {
            mockFetch.mockResolvedValue({
                ok: true,
                json: () => Promise.resolve({message: 'Joyful Anniversary John !'}),
            });

            const result = await updateGreeting('abc-123', {newType: 'ANNIVERSARY'});

            expect(mockFetch).toHaveBeenCalledWith('/api/greetings/abc-123', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({newType: 'ANNIVERSARY'}),
            });
            expect(result).toEqual({message: 'Joyful Anniversary John !'});
        });

        it('throws on non-ok response', async () => {
            mockFetch.mockResolvedValue({ok: false, status: 422});
            await expect(updateGreeting('id', {newType: 'CHRISTMAS'})).rejects.toThrow('API error: 422');
        });
    });
});
