import js from '@eslint/js';
import ts from 'typescript-eslint';
import vue from 'eslint-plugin-vue';
import vueParser from 'vue-eslint-parser';

export default [
    js.configs.recommended,
    ...ts.configs.recommended,
    {
        files: ['src/**/*.vue'],
        languageOptions: {
            parser: vueParser,
            parserOptions: {
                parser: ts.parser,
                sourceType: 'module',
            },
        },
        plugins: {vue},
        rules: {
            ...vue.configs['recommended'].rules,
            'vue/multi-word-component-names': 'off',
        },
    },
    {
        files: ['src/**/*.ts'],
        languageOptions: {
            parser: ts.parser,
        },
    },
    {
        ignores: ['dist/**', 'coverage/**', 'node_modules/**'],
    },
];
