module.exports = {
  preset: 'jest-preset-angular',
  setupFilesAfterEnv: ['<rootDir>/setup-jest.ts', '<rootDir>/jest.env.js',],
  globalSetup: 'jest-preset-angular/global-setup',
};
