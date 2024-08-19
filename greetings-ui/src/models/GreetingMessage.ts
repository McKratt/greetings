export class GreetingMessage {
    constructor(private _id: string, private _message: string) {
    }

    public get message() {
        return this._message;
    }

    public get id() {
        return this._id;
    }
}