# Smart Pantry Manager: Report and Submission Workbook

This is a preparation document, not a completed report. Replace bracketed prompts with your own information and explanations. Record only tests you actually perform. Use real screenshots of your app.

## Report formatting

Use the assignment's prescribed cover page, Times New Roman 12 pt, 1.5 line spacing and Harvard referencing. Generate a table of contents from Word heading styles. Include the signed declaration. Export the completed report in an accepted format after checking the assignment's submission requirements.

## 1. Cover page

- App: Smart Pantry Manager
- Module: Mobile App Development 700
- Name: [Your full name]
- Student number: [Your student number]
- Date: [Submission date]
- GitHub: https://github.com/braedon1612-cpu/mobileappdevelopment
- [Attach the prescribed cover page and signed declaration.]

## 2. Table of contents

[Generate after applying headings to the completed report.]

## 3. Introduction

Write two or three paragraphs explaining:
- Why forgotten pantry ingredients can contribute to food waste.
- Who will use the app and what they need.
- How inventory tracking and strict recipe suggestions address the problem.
- The implemented scope: Java, local SQLite, five Activities, 20 recipes.

Avoid claiming measured food-waste savings unless you have evidence.

## 4. System design

### 4.1 Screens and navigation

| Activity | Responsibility |
| --- | --- |
| MainActivity | Display pantry stock; open add/edit; confirm deletion |
| AddEditIngredientActivity | Validate and save a new or existing ingredient |
| SuggestedRecipesActivity | Display recipes satisfying the strict matching rule |
| RecipeDetailActivity | Load a recipe using its ID and show its ingredients and instructions |
| SettingsActivity | Save a default unit for new pantry items |

Explain explicit Intents, ingredient_id, recipe_id and toolbar navigation. Describe how onResume refreshes pantry and suggestion lists after returning to them.

Render or redraw this flow as a legible figure for Word; do not paste raw Mermaid syntax as the final figure.

~~~mermaid
flowchart LR
    P[Pantry List] <-->|Ingredient ID via Intent| A[Add/Edit]
    P <-->|Toolbar| S[Suggested Recipes]
    S -->|Recipe ID via Intent| D[Recipe Detail]
    D -->|Back| S
    P <-->|Toolbar| T[Settings]
    S <-->|Toolbar| T
~~~

Figure caption: Screen flow for the five Activities in Smart Pantry Manager.

### 4.2 Data model

