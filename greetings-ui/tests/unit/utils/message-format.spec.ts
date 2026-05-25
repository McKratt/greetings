import {describe, expect, it} from 'vitest';
import {formatMessage} from '../../../src/utils/message-format';

describe('formatMessage', () => {
    it('formats BIRTHDAY message', () => {
        expect(formatMessage('BIRTHDAY', 'Anna')).toBe('Happy Birthday Anna !');
    });

    it('formats ANNIVERSARY message', () => {
        expect(formatMessage('ANNIVERSARY', 'Charles')).toBe('Joyful Anniversary Charles !');
    });

    it('formats CHRISTMAS message', () => {
        expect(formatMessage('CHRISTMAS', 'Leslie')).toBe('Merry Christmas Leslie !');
    });

    it('throws on unknown type', () => {
        expect(() => formatMessage('UNKNOWN', 'X')).toThrow('Unknown greeting type: UNKNOWN');
    });
});
