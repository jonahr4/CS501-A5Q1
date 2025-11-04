Assignment 5 Q1 – What’s for Dinner?

Files Added
- app/src/main/java/com/example/a5q1/MainActivity.kt – Activity setup, sealed routes, nav host, and bottom navigation bar.
- app/src/main/java/com/example/a5q1/RecipeViewModel.kt – In-memory recipe store, starter data, and helper methods for add/get.
- app/src/main/java/com/example/a5q1/ui/HomeScreen.kt – Home list screen with LazyColumn of recipes and card rows.
- app/src/main/java/com/example/a5q1/ui/AddRecipeScreen.kt – Add form composable with text fields and save button state.
- app/src/main/java/com/example/a5q1/ui/RecipeDetailScreen.kt – Detail view for showing ingredients and steps.
- app/src/main/java/com/example/a5q1/ui/SettingsScreen.kt – Placeholder screen for bottom nav consistency.

Navigation
- Bottom navigation uses NavigationBar plus currentBackStackEntryAsState() so the active tab stays highlighted.
- Screen routes are modeled with sealed Routes objects, and the detail screen expects `detail/{recipeId}`.
- Recipe taps call navigate("detail/{id}") with launchSingleTop; back navigation uses navigateUp().
- Adding a recipe pops back to Home with popUpTo(Home) and inclusive = true to avoid stacking duplicate homes.

State Management
- RecipeViewModel exposes a mutableStateListOf so the UI observes updates automatically.
- Add screen sends raw text to the view model, which trims inputs and substitutes defaults when fields are empty.
- Home and detail screens read straight from the shared view model provided by MainActivity.

AI Assistance
- I used OpenAI’s ChatGPT (Codex CLI) for reference while wiring up navigation and state patterns.
- The AI originally suggested navigating to "detail/{id}" without including the dynamic id in the route pattern, causing unresolved arguments. I corrected this by defining the route as `"${Routes.Detail.route}/{recipeId}"` and reading the id from backStackEntry.
- It also tried to create the view model inside a composable with viewModel(), but this project keeps things simple, so I hoisted the RecipeViewModel to MainActivity instead.
- ChatGPT helped me polish the Markdown layout for this README.

Repository
- https://github.com/jonahrothman/CS501-A5Q1
