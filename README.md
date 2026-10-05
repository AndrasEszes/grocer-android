# Grocer

A small grocery shopping app (Jetpack Compose) used to try out per-test attachments on Bitrise:
screenshots and logs taken by Android UI tests show up under the test case that produced them on
the Tests tab, with no linking script.

## How it works

1. The UI tests save files named `<classname>__<name>__<label>.<ext>` through AndroidX Test
   Storage, using the [`TestArtifacts`](app/src/androidTest/java/io/grocer/app/testing/TestArtifacts.kt)
   rule:
   - `artifacts.screenshot("cart")` saves a numbered step screenshot, e.g.
     `io.grocer.app.flows.CheckoutFlowTest__placesAnOrderWithAPromoCode__03-cart.png`.
   - When a test fails, the rule saves a screenshot, the test process logcat (`.log`) and the
     Compose UI tree (`.txt`).
2. AGP pulls the files to `app/build/outputs/connected_android_test_additional_output/`.
3. **Export test results to Test Reports** (1.3.0+) finds the files under its `base_path` that
   match a test case in the JUnit XML and copies them next to it.
4. **Deploy to Bitrise.io** (2.26.0+) links each file to its test case and uploads it.

The [`bitrise.yml`](bitrise.yml) is the
[emulator test recipe](https://github.com/bitrise-io/workflow-recipes/blob/main/recipes/android-emulator-test.md)
with a wider `base_path`, so the Export step finds the screenshots too.

## The build is red on purpose

`DeliveryFeeTest.deliveryIsFreeFromExactlyFiftyEuros` catches a real bug in the app: an order of
exactly €50.00 still pays for delivery (`Pricing.quote` uses `>` instead of `>=`). Its failure
screenshot, logcat and UI tree are on the Tests tab.

## Known gap: parameterized tests

`CatalogSearchTest` gets no attachments. The XML names its tests `findsTheProduct[MILK]`, but
AGP replaces every character outside `A-Z a-z 0-9 . _ -` with `_` when it pulls the files from
the device, so the file arrives as `findsTheProduct_MILK___01-results.png` and matches no test
case.

## Running locally

```bash
./gradlew connectedDebugAndroidTest
```

Demo account: `demo@grocer.app` / `grocer123`. Promo codes: `SAVE10`, `FREEDELIVERY`.
