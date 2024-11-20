package com.example.listatareas

import Task
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.listatareas.ui.theme.ListaTareasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskListApp()
        }
    }
}

@Composable
fun TaskListApp() {
    val sampleTasks = listOf(
        Task(1, "Tarea 1", isCompleted = false, priority = Priority.HIGH),
        Task(2, "Tarea 2", isCompleted = true, priority = Priority.MEDIUM),
        Task(3, "Tarea 3", isCompleted = false, priority = Priority.LOW)
    )

    TaskListScreen(initialTasks = sampleTasks)
}
