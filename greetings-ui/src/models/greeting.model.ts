import {EventType} from "./event-type.model.ts";

export class Greeting {
    constructor(private _type: EventType, private _name: string) {
    }
}