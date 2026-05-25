export function formatMessage(type: string, name: string): string {
    switch (type.toUpperCase()) {
        case 'BIRTHDAY': return `Happy Birthday ${name} !`;
        case 'ANNIVERSARY': return `Joyful Anniversary ${name} !`;
        case 'CHRISTMAS': return `Merry Christmas ${name} !`;
        default: throw new Error(`Unknown greeting type: ${type}`);
    }
}
