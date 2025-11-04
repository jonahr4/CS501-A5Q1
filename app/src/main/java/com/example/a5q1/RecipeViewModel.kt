package com.example.a5q1

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.util.UUID

data class Recipe(
    val id: String,
    val title: String,
    val ingredients: List<String>,
    val steps: List<String>
)

class RecipeViewModel : ViewModel() {
    // keeps an in-memory list so recipes survive simple config changes
    private val _recipes = mutableStateListOf(
        Recipe(
            id = UUID.randomUUID().toString(),
            title = "Quick Pasta",
            ingredients = listOf("Pasta", "Salt", "Olive oil"),
            steps = listOf("Boil water", "Cook pasta", "Drain and season")
        )
    )

    val recipes: SnapshotStateList<Recipe>
        get() = _recipes

    // drops a new recipe into the list after trimming blank lines
    fun addRecipe(title: String, ingredientsText: String, stepsText: String) {
        if (title.isBlank()) return
        val ingredients = ingredientsText.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        val steps = stepsText.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        val recipe = Recipe(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            ingredients = if (ingredients.isNotEmpty()) ingredients else listOf("No ingredients added"),
            steps = if (steps.isNotEmpty()) steps else listOf("No steps added")
        )
        _recipes.add(recipe)
    }

    // fetches a recipe for the detail screen by its nav id
    fun getRecipe(id: String): Recipe? = _recipes.firstOrNull { it.id == id }
}
