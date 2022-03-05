import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor() { }

  public getGreetingsAPIUrl() : string {
    return 'http://localhost:9080'
  }
}
