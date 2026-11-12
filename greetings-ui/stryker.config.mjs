// @ts-check
/** @type {import('@stryker-mutator/api/core').PartialStrykerOptions} */
const config = {
    packageManager: 'pnpm',
    testRunner: 'vitest',
    // pnpm's strict node_modules layout prevents Stryker's plugin auto-discovery
    plugins: ['@stryker-mutator/vitest-runner'],
    vitest: {
        configFile: 'vitest.config.ts',
    },
    coverageAnalysis: 'perTest',
    mutate: [
        'src/**/*.ts',
        'src/**/*.vue',
        '!src/main.ts',
        '!src/vite-env.d.ts',
        '!src/presets/**',
    ],
    reporters: ['html', 'json', 'clear-text', 'progress'],
    htmlReporter: {fileName: 'reports/mutation/mutation.html'},
    jsonReporter: {fileName: 'reports/mutation/mutation.json'},
    tempDirName: '.stryker-tmp',
    thresholds: {high: 80, low: 60, break: null},
};

export default config;
