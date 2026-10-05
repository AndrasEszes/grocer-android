# Grocer

A small grocery shopping app (Jetpack Compose) with a realistic test setup, used to try out
per-test attachments on Bitrise: screenshots, logs and UI dumps taken by Android UI tests show
up under the test case that produced them on the Tests tab, with no linking script.

## How the attachments get linked

1. The UI tests save files named `<classname>__<name>__<label>.<ext>` through AndroidX Test
   Storage, using the [`TestArtifacts`](app/src/androidTest/java/io/grocer/app/testing/TestArtifacts.kt)
   rule:
   - `artifacts.screenshot("cart")` saves a numbered step screenshot.
   - When a test fails, the rule saves a screenshot, the Compose UI tree (`.txt`) and the test
     process logcat (`.log`) before the activity is torn down.
   - The label always ends with the API level (`-api34`), so runs on different devices don't
     produce the same file name.
2. AGP pulls the files to `app/build/outputs/connected_android_test_additional_output/`.
3. **Export test results to Test Reports** (`custom-test-results-export` 1.3.0+) finds the files
   under its `base_path` that match a test case in the exported JUnit XML and copies them next to
   it.
4. **Deploy to Bitrise.io** (2.26.0+) adds an `attachment_N` property to the matching test case
   and uploads the files.

Example: `io.grocer.app.flows.CheckoutFlowTest__placesAnOrderWithAPromoCode__03-cart-api34.png`
goes to the `placesAnOrderWithAPromoCode` test of `io.grocer.app.flows.CheckoutFlowTest`.

## Tests

| Kind | Where | Runs on Bitrise via |
|---|---|---|
| JVM unit tests | `app/src/test` | Android Unit Test step |
| Emulator UI tests (Compose, Test Orchestrator) | `app/src/androidTest` | Gradle Runner + Export step |

`DeliveryFeeTest.deliveryIsFreeFromExactlyFiftyEuros` reproduces a real bug in the app: an order
of exactly €50.00 still pays for delivery (`Pricing.quote` uses `>` instead of `>=`). It is marked
`@KnownIssue` and only runs in the `ui_tests_with_known_issues` workflow, which therefore fails
and shows the failure screenshot, logcat and UI tree on the Tests tab.

## Workflows

| Workflow | What it shows |
|---|---|
| `ci` pipeline | `unit_tests`, `ui_tests` (API 34) and `ui_tests_api31` in parallel, all green |
| `ui_tests_with_known_issues` | A red build with one failing test and its failure attachments |

## Known gaps

- **Parameterized tests get no attachments.** The XML names them `findsTheProduct[MILK]`. On
  the device the file name keeps the brackets, but AGP replaces every character outside
  `A-Z a-z 0-9 . _ -` with `_` when it pulls the files, so the file arrives as
  `findsTheProduct_MILK___01-results-api34.png`. The convention only cleans
  `" * / : < > ? \ |`, so the file doesn't match its test case. The same goes for nested test
  classes (`Outer$Inner`), and for test names with spaces or non-ASCII letters.
  `CatalogSearchTest` is kept as it is to show this. The Export step logs the skip only at debug
  level (`verbose_log: yes`).
- The backend matches attachments by file name across the whole build, so every attachment
  name has to be unique within a build. Test name plus device label covers it here.
- The Export step exports only the first XML its search pattern matches. Connected tests write
  one XML per device, so one emulator per workflow is fine.

## Running locally

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.notAnnotation=io.grocer.app.testing.KnownIssue
```

Demo account: `demo@grocer.app` / `grocer123`. Promo codes: `SAVE10`, `FREEDELIVERY`.
