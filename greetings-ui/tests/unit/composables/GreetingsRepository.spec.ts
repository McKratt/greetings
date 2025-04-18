import {beforeEach, describe, expect, it} from 'vitest';
import {GreetingsRepository} from '../../../src/composables/GreetingsRepository';
import {Greeting} from '../../../src/models/greeting.model';
import {EventType} from '../../../src/models/event-type.model';
import {GreetingMessage} from '../../../src/models/GreetingMessage';

describe('GreetingsRepository', () => {
    let repository: GreetingsRepository;
    let greeting: Greeting;

    beforeEach(() => {
        repository = new GreetingsRepository();
        greeting = new Greeting(EventType.BIRTHDAY, 'John');
    });

    it('should create a greeting message', () => {
        const message = repository.createGreeting(greeting);
        expect(message).toBeInstanceOf(GreetingMessage);
        expect(message.id).toBe('1');
        expect(message.message).toBe('Hello John, happy BIRTHDAY!');
    });

    it('should store the created message', () => {
        const message = repository.createGreeting(greeting);
        const retrievedMessage = repository.getGreetingById(message.id);
        expect(retrievedMessage).toBe(message);
    });

    it('should return undefined for non-existent message id', () => {
        const retrievedMessage = repository.getGreetingById('non-existent-id');
        expect(retrievedMessage).toBeUndefined();
    });

    it('should create different messages for different greetings', () => {
        const birthdayGreeting = new Greeting(EventType.BIRTHDAY, 'John');
        const christmasGreeting = new Greeting(EventType.CHRISTMAS, 'Jane');

        const birthdayMessage = repository.createGreeting(birthdayGreeting);
        const christmasMessage = repository.createGreeting(christmasGreeting);

        expect(birthdayMessage.message).toBe('Hello John, happy BIRTHDAY!');
        expect(christmasMessage.message).toBe('Hello Jane, happy CHRISTMAS!');
    });
});