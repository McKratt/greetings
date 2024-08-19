import {Greeting} from "../models/greeting.model.ts";
import {GreetingMessage} from "../models/GreetingMessage.ts";

export class GreetingsRepository {
    public createGreeting(greeting: Greeting): GreetingMessage {
        // TODO Call API
        return new GreetingMessage("1", "toto")
    }
}

export const greetingRepository = new GreetingsRepository()