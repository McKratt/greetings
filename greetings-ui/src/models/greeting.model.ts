import {EventType} from "./event-type.model.ts";

export class Greeting {
    constructor(private readonly _type: EventType, private readonly _name: string) {
    }

    public get type(): EventType {
        return this._type;
    }

    public get name(): string {
        return this._name;
    }
}
