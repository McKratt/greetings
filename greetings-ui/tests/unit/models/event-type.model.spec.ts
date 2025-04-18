import {describe, expect, it} from 'vitest';
import {EventType} from '../../../src/models/event-type.model';

describe('EventType', () => {
    it('should have CHRISTMAS enum value', () => {
        expect(EventType.CHRISTMAS).toBe('CHRISTMAS');
    });

    it('should have BIRTHDAY enum value', () => {
        expect(EventType.BIRTHDAY).toBe('BIRTHDAY');
    });

    it('should have ANNIVERSARY enum value', () => {
        expect(EventType.ANNIVERSARY).toBe('ANNIVERSARY');
    });

    it('should have exactly 3 enum values', () => {
        const enumValues = Object.keys(EventType).filter(key => isNaN(Number(key)));
        expect(enumValues.length).toBe(3);
    });
});