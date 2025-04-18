import {Greeting} from "../models/greeting.model.ts";
import {GreetingMessage} from "../models/GreetingMessage.ts";

export class GreetingsRepository {
    // Store messages in memory for demo purposes
    private messages: Map<string, GreetingMessage> = new Map();

    public createGreeting(greeting: Greeting): GreetingMessage {
        // TODO Call API
        const message = new GreetingMessage("1", `Hello ${greeting.name}, happy ${greeting.type}!`);
        this.messages.set(message.id, message);
        return message;
    }

    public getGreetingById(id: string): GreetingMessage | undefined {
        return this.messages.get(id);
    }
}

export const greetingRepository = new GreetingsRepository()
