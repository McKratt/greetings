import {TestBed} from '@angular/core/testing';

import {GreetingsService} from './greetings.service';
import {ConfigService} from "./config.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {of} from "rxjs";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('GreetingsService', () => {
  let service: GreetingsService;

  describe('it', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      service = TestBed.inject(GreetingsService);
    });

    test('should be created', () => {
      expect(service).toBeTruthy();
    });
  });
  describe('ut', () => {
    const httpPostMethod = jest.fn()
    const configServiceMethod = jest.fn();
    const configService: ConfigService = {
      getGreetingsAPIUrl: configServiceMethod
    };

    const httpClient: Partial<HttpClient> = {
      post: httpPostMethod
    };
    beforeEach(() => {
      // @ts-ignore
      service = new GreetingsService(configService, httpClient);
    });
    test('should use config service and post', (done) => {
      const name: string = 'Marie Currie';
      const type: string = 'Anniversary';
      const id: string = '8b362bc0-27a8-4e24-a5ed-6ec07b8e5db0';
      const baseUrl: string = 'http://test.org';
      configServiceMethod.mockReturnValue(baseUrl);
      httpPostMethod.mockImplementation(() => {
        return of({
          body: {
            message: `Joyful ${type} ${name} !`
          },
          headers: new HttpHeaders({
            Location: `${baseUrl}/rest/api/v1/greetings/${id}`
          })
        })
      });
      try {
        service.createNewGreeting(type, name).subscribe(
          greeting => {
            expect(greeting).toBeTruthy();
            expect(greeting.id).toEqual(id);
            expect(greeting.message).toContain(name)
            expect(greeting.message).toContain(type)
            expect(httpPostMethod.mock.calls.length).toEqual(1)
            done();
          }
        );
      } catch (err) {
        done(err);
      }
    });
  });

});
