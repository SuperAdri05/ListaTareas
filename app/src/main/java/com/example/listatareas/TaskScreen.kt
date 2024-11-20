package com.example.listatareas

import Task
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun TaskListScreen(initialTasks: List<Task>) {
    val tasks = remember { mutableStateListOf(*initialTasks.toTypedArray()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Añadir tarea")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Tareas pendientes", style = MaterialTheme.typography.headlineMedium)
            Divider()
            LazyColumn {
                items(tasks.filter { !it.isCompleted }) { task ->
                    TaskCard(task, onTaskUpdate = { updatedTask ->
                        updateTask(tasks, updatedTask)
                    })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Tareas completadas", style = MaterialTheme.typography.headlineMedium)
            Divider()
            LazyColumn {
                items(tasks.filter { it.isCompleted }) { task ->
                    TaskCard(task, onTaskUpdate = { updatedTask ->
                        updateTask(tasks, updatedTask)
                    })
                }
            }
        }

        // Mostrar cuadro de diálogo para añadir tareas
        if (showDialog) {
            AddTaskDialog(
                onDismiss = { showDialog = false },
                onAddTask = { newTask ->
                    tasks.add(newTask)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Nueva tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Título de la tarea") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Prioridad:")
                Row {
                    Priority.values().forEach { priority ->
                        Row(
                            modifier = Modifier.padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPriority == priority,
                                onClick = { selectedPriority = priority }
                            )
                            Text(priority.name)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        val newTask = Task(
                            id = (0..1000).random(), // ID generado aleatoriamente
                            title = taskTitle,
                            isCompleted = false,
                            priority = selectedPriority
                        )
                        onAddTask(newTask)
                    }
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun updateTask(tasks: MutableList<Task>, updatedTask: Task) {
    val index = tasks.indexOfFirst { it.id == updatedTask.id }
    if (index >= 0) {
        tasks[index] = updatedTask
    }
}


@Composable
fun TaskCard(task: Task, onTaskUpdate: (Task) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = task.priority.color,
                        content = {}
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Opciones"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(if (task.isCompleted) "Marcar como pendiente" else "Marcar como completada")
                    },
                    onClick = {
                        val updatedTask = task.copy(isCompleted = !task.isCompleted)
                        onTaskUpdate(updatedTask) // Llama a la función para actualizar la lista
                        expanded = false
                    }
                )


                DropdownMenuItem(
                    text = { Text("Cambiar prioridad") },
                    onClick = {
                        task.priority = when (task.priority) {
                            Priority.HIGH -> Priority.MEDIUM
                            Priority.MEDIUM -> Priority.LOW
                            Priority.LOW -> Priority.HIGH
                        }
                        onTaskUpdate(task)
                        expanded = false
                    }
                )
            }
        }
    }
}
