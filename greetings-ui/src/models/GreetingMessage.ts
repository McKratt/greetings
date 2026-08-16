export class GreetingMessage {
    constructor(private readonly _id: string, private readonly _message: string, private readonly _type: string) {
    }

    public get message() {
        return this._message;
    }

    public get id() {
        return this._id;
    }

    public get type() {
        return this._type;
    }
}