# GitHub Action – Build on Version Tag

Workflow that builds the Android app whenever a version tag is pushed.

## Trigger
- Push of any tag matching `v*` (examples: `v1.0.0`, `v1.2.3`, `v2.0.0-beta`)

## What it does
1. Checks out the code
2. Sets up JDK 17
3. Runs `./gradlew assembleDebug`
4. Uploads the debug APK as a build artifact (named with the tag)

## Usage
```bash
git tag v1.0.0
git push origin v1.0.0
```

The Action will start automatically. You can download the APK from the Actions tab.

## Notes
- Currently builds the **debug** APK (no signing required).
- For a signed release APK you will need to add keystore secrets and switch to `assembleRelease`.
- The Gradle wrapper (`gradlew`) must exist in the repository. If it is missing, run `gradle wrapper` locally and commit it.
