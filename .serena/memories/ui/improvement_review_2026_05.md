# greetings-ui Improvement Review (May 2026)

Comprehensive code review of the Vue 3 frontend at `greetings-ui/`. No changes were made — this is analysis only.

---

## 1. Bugs (Things That Are Broken Today)

### 1.1 Hardcoded ID in `GreetingsRepository`
**File:** `greetings-ui/src/composables/GreetingsRepository.ts`

```ts
const message = new GreetingMessage("1", `Hello ${greeting.name}, happy ${greeting.type}!`);
```

Every greeting gets ID `"1"`. Creating two greetings in the same session means the second overwrites the first in the `Map`. Navigation to `/messages/1` always shows the last-created message regardless of what was requested.

### 1.2 `GreetingMessage` is missing the `type` field
**File:** `greetings-ui/src/views/Message.vue`

The template renders `{{ greetingMessage.type }}` and `data-cy="greeting-type-display"`, but the `GreetingMessage` class (in `src/models/GreetingMessage.ts`) only exposes `id` and `message`. The `type` getter does not exist — TypeScript should catch this at build time but does not because the object is typed as `GreetingMessage | undefined` and the ref is assigned from an in-memory object cast via the repository. Result: the "Current type:" line always renders blank.

### 1.3 Update button does nothing
**File:** `greetings-ui/src/views/Message.vue`

```html
<Button data-cy="update-greeting" label="Update"/>
```

There is no `@click` handler on this button. Clicking it does nothing. The `selectedType` ref is wired to the `Select`, but the update action is never connected.

### 1.4 Double-nested `options` in PrimeVue config
**File:** `greetings-ui/src/main.ts`

```ts
theme: {
    preset: MyPreset,
    options: {
        options: {          // <-- nested twice, outer `options` is wrong key
            darkModeSelector: false,
        }
    }
}
```

The correct PrimeVue 4 structure is `theme: { preset, options: { darkModeSelector } }`. The extra nesting means `darkModeSelector` is never applied.

### 1.5 `greeting-created` div is invisible
**File:** `greetings-ui/src/views/Message.vue`

```html
<div data-cy="greeting-created"></div>
```

The Cypress command `verifyGreetingCreated()` asserts `.should('be.visible')` on this element. An empty `<div>` with no content, no dimensions, and no styles is not visible in browsers — the assertion will fail unless the div carries meaningful content or dimensions.

---

## 2. Missing Implementations (Features Promised But Absent)

### 2.1 No API calls — entire app is in-memory
**File:** `greetings-ui/src/composables/GreetingsRepository.ts`

```ts
// TODO Call API
```

The backend (`greetings-service`) exposes a full REST API. The repository never calls it. All data is fabricated in-memory and lost on page refresh.

### 2.2 Stats page is a placeholder
**File:** `greetings-ui/src/views/Stats.vue`

```html
<p> Stats Component</p>
```

The BDD feature file `bdd/features/GreetingsStats.feature` describes 5 detailed scenarios (counters per type, per name, etc.) powered by `greetings-stat-service`. The Stats view is completely unimplemented.

### 2.3 No form validation
**File:** `greetings-ui/src/views/GreetingForm.vue`

A user can submit with an empty name or without selecting a type. The `submit()` function runs unconditionally, calling `EventType[type as keyof typeof EventType]` with an empty string, which produces `undefined` and would cause an API error once the real call is wired.

### 2.4 Update type list is hardcoded, not driven by enum
**File:** `greetings-ui/src/views/Message.vue`

```ts
:options="['birthday', 'anniversary', 'christmas']"
```

The creation form correctly derives options from `EventType`. The update dropdown hardcodes lowercase strings that don't match enum values, making selection-to-API mapping impossible.

---

## 3. Code Quality Issues

### 3.1 `console.log` in production code
**File:** `greetings-ui/src/views/GreetingForm.vue`

