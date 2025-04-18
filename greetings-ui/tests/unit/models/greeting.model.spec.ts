import {describe, expect, it} from 'vitest';
import {Greeting} from '../../../src/models/greeting.model';
import {EventType} from '../../../src/models/event-type.model';

describe('Greeting', () => {
    it('should create a greeting with type and name', () => {
        const greeting = new Greeting(EventType.BIRTHDAY, 'John');
        expect(greeting).toBeInstanceOf(Greeting);
    });

    it('should return the correct type via getter', () => {
        const greeting = new Greeting(EventType.CHRISTMAS, 'Jane');
        expect(greeting.type).toBe(EventType.CHRISTMAS);
    });

    it('should return the correct name via getter', () => {
        const greeting = new Greeting(EventType.ANNIVERSARY, 'Bob');
        expect(greeting.name).toBe('Bob');
    });

    it('should handle empty name', () => {
        const greeting = new Greeting(EventType.BIRTHDAY, '');
        expect(greeting.name).toBe('');
    });

    it('should work with all event types', () => {
        const types = [EventType.BIRTHDAY, EventType.CHRISTMAS, EventType.ANNIVERSARY];
        types.forEach(type => {
            const greeting = new Greeting(type, 'Test');
            expect(greeting.type).toBe(type);
        });
    });
});