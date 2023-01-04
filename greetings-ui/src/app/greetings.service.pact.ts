import {pactWith} from "jest-pact";
import {GreetingsService} from "./greetings.service";
import {ConfigService} from "./config.service";
import {Matchers} from '@pact-foundation/pact';
import {TestBed} from "@angular/core/testing";
import {HttpClientModule} from "@angular/common/http";
import {firstValueFrom} from "rxjs";

pactWith({
  consumer: 'greetings-ui',
  provider: 'greetings-service',
  spec: 2,
  pactfileWriteMode: 'overwrite',
  cors: true
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
    beforeEach(() => {
      provider.addInteraction({
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
            'Content-Type': 'application/json'
          },
          body: {
            id: Matchers.uuid(),
            message: Matchers.term({
              generate: 'Merry Christmas Max Planck !',
              matcher: '.* (Christmas|Anniversary|Birthday) .* \!'
            })
          }
        }
      });
    });

    test('should create new greetings', (done) => {
      firstValueFrom(service.createNewGreeting('Birthday', 'Albert Einstein'))
        .then((greeting) => {
          expect(greeting).toBeTruthy();
          expect(greeting.id).toMatch(/^[a-z0-9]{8}(-[a-z0-9]{4}){3}-[a-z0-9]{12}$/);
          expect(greeting.message).toBeTruthy();
          done();
        });
    });
  });
});
