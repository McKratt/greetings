export class GreetingMessage {
    constructor(private _id: string, private _message: string, private _type: string) {
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