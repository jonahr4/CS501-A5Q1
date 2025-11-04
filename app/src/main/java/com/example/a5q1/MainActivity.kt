package com.example.a5q1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a5q1.ui.AddRecipeScreen
import com.example.a5q1.ui.HomeScreen
import com.example.a5q1.ui.RecipeDetailScreen
import com.example.a5q1.ui.SettingsScreen
import com.example.a5q1.ui.theme.A5Q1Theme
import com.example.a5q1.RecipeViewModel

sealed class Routes(val route: String, val label: String, val icon: ImageVector?) {
    object Home : Routes("home", "Home", Icons.Default.Home)
    object Add : Routes("add", "Add", Icons.Default.Add)
    object Detail : Routes("detail", "Detail", null)
    object Settings : Routes("settings", "Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    // spins up the nav-driven app and wires the view model to the UI
    private val recipeViewModel by lazy { RecipeViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { RecipeApp(viewModel = recipeViewModel) }
    }
}

@Composable
fun RecipeApp(viewModel: RecipeViewModel) {
    // hosts the overall scaffold and nav graph so every screen can reach the data
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    A5Q1Theme {
        Scaffold(
            bottomBar = {
                BottomNavBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        ) { innerPadding ->
            RecipeNavHost(
                navController = navController,
                innerPadding = innerPadding,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun RecipeNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: RecipeViewModel
) {
    // defines every destination and how we pass around recipe ids
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                recipes = viewModel.recipes,
                onRecipeSelected = { recipeId ->
                    navController.navigate("${Routes.Detail.route}/$recipeId") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.Add.route) {
            AddRecipeScreen(
                onSaveRecipe = { title, ingredientText, stepText ->
                    viewModel.addRecipe(title, ingredientText, stepText)
                    navController.navigate(Routes.Home.route) {
                        launchSingleTop = true
                        popUpTo(Routes.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("${Routes.Detail.route}/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            val recipe = recipeId?.let { viewModel.getRecipe(it) }
            RecipeDetailScreen(
                recipe = recipe,
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(Routes.Settings.route) {
            SettingsScreen()
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    // draws the bottom nav so we can jump between the main sections without stack spam
    val items = listOf(Routes.Home, Routes.Add, Routes.Settings)
    NavigationBar {
        items.forEach { target ->
            val selected = currentDestination?.hierarchy?.any { it.route == target.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(target.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    target.icon?.let { icon ->
                        Icon(imageVector = icon, contentDescription = target.label)
                    }
                },
                label = { Text(target.label) }
            )
        }
    }
}
