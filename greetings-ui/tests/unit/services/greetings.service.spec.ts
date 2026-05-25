import {beforeEach, describe, expect, it, vi} from 'vitest';
import {GreetingsService} from '../../../src/services/greetings.service';
import * as greetingsApi from '../../../src/api/greetings.api';
import {Greeting} from '../../../src/models/greeting.model';
import {EventType} from '../../../src/models/event-type.model';
import {GreetingMessage} from '../../../src/models/GreetingMessage';

vi.mock('../../../src/api/greetings.api');

describe('GreetingsService', () => {
    let service: GreetingsService;

    beforeEach(() => {
        service = new GreetingsService();
        vi.clearAllMocks();
    });

    describe('createGreeting', () => {
        it('calls the API and returns a GreetingMessage with type', async () => {
            vi.mocked(greetingsApi.createGreeting).mockResolvedValue({
                id: 'uuid-1',
                message: 'Happy Birthday John !',
            });

            const greeting = new Greeting(EventType.BIRTHDAY, 'John');
            const result = await service.createGreeting(greeting);

            expect(greetingsApi.createGreeting).toHaveBeenCalledWith({
                type: 'BIRTHDAY',
                name: 'John',
            });
            expect(result).toBeInstanceOf(GreetingMessage);
            expect(result.id).toBe('uuid-1');
            expect(result.message).toBe('Happy Birthday John !');
            expect(result.type).toBe('BIRTHDAY');
        });
    });

    describe('getGreetingById', () => {
        it('returns cached message without calling the API', async () => {
            vi.mocked(greetingsApi.createGreeting).mockResolvedValue({
                id: 'uuid-1',
                message: 'Happy Birthday John !',
            });
            const greeting = new Greeting(EventType.BIRTHDAY, 'John');
            await service.createGreeting(greeting);

            const result = await service.getGreetingById('uuid-1');

            expect(greetingsApi.getGreetingById).not.toHaveBeenCalled();
            expect(result?.message).toBe('Happy Birthday John !');
        });

        it('fetches from API on cache miss and reconstructs message', async () => {
            vi.mocked(greetingsApi.getGreetingById).mockResolvedValue({
                type: 'ANNIVERSARY',
                name: 'Charles',
            });

            const result = await service.getGreetingById('uuid-2');

            expect(greetingsApi.getGreetingById).toHaveBeenCalledWith('uuid-2');
            expect(result?.message).toBe('Joyful Anniversary Charles !');
            expect(result?.type).toBe('ANNIVERSARY');
        });

        it('returns undefined when API returns null (not found)', async () => {
            vi.mocked(greetingsApi.getGreetingById).mockResolvedValue(null);
            const result = await service.getGreetingById('missing');
            expect(result).toBeUndefined();
        });
    });

    describe('updateGreeting', () => {
        it('calls the API and returns updated GreetingMessage', async () => {
            vi.mocked(greetingsApi.createGreeting).mockResolvedValue({
                id: 'uuid-1',
                message: 'Happy Birthday John !',
            });
            vi.mocked(greetingsApi.updateGreeting).mockResolvedValue({
                message: 'Joyful Anniversary John !',
            });
            const greeting = new Greeting(EventType.BIRTHDAY, 'John');
            await service.createGreeting(greeting);

            const result = await service.updateGreeting('uuid-1', 'ANNIVERSARY');

            expect(greetingsApi.updateGreeting).toHaveBeenCalledWith('uuid-1', {newType: 'ANNIVERSARY'});
            expect(result.message).toBe('Joyful Anniversary John !');
            expect(result.type).toBe('ANNIVERSARY');
        });
    });
});
