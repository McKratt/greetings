import {Injectable} from '@angular/core';
import {ConfigService} from "./config.service";
import {map, Observable} from "rxjs";
import {Greeting} from "./greeting";
import {HttpClient} from "@angular/common/http";

interface GreetingResponse {
  message: string
}

@Injectable({
  providedIn: 'root'
})
export class GreetingsService {

  constructor(private configService: ConfigService, private http: HttpClient) {
  }

// TODO make type more "enumish"
  createNewGreeting(type: string, name: string): Observable<Greeting> {
    return this.http.post<GreetingResponse>(this.configService.getGreetingsAPIUrl() + '/rest/api/v1/greetings', {
      type: type,
      name: name
    }, {observe: "response"}).pipe(
      map(response => {
        if (!response) {
          throw new Error('Response is null or empty !')
        }
        if (!response.body) {
          throw new Error('Body is null or empty !')
        }
        return {
          message: response.body.message,
          id: this.extractIdFromLocationHeader(response.headers.get('location'))
        };
      })
    );

  }

  private extractIdFromLocationHeader(headerValue: string | null): string {
    if (!headerValue) {
      throw new Error('The Location header has not been found !')
    }
    const matches = headerValue.match(/[a-z0-9]{8}(-[a-z0-9]{4}){3}-[a-z0-9]{12}/g)
    if (matches) {
      return matches[0];
    } else {
      throw new Error(`Id not found in Location : [${headerValue}] !`)
    }
  }
}
