# OACP Integration -- Wikipedia

How voice control was added to Wikipedia using the OACP protocol.

## Overview

Three capabilities exposed via OACP:

| Capability | Dispatch | What it does |
|-----------|----------|-------------|
| `search_articles` | **activity** | Opens Wikipedia search with query pre-filled |
| `open_article` | **activity** | Opens a specific article by title, optionally in another language |
| `open_random_article` | **activity** | Opens a random Wikipedia article |

## Architecture

All actions use the **activity** pattern since they all require UI. A transparent `OacpRouterActivity` receives the intent and routes to the correct Wikipedia screen (SearchActivity, PageActivity, or RandomActivity).

Action strings are built at runtime using `BuildConfig.APPLICATION_ID` via an `OacpActions` object, so they work across all build variants (dev, alpha, beta, prod).

## Files added

| File | Purpose |
|------|---------|
| `app/libs/oacp-android-release.aar` | OACP Kotlin SDK |
| `app/src/main/assets/oacp.json` | Capability manifest with rich metadata |
| `app/src/main/assets/OACP.md` | LLM context for disambiguation |
| `app/src/main/java/.../oacp/OacpActions.kt` | Action string constants using BuildConfig |
| `app/src/main/java/.../oacp/OacpRouterActivity.kt` | Transparent activity that routes intents |

## Files modified

### `app/build.gradle`
```groovy
implementation files('libs/oacp-android-release.aar')
```

### `AndroidManifest.xml`

Router activity with intent filters:
```xml
<activity
    android:name=".oacp.OacpRouterActivity"
    android:theme="@style/AppTheme.FullScreen.Translucent"
    android:exported="true">
    <intent-filter>
        <action android:name="${applicationId}.ACTION_SEARCH_ARTICLES" />
        <action android:name="${applicationId}.ACTION_OPEN_ARTICLE" />
        <action android:name="${applicationId}.ACTION_OPEN_RANDOM_ARTICLE" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

## Testing

```bash
# Search for a topic
adb shell "am start -a org.wikipedia.dev.ACTION_SEARCH_ARTICLES --es 'org.wikipedia.oacp.extra.QUERY' 'Flutter' -p org.wikipedia.dev"

# Open a specific article
adb shell "am start -a org.wikipedia.dev.ACTION_OPEN_ARTICLE --es 'org.wikipedia.oacp.extra.ARTICLE_TITLE' 'Alan Turing' -p org.wikipedia.dev"

# Open a random article
adb shell am start -a org.wikipedia.dev.ACTION_OPEN_RANDOM_ARTICLE -p org.wikipedia.dev
```

For other build variants, replace `org.wikipedia.dev` with the variant's applicationId.
