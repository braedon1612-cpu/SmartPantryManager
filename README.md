# Smart Pantry Manager

A Java Android application for Mobile App Development 700. It helps users track food at home and find recipes they can prepare using their existing pantry stock.

Repository: https://github.com/braedon1612-cpu/mobileappdevelopment

## Features

- Add, list, edit and delete pantry ingredients.
- Store ingredient names, quantities and units locally.
- Display pantry items using a RecyclerView and custom adapter.
- Load 20 default recipes, with 70 ingredient requirements and preparation steps.
- Suggest a recipe only when every required ingredient is available in sufficient quantity.
- View complete recipe ingredients and preparation instructions.
- Save a default unit for new ingredients in Settings.
- Navigate between screens through a toolbar and explicit Intents.
- Show feedback for an empty pantry, no matching recipes, invalid input and duplicate items.

## Technology and database choice

Application source code is Java, with XML layouts. The project uses Android Studio, AndroidX, RecyclerView and SQLiteOpenHelper.

SQLite was selected because the pantry and recipes are small, structured datasets that can be stored on-device. It supports persistence, constraints, foreign keys and CRUD without a separate backend or account. The app works offline after installation; Gradle dependency downloads require internet during initial setup.

SharedPreferences stores only the default unit preference. Inventory and recipe data remain in SQLite.

Gradle configuration files use Kotlin DSL (.gradle.kts); these are build scripts. The application classes are Java.

## Requirements

- Android Studio compatible with the project's Android Gradle Plugin 9.4.1.
- Android SDK Platform 37 and any build components requested by Android Studio.
- JDK 21, as used during this project's setup, or the compatible bundled Gradle JDK.
- An Android emulator or physical Android device running API 24 or higher.
- Internet access for the initial Gradle and SDK downloads.

The project compiles and targets API 37. Application Java source compatibility is set to Java 11.

## Setup and run

1. Clone this repository, or download and extract its ZIP.
2. Open the repository root in Android Studio. This is the folder containing settings.gradle.kts and gradlew.bat.
3. Let Android Studio configure the local SDK path. Do not copy another computer's local.properties.
4. Set a compatible Gradle JDK in Settings > Build, Execution, Deployment > Build Tools > Gradle if requested.
5. Install SDK Platform 37 if prompted and complete Gradle sync.
6. Select the app run configuration.
7. Select a running emulator or a connected device with USB debugging enabled.
8. Click Run.

A fresh installation starts with an empty pantry and 20 seeded recipes. Database version 2 also seeds recipes when upgrading the earlier version 1 database, preserving pantry rows.

To build from a Windows terminal in the project root:

    .\gradlew.bat :app:assembleDebug

The debug APK is generated under app/build/outputs/apk/debug/.

## Using the app

1. On Pantry, choose Add ingredient.
2. Enter a name, a non-negative quantity and a unit, then save.
3. Use Edit or Delete beside an existing item to change the pantry.
4. Choose Recipes in the toolbar to calculate strict suggestions.
5. Choose View recipe to read requirements and preparation steps.
6. Open the toolbar overflow menu and select Settings to save the default unit for new items.

Supported units: g, kg, ml, l and piece. Existing items retain their saved unit when the default changes.

## Strict matching rule

The matcher loads the user's current pantry and checks each recipe independently. A recipe qualifies only if every requirement is satisfied.

- Compatible weights are converted to grams.
- Compatible volumes are converted to millilitres.
- Counts remain in pieces.
- Stock for the same canonical ingredient and compatible units is combined.
- Common plural aliases such as eggs/egg and tomatoes/tomato are normalised.
- Missing ingredients, insufficient stock and incompatible units reject a recipe.
- No pantry staples, including water, salt or oil, are assumed to be available.
- Viewing a recipe does not consume or deduct pantry stock.

Example: Scrambled Eggs requires 2 eggs and 10 g butter. With 2 eggs and 0.01 kg butter it qualifies. Reducing the egg quantity to 1 removes it from suggestions.

Suggestions are calculated individually; multiple suggested recipes are alternatives and may share the same stock.

## Database structure

Database: smart_pantry.db. Current version: 2.

| Table | Fields | Purpose |
| --- | --- | --- |
| Pantry | _id, name, ingredient_key, quantity, unit | User-owned stock |
| Recipes | _id, name, instructions | Recipe details |
| RecipeIngredients | _id, recipe_id, name, ingredient_key, quantity, unit | Required ingredients |

Recipes has a one-to-many relationship with RecipeIngredients through recipe_id. Deleting a recipe cascades to its requirement rows. Recipe requirements do not reference pantry rows, so deleting pantry stock cannot delete a recipe.

Pantry has a unique constraint on ingredient_key and unit. Duplicate pairs are rejected with feedback; users can edit the existing row.

## Code structure

    app/src/main/java/com/example/smartpantrymanager/
      MainActivity.java
      AddEditIngredientActivity.java
      SuggestedRecipesActivity.java
      RecipeDetailActivity.java
      SettingsActivity.java
      adapter/
        PantryAdapter.java
        RecipeAdapter.java
      database/
        PantryDatabaseHelper.java
        PantryRepository.java
        RecipeRepository.java
        RecipeSeeder.java
      logic/
        IngredientNames.java
        StrictRecipeMatcher.java
      model/
        Ingredient.java
        Recipe.java
      ui/
        Navigation.java
        ScreenInsets.java

## Automated tests

StrictRecipeMatcherTest contains 24 local JUnit regression tests for missing ingredients, exact and insufficient quantities, aliases, compatible-unit conversion, incompatible units, decimal arithmetic, duplicate requirements and invalid quantities.

In Android Studio, open StrictRecipeMatcherTest and run the class using its gutter Run icon. No emulator is required for these local tests.

Alternatively:

    .\gradlew.bat :app:testDebugUnitTest

The standard HTML results are written under app/build/reports/tests/testDebugUnitTest/.

## Manual verification

- Add an ingredient and check it appears.
- Edit its quantity and confirm the list refreshes.
- Cancel a deletion, then confirm a deletion.
- Close and relaunch the app and check inventory persistence.
- Save an empty name or quantity and check validation.
- Try an existing ingredient/unit pair and check duplicate feedback.
- Add 2 eggs and 10 g butter; verify Scrambled Eggs appears.
- Open its detail screen and verify ingredients and method.
- Reduce eggs to 1; verify Scrambled Eggs disappears.
- Test 0.01 kg butter against the 10 g requirement.
- Save piece as the default unit; verify a new item defaults to piece.
- Restart the app and verify the settings preference persists.

Record actual results and capture real screenshots for the assignment report.

## Current limitations

Ingredient aliases are intentionally limited, rather than a full natural-language system. There is no conversion between weight, volume and piece counts, because those conversions need ingredient-specific information. Expiry dates, stock deduction after cooking, cloud synchronisation and notifications are not implemented.

No location, mapping or payment features are included.
