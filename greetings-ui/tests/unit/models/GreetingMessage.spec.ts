import {describe, expect, it} from 'vitest';
import {GreetingMessage} from '../../../src/models/GreetingMessage';

describe('GreetingMessage', () => {
    it('should create a greeting message with id and message', () => {
        const greetingMessage = new GreetingMessage('1', 'Hello, World!');
        expect(greetingMessage).toBeInstanceOf(GreetingMessage);
    });

    it('should return the correct id via getter', () => {
        const greetingMessage = new GreetingMessage('123', 'Test message');
        expect(greetingMessage.id).toBe('123');
    });

    it('should return the correct message via getter', () => {
        const greetingMessage = new GreetingMessage('456', 'Hello, Jane!');
        expect(greetingMessage.message).toBe('Hello, Jane!');
    });

    it('should handle empty message', () => {
        const greetingMessage = new GreetingMessage('789', '');
        expect(greetingMessage.message).toBe('');
    });

    it('should handle numeric id as string', () => {
        const greetingMessage = new GreetingMessage('42', 'Answer to everything');
        expect(greetingMessage.id).toBe('42');
    });
});