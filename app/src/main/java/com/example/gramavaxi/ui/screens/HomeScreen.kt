package com.example.gramavaxi.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gramavaxi.R
import com.example.gramavaxi.ui.viewmodel.GramaVaxiViewModel
import com.example.gramavaxi.worker.VaccineReminderWorker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: GramaVaxiViewModel, onNavigateToAddAnimal: () -> Unit, onNavigateToReport: () -> Unit) {
    val animals by viewModel.animals.collectAsState()
    val schedules by viewModel.vaccineSchedules.collectAsState()
    val context = LocalContext.current

    val upcomingCount = schedules.count { it.status == "Pending" }
    val overdueCount = schedules.count { it.status == "Overdue" }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Grama-Vaxi", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                        Text(stringResource(R.string.tagline), fontSize = 10.sp, fontWeight = FontWeight.Normal)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAddAnimal,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text(stringResource(R.string.add_animal)) },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), Color.Transparent)
                    )
                ),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Welcome back, Farmer!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard(
                        title = stringResource(R.string.total_animals),
                        count = animals.size.toString(),
                        icon = Icons.Default.Pets,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = stringResource(R.string.upcoming_vaccines),
                        count = upcomingCount.toString(),
                        icon = Icons.Default.Upcoming,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                StatCard(
                    title = stringResource(R.string.overdue_vaccines),
                    count = overdueCount.toString(),
                    icon = Icons.Default.Warning,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                QuickActionCard(
                    title = "Report Sick Animal",
                    subtitle = "Get AI help and contact vet",
                    icon = Icons.Default.MedicalServices,
                    onClick = onNavigateToReport
                )
            }

            item {
                Button(
                    onClick = {
                        val testWork = OneTimeWorkRequestBuilder<VaccineReminderWorker>().build()
                        WorkManager.getInstance(context).enqueue(testWork)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Test Vaccine Alert Notification")
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, count: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.padding(8.dp).size(24.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = count, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Text(text = title, fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun QuickActionCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = subtitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
            }
        }
    }
}
