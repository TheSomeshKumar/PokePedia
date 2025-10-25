# Localization Guide

This document explains how to add support for additional languages in the PokePedia app.

## Current Languages

- **English** (default): `composeResources/values/strings.xml`
- **Spanish**: `composeResources/values-es/strings.xml`

## How to Add a New Language

### Step 1: Create Language Directory

Create a new directory in `composeApp/src/commonMain/composeResources/` with the appropriate language code:

```
composeResources/
  ├── values/              # Default (English)
  ├── values-es/           # Spanish
  ├── values-fr/           # French (example)
  ├── values-de/           # German (example)
  └── values-ja/           # Japanese (example)
```

### Step 2: Copy String Resources

Copy the `strings.xml` file from `values/` to your new language directory:

```bash
cp composeApp/src/commonMain/composeResources/values/strings.xml \
   composeApp/src/commonMain/composeResources/values-<language-code>/strings.xml
```

### Step 3: Translate Strings

Open the new `strings.xml` file and translate all string values. Keep the `name` attributes unchanged:

```xml
<!-- English (values/strings.xml) -->
<string name="loading_pokemon">Loading Pokemon...</string>

<!-- French (values-fr/strings.xml) -->
<string name="loading_pokemon">Chargement des Pokémon...</string>
```

### Common Language Codes

| Language | Code | Directory |
|----------|------|-----------|
| English | en | values (default) |
| Spanish | es | values-es |
| French | fr | values-fr |
| German | de | values-de |
| Italian | it | values-it |
| Portuguese | pt | values-pt |
| Japanese | ja | values-ja |
| Korean | ko | values-ko |
| Chinese (Simplified) | zh | values-zh |
| Chinese (Traditional) | zh-rTW | values-zh-rTW |
| Russian | ru | values-ru |
| Arabic | ar | values-ar |
| Hindi | hi | values-hi |

### Step 4: Test Your Translation

The app will automatically use the appropriate language based on the device's system language settings.

To test:
1. Build and run the app
2. Change your device language to the newly added language
3. Restart the app
4. Verify all strings are displayed correctly

## Translation Guidelines

### 1. Keep Format Specifiers
If a string contains format specifiers like `%s` or `%d`, keep them in the translation:

```xml
<!-- English -->
<string name="capture_rate_label">Capture Rate</string>

<!-- Spanish -->
<string name="capture_rate_label">Tasa de Captura</string>
```

### 2. Preserve Emojis
Keep emojis as they are universal:

```xml
<string name="legendary_badge">✨ Legendario</string>
```

### 3. Maintain Context
Ensure translations fit the UI context. Keep them concise for labels and buttons.

### 4. Cultural Sensitivity
Be mindful of cultural differences and adapt content where appropriate.

### 5. Test on Real Devices
Always test translations on actual devices to ensure proper display, especially for:
- Right-to-left (RTL) languages (Arabic, Hebrew)
- Languages with longer text (German, Russian)
- Languages with different character sets (Japanese, Chinese, Korean, Arabic)

## RTL Language Support

For right-to-left languages (Arabic, Hebrew, etc.), you may need additional layout adjustments:

1. Create the string resources as usual
2. Test the app with RTL enabled
3. Adjust layouts if needed using `start`/`end` instead of `left`/`right`

## Example: Adding French Translation

1. Create the directory:
```bash
mkdir -p composeApp/src/commonMain/composeResources/values-fr
```

2. Copy the strings file:
```bash
cp composeApp/src/commonMain/composeResources/values/strings.xml \
   composeApp/src/commonMain/composeResources/values-fr/strings.xml
```

3. Edit `values-fr/strings.xml` and translate:
```xml
<resources>
    <string name="app_name">PokePedia</string>
    <string name="search_pokemon_hint">Rechercher un Pokémon...</string>
    <string name="loading_pokemon">Chargement des Pokémon...</string>
    <string name="error_title">Oups! Une erreur s'est produite</string>
    <!-- ... translate all strings ... -->
</resources>
```

4. Rebuild the project and test!

## Resources

- [Android Localization Guide](https://developer.android.com/guide/topics/resources/localization)
- [iOS Localization Guide](https://developer.apple.com/documentation/xcode/localization)
- [Compose Multiplatform Resources](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html)

## Contributing Translations

If you'd like to contribute translations:

1. Fork the repository
2. Add your language following the steps above
3. Test thoroughly
4. Submit a pull request with:
   - The new `strings.xml` file
   - Screenshots showing the translation in action
   - A note about any special considerations

Thank you for helping make PokePedia accessible to more users worldwide! 🌍

