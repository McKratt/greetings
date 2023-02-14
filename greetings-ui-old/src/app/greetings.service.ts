import {Injectable} from '@angular/core';
import {ConfigService} from "./config.service";
import {Observable} from "rxjs";
import {Greeting} from "./greeting";
import {HttpClient} from "@angular/common/http";

export type GreetingType = 'Anniversary' | 'Christmas' | 'Birthday';

@Injectable({
  providedIn: 'root'
})
export class GreetingsService {

  constructor(private configService: ConfigService, private http: HttpClient) {
  }

  createNewGreeting(type: GreetingType, name: string): Observable<Greeting> {
    return this.http.post<Greeting>(this.configService.getGreetingsAPIUrl() + '/rest/api/v1/greetings', {
      type: type,
      name: name
    });
  }
}
