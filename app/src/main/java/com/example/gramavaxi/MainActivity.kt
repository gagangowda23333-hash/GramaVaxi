package com.example.gramavaxi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gramavaxi.navigation.Screen
import com.example.gramavaxi.ui.screens.*
import com.example.gramavaxi.ui.theme.GramaVaxiTheme
import com.example.gramavaxi.ui.viewmodel.GramaVaxiViewModel
import com.example.gramavaxi.worker.VaccineReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize background vaccination reminders
        setupWorkManager()

        setContent {
            GramaVaxiTheme {
                val viewModel: GramaVaxiViewModel = viewModel()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Navigation logic: Hide bottom bar on Splash and Add Animal screens
                val showBottomBar = currentDestination?.route !in listOf(Screen.Splash.route, Screen.AddAnimal.route)

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Splash.route) {
                            SplashScreen(onNavigateToHome = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            })
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToAddAnimal = { navController.navigate(Screen.AddAnimal.route) },
                                onNavigateToReport = { navController.navigate(Screen.Reports.route) }
                            )
                        }
                        composable(Screen.Animals.route) {
                            AnimalListScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onAddAnimal = { navController.navigate(Screen.AddAnimal.route) }
                            )
                        }
                        composable(Screen.Calendar.route) {
                            CalendarScreen(viewModel, onBack = { navController.popBackStack() })
                        }
                        composable(Screen.Reports.route) {
                            ReportDiseaseScreen(viewModel, onBack = { navController.popBackStack() })
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(viewModel, onBack = { navController.popBackStack() })
                        }
                        composable(Screen.AddAnimal.route) {
                            AddAnimalScreen(viewModel, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }

    private fun setupWorkManager() {
        // Daily background sync for vaccination alerts
        val workRequest = PeriodicWorkRequestBuilder<VaccineReminderWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "VaccineReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Home", Screen.Home.route, Icons.Default.Home),
        NavigationItem("Animals", Screen.Animals.route, Icons.Default.Pets),
        NavigationItem("Calendar", Screen.Calendar.route, Icons.Default.CalendarMonth),
        NavigationItem("Reports", Screen.Reports.route, Icons.Default.Report),
        NavigationItem("Profile", Screen.Profile.route, Icons.Default.Person)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(val title: String, val route: String, val icon: ImageVector)
