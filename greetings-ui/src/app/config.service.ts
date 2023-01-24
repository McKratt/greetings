import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  public getGreetingsAPIUrl() : string {
    return 'http://localhost:9080'
  }
}
