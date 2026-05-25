import {createGreeting as apiCreate, getGreetingById as apiGet, updateGreeting as apiUpdate} from '../api/greetings.api';
import {GreetingMessage} from '../models/GreetingMessage';
import {Greeting} from '../models/greeting.model';
import {formatMessage} from '../utils/message-format';

export class GreetingsService {
    private cache: Map<string, GreetingMessage> = new Map();

    public async createGreeting(greeting: Greeting): Promise<GreetingMessage> {
        const data = await apiCreate({type: greeting.type as string, name: greeting.name});
        const message = new GreetingMessage(data.id, data.message, greeting.type as string);
        this.cache.set(message.id, message);
        return message;
    }

    public async getGreetingById(id: string): Promise<GreetingMessage | undefined> {
        if (this.cache.has(id)) return this.cache.get(id);
        const data = await apiGet(id);
        if (!data) return undefined;
        const message = new GreetingMessage(id, formatMessage(data.type, data.name), data.type);
        this.cache.set(id, message);
        return message;
    }

    public async updateGreeting(id: string, newType: string): Promise<GreetingMessage> {
        const data = await apiUpdate(id, {newType});
        const updated = new GreetingMessage(id, data.message, newType);
        this.cache.set(id, updated);
        return updated;
    }
}

export const greetingsService = new GreetingsService();
