import {pactWith} from "jest-pact";
import {GreetingsService} from "./greetings.service";
import {ConfigService} from "./config.service";
import {Matchers} from '@pact-foundation/pact';
import {TestBed} from "@angular/core/testing";
import {HttpClientModule} from "@angular/common/http";
import {Greeting} from "./greeting";
import {firstValueFrom} from "rxjs";

pactWith({
  consumer: 'greetings-ui',
  provider: 'greetings-service',
  spec: 2,
  pactfileWriteMode: 'overwrite'
}, provider => {
  let service: GreetingsService;

  beforeEach(() => {
    const configService: ConfigService = {
      getGreetingsAPIUrl: jest.fn(() => provider.mockService.baseUrl)
    }
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [
        {provide: ConfigService, useValue: configService}
      ]
    });
    service = TestBed.inject(GreetingsService)
  });

  describe('Create Greetings endpoint', () => {
    beforeEach(async () => {
      await provider.addInteraction({
        state: '',
        uponReceiving: 'A request for a new Greeting Message',
        withRequest: {
          path: '/rest/api/v1/greetings',
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Accept': Matchers.string('application/json')
          },
          body: {
            type: Matchers.term({
              generate: 'Christmas',
              matcher: '^Christmas|Anniversary|Birthday$'
            }),
            name: Matchers.string('Max Planck')
          }
        },
        willRespondWith: {
          status: 201,
          headers: {
            'access-control-expose-headers': 'Location',
            'Location': Matchers.term({
              generate: provider.mockService.baseUrl + '/rest/api/v1/greetings/f229a83e-fff8-450d-b557-552367a37391',
              matcher: '.+/rest/api/v1/greetings/[a-z0-9]{8}(-[a-z0-9]{4}){3}-[a-z0-9]{12}'
            }),
            'Content-Type': 'application/json'
          },
          body: {
            message: Matchers.term({
              generate: 'Merry Christmas Max Planck !',
              matcher: '.* (Christmas|Anniversary|Birthday) .* \!'
            })
          }
        }
      });
    });

    test('should create new greetings', async () => {
      const greeting: Greeting = await firstValueFrom(service.createNewGreeting('Birthday', 'Albert Einstein'))
      expect(greeting).toBeTruthy();
      expect(greeting.id).toMatch(/^[a-z0-9]{8}(-[a-z0-9]{4}){3}-[a-z0-9]{12}$/);
      expect(greeting.message).toBeTruthy();
    });
  });
});