~~~mermaid
erDiagram
    Pantry {
        INTEGER _id PK
        TEXT name
        TEXT ingredient_key
        REAL quantity
        TEXT unit
    }
    Recipes {
        INTEGER _id PK
        TEXT name
        TEXT instructions
    }
    RecipeIngredients {
        INTEGER _id PK
        INTEGER recipe_id FK
        TEXT name
        TEXT ingredient_key
        REAL quantity
        TEXT unit
    }
    Recipes ||--|{ RecipeIngredients : requires
~~~

Figure caption: Recipe requirements reference their parent recipe; pantry stock is matched by canonical ingredient key and compatible unit dimension.

Explain these choices:
- Display names and canonical ingredient keys serve different purposes.
- Pantry has a unique ingredient_key/unit pair.
- RecipeIngredients has a unique recipe_id/ingredient_key pair.
- Quantities are checked; recipe quantities must be positive.
- The foreign key is enabled and recipe deletion cascades to requirement rows.
- Pantry deletion does not remove a recipe or a requirement.
- Database version 2 seeds the recipe collection while preserving existing pantry rows.
- SQLiteOpenHelper handles creation and upgrade transactions.
- SharedPreferences holds a UI preference, separate from the three SQLite tables.

### 4.3 Strict matching

Use your own explanation of this sequence:
1. Read current pantry stock.
2. Canonicalise names using the shared alias mapping.
3. Convert compatible mass units to grams and volume units to millilitres.
4. Sum stock by ingredient and dimension.
5. Sum recipe requirements by the same key.
6. Reject empty or invalid requirements.
7. Reject a recipe as soon as a required key is missing or its stock is insufficient.
8. Suggest only recipes whose every requirement passes.

Explain that there is no mass-to-volume or piece-to-mass conversion. Water, salt and oil must also be present if a recipe requires them. Each suggested recipe is evaluated independently; suggestions do not promise that every displayed recipe can be cooked simultaneously.

Worked example: Scrambled Eggs requires 2 eggs and 10 g butter. Two eggs plus 0.01 kg butter qualifies. One egg plus 10 g butter does not qualify.

## 5. Screenshots of the running application

Insert actual screenshots, number them and write a short caption explaining the evidence.

| Figure | Capture | Suggested caption focus |
| --- | --- | --- |
| 1 | Empty pantry | Helpful empty-state message |
| 2 | Pantry with stock | RecyclerView displays saved names, quantities and units |
| 3 | Add form filled in | Data entry before saving |
| 4 | Updated item | Edited quantity reflected in list |
| 5 | Delete confirmation | User confirms removal |
| 6 | List after deletion | Deleted row disappears |
| 7 | Blank-input validation | Invalid data is rejected |
| 8 | Duplicate-input feedback | Existing ingredient/unit pair is rejected |
| 9 | Scrambled Eggs suggestion | All requirements are present |
| 10 | Recipe detail | Complete ingredients and numbered method |
| 11 | No-match result | Missing or insufficient stock excludes recipes |
| 12 | Settings | Default unit selection and save |
| 13 | Add form after preference change | New item uses saved default unit |
| 14 | Pantry after app restart | Persisted inventory remains |
| 15 | GitHub history | Actual incremental development commits |
| 16 | JUnit result panel | Actual unit-test outcome, if run |

Keep text readable; crop unused desktop space. Do not use screenshots showing unrelated errors or unverified test claims.

## 6. Key code snippets

Choose 3-5 short excerpts from the final source. For each, give its purpose, explain the important lines and state why the implementation suits the app.

1. PantryDatabaseHelper.onCreate/onUpgrade: show the foreign key or non-destructive upgrade.
2. PantryRepository: show one CRUD operation and explain parameterised selection arguments.
3. StrictRecipeMatcher.canMake: show quantity comparison and rejection of missing requirements.
4. PantryAdapter.onBindViewHolder: explain how a model becomes a RecyclerView row.
5. SuggestedRecipesActivity: show the recipe ID Intent and explain loading details by ID.

Possible discussion points:
- BigDecimal prevents a broad tolerance from admitting a recipe with slightly insufficient stock.
- try-with-resources closes cursors.
- IDs identify stable database rows.
- onResume refreshes data after edits.
- RowMotion provides brief entrance effects and checks the device animation preference.

## 7. Challenges and solutions

Write about 2-3 problems you actually encountered. Use symptom, investigation, correction and lesson learned.

Observed during this project:
- Project JDK was initially undefined.
- Emulator reported that an instance was already running.
- RecipeDetailActivity contained a broken string literal that caused compilation to fail.

Explain these in your own words. Describe assistance and sources according to your institution's policy.

## 8. Conclusion and reflection

[Explain what you learned about Java models, Activities, SQLite, RecyclerView, Intents, validation and testing.]

[Evaluate the app honestly: what is complete, what is limited and what you would improve.]

Current improvement possibilities: expiry tracking, more ingredient aliases, cooking stock deduction, and a larger recipe collection. These are future work, not implemented features.

## 9. References

Use Harvard referencing for documentation, tutorials and other sources actually consulted. Verify titles, dates and URLs. Include an access date for web references where required. Do not invent references.

## Manual test record

Fill in Actual result, Pass/Fail and evidence after running each test.

| ID | Action | Expected result | Actual result / Pass-Fail / Evidence |
| --- | --- | --- | --- |
| T01 | Save Eggs, 2, piece | Row appears | [Record] |
| T02 | Edit Eggs to 4 | Same row shows 4 | [Record] |
| T03 | Cancel deletion | Row remains | [Record] |
| T04 | Confirm deletion | Row disappears | [Record] |
| T05 | Save blank name | Validation shown | [Record] |
| T06 | Save blank quantity | Validation shown | [Record] |
| T07 | Add existing ingredient/unit | Duplicate feedback; stock unchanged | [Record] |
| T08 | Add 2 eggs and 10 g butter | Scrambled Eggs suggested | [Record] |
| T09 | Reduce eggs to 1 | Scrambled Eggs excluded | [Record] |
| T10 | Use 2 eggs and 0.01 kg butter, with no other butter rows | Scrambled Eggs suggested | [Record] |
| T11 | Use butter in ml rather than g/kg | Does not satisfy butter mass requirement | [Record] |
| T12 | Open recipe detail | Ingredient list and method shown | [Record] |
| T13 | Relaunch app | Pantry persists | [Record] |
| T14 | Save piece as default; open Add | piece preselected | [Record] |
| T15 | Relaunch; check Settings | Preference persists | [Record] |
| T16 | Run StrictRecipeMatcherTest | 24 regression tests pass | [Record actual JUnit output] |
| T17 | Open Pantry and Recipes with rows | Brief motion; scrolling remains usable | [Record] |
| T18 | Disable device animations, reopen list | Entrance animation skipped | [Record] |

Standalone Java execution passed the 24 regression check bodies during development. That does not replace recording your actual Android Studio JUnit result. Compilation success alone does not prove CRUD, persistence or matching works on-device.

## Video plan: target 6 minutes 30 seconds

Narrate throughout using your own understanding. Rehearse once before recording.

| Time | Show and explain |
| --- | --- |
| 0:00-1:00 | GitHub repository, README and incremental commit history |
| 1:00-1:50 | Add, edit, delete and validation |
| 1:50-2:45 | Strict suggestion with eggs/butter; detail; reduce quantity and show exclusion |
| 2:45-3:20 | Close/reopen app for persistence; Settings preference |
| 3:20-4:10 | SQLite helper/repository and why local SQLite fits |
| 4:10-5:00 | Matching code, quantity comparison and unit conversion |
| 5:00-5:50 | RecyclerView binding and recipe-ID Intent |
| 5:50-6:30 | Recap limitations and explain an improvement |

Show actual files while explaining concepts. Keep the narration clear and screens legible. The assignment requires a 5-7 minute narrated video; include the video file in the submission ZIP.

## Final packaging

Create a submission folder containing:
- Full Android Studio project source, including Gradle wrapper and README.
- Completed report, with prescribed cover page, declaration, diagrams, screenshots, references and working GitHub link.
- Narrated demonstration video as a local MP4 file.

Exclude regenerable build/ and .gradle/ directories from the submission copy. Do not delete project source or wrapper files. Avoid including local.properties because it contains a machine-specific SDK path.

Use the prescribed filename:
Studentnumber_Surname_MobileAppDev700_Assignment.zip

The assignment limits the complete ZIP to 50 MB. Check the final archive size, open it, and verify that the report, video and project source are present. Open the report and play the video from the extracted copy. Confirm the GitHub repository is accessible to the marker.

The report, screenshots, personal reflection and narrated video remain to be completed by the student.