```ts
console.log(JSON.stringify(payload))
```

Debug output left in the `submit()` function.

### 3.2 Non-reactive state in `<script setup>`
**File:** `greetings-ui/src/views/GreetingForm.vue`

```ts
let name: string = ''
let type: string = ''
```

These are plain `let` variables, not `ref()`. They work by accident because the values are only read at click time, but Vue cannot track them reactively. If any template binding or computed property depended on them, it would not update. Convention is `ref('')`.

### 3.3 Unused `label` prop in `GreetingDropdown`
**File:** `greetings-ui/src/components/GreetingDropdown.vue`

The `label` prop is declared in `defineProps` but never rendered. The placeholder text is hardcoded as `"Select a type"` regardless of what the parent passes. The prop declaration creates a false API.

### 3.4 Hardcoded `id` in `GreetingInput`
**File:** `greetings-ui/src/components/GreetingInput.vue`

```html
<InputText id="name" .../>
<label for="name">
```

If `GreetingInput` is ever mounted more than once on a page, the duplicate `id="name"` violates HTML spec and breaks `<label for>` associations. The `id` should be derived from the `label` prop or injected as a prop.

### 3.5 `let` used with a `ref` (cosmetic but misleading)
**File:** `greetings-ui/src/components/GreetingDropdown.vue`

```ts
let selectedType = ref<string>();
```

`let` implies the variable itself may be reassigned. Vue refs should use `const`. This is a Vue style guide violation.

### 3.6 Inconsistent import extension usage
Some imports include `.ts`:
```ts
import {Greeting} from "../models/greeting.model.ts";
```
Others omit it:
```ts
import {greetingRepository} from "../composables/GreetingsRepository";
```
The project has `allowImportingTsExtensions: true` so both work, but mixing is inconsistent. Pick one convention.

### 3.7 `GreetingsRepository` is not a Vue composable
**Directory:** `greetings-ui/src/composables/`

Vue 3 composables are functions starting with `use` (e.g., `useGreeting()`). `GreetingsRepository` is a class with a singleton export. It belongs in `src/services/` or `src/api/`, not `src/composables/`. The naming misleads future developers about the intended pattern.

