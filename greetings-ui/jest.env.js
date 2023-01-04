// Fix an issue starting Jest 27 with Pact
global.setImmediate = jest.useRealTimers;
