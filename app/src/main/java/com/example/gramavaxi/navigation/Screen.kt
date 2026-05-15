package com.example.gramavaxi.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Animals : Screen("animals")
    object AddAnimal : Screen("add_animal")
    object Calendar : Screen("calendar")
    object Reports : Screen("reports")
    object Profile : Screen("profile")
}