### 3.8 Event naming inconsistency
- `GreetingInput` emits `update` (generic, conflicts with Vue's own `update:modelValue` pattern)
- `GreetingDropdown` emits `typeSelected` (camelCase instead of kebab-case)

Vue convention: emit names should be kebab-case (`type-selected`) and avoid names that shadow framework internals.

### 3.9 File naming inconsistency in `src/models/`
- `GreetingMessage.ts` — PascalCase
- `event-type.model.ts` — kebab-case with suffix
- `greeting.model.ts` — kebab-case with suffix

Pick one convention. PrimeVue community convention for Vue 3 is PascalCase for classes/components, but whatever is chosen should be uniform.

---

## 4. Testing Issues

### 4.1 `Stats.spec.ts` tests a placeholder, not a feature
**File:** `greetings-ui/tests/unit/views/Stats.spec.ts`

Both tests assert that a `<p>` tag containing "Stats Component" exists. This only verifies that a placeholder renders. When Stats is implemented, these tests will be rewritten from scratch — they add no safety net.

### 4.2 `GreetingsRepository` tests assert fabricated, soon-to-be-removed behavior
**File:** `greetings-ui/tests/unit/composables/GreetingsRepository.spec.ts`

```ts
expect(message.message).toBe('Hello John, happy BIRTHDAY!');
```

This message format is from the in-memory TODO stub. Once real API calls are added, these test expectations will all break. Tests should instead mock `fetch`/axios and assert that the API is called with correct parameters.

### 4.3 No test for the Update button in `Message.spec.ts`
**File:** `greetings-ui/tests/unit/views/Message.spec.ts`

The Update button and type-change dropdown have zero unit test coverage.

### 4.4 `Message.spec.ts` mocks `useRoute` incompletely
```ts
vi.mocked(useRoute).mockReturnValue({ params: {} });
```
This doesn't return a full `RouteLocationNormalizedLoaded` object. In strict TypeScript mode this would be an error. Using `as any` or a proper stub is cleaner.

---

## 5. Configuration / Build Issues

### 5.1 No ESLint setup
`package.json` has no ESLint dependency or script. The `task_completion_guidelines` memory references `yarn lint` but this script doesn't exist and would fail. The tech stack memory lists ESLint as a tool but it is not configured.

### 5.2 No `lint` script in `package.json`
The scripts block only has: `dev`, `build`, `preview`, `test`, `test:watch`, `test:coverage`. No `lint` script.

### 5.3 No Vite proxy for API calls
**File:** `greetings-ui/vite.config.ts`

When real API calls are implemented, the dev server will make cross-origin requests to `greetings-service` (likely on a different port). A `server.proxy` configuration will be needed. Currently missing.

### 5.4 No environment variable configuration
API base URLs are not externalised. There are no `.env` files and no `import.meta.env` references. URLs will need to be configurable per environment (local dev, CI, staging) once API calls are added.

### 5.5 E2E package missing `test` and `test:open` scripts
**File:** `greeting-ui-e2e/package.json`

The Serena `suggested_commands` memory documents `yarn test` and `yarn test:open`, but only `cypress:run` and `cypress:open` are defined. Either the scripts or the documentation is wrong.

### 5.6 Coverage config excludes `*.pact.ts` files that don't exist
**File:** `greetings-ui/vitest.config.ts`

```ts
exclude: ['**/**.pact.ts']
```

There are no Pact contract test files in the project. The exclusion is a forward-looking placeholder but currently dead config.

---

## 6. Architecture / Feature Completeness

### 6.1 BDD scenarios have no matching backend steps for UI-E2E
The `GreetingsStats.feature` defines 5 `@e2e` scenarios that require `greetings-stat-service` to be running and seeded. The corresponding Cypress step definitions (`GreetingsStats.steps.ts`) are not implemented (file exists but presumably empty or minimal — the step file wasn't found in the step_definitions folder).

### 6.2 Message format is UI-fabricated, not backend-derived
The backend greeting service determines the message format (e.g. "Happy Birthday Anna !"). The UI generates its own format (`"Hello ${name}, happy ${type}!"`), which differs from what the BDD feature files expect (`"Happy Birthday Anna !"`). This will be a mismatch once the API is connected.

### 6.3 Router missing a 404 / catch-all route
**File:** `greetings-ui/src/router.ts`

Navigating to any unrecognised path renders nothing. A wildcard `{ path: '/:pathMatch(.*)*', redirect: '/form' }` route would improve robustness.

### 6.4 `MainLayout` uses `h-screen` causing overflow on tall content
**File:** `greetings-ui/src/layout/MainLayout.vue`

```html
<main class="p-4 flex flex-col justify-around h-screen w-full max-w-7xl mx-auto">
```

`h-screen` fixes the height to the viewport. On pages with content taller than the viewport (e.g., a future Stats table) this will hide overflow or cause layout issues. `min-h-screen` is more appropriate.

---

## Priority Order for Implementation

1. **Fix ID collision in repository** (bug, blocks all multi-greeting flows)
2. **Add `type` field to `GreetingMessage` model** (bug, broken UI today)
3. **Wire Update button** (bug, UI affordance does nothing)
4. **Fix PrimeVue double-nested options** (config bug)
5. **Implement API calls in `GreetingsRepository`** (core missing feature)
6. **Implement Stats view** (core missing feature)
7. **Add form validation** (UX, prevents bad API calls)
8. **Set up ESLint** (code quality tooling)
9. **Add Vite proxy + env vars** (needed for local API dev)
10. **Refactor composable → service, fix naming inconsistencies** (code health)
