package com.example.a5q1.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.a5q1.Recipe

@Composable
fun HomeScreen(
    recipes: List<Recipe>,
    onRecipeSelected: (String) -> Unit
) {
    // shows every recipe so the user can hop into one quickly
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(recipes) { recipe ->
            RecipeRow(recipe = recipe, onRecipeSelected = onRecipeSelected)
        }
    }
}

@Composable
fun RecipeRow(recipe: Recipe, onRecipeSelected: (String) -> Unit) {
    // draws a tappable card for one recipe entry
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onRecipeSelected(recipe.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
            if (recipe.ingredients.isNotEmpty()) {
                Text(
                    text = "Ingredients: ${recipe.ingredients.joinToString()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
