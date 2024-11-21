package com.example.openclass.presentation.home.options.registropersonal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Preview
@Composable
fun RegistroPersonalScreen() {
    var showDialog by remember { mutableStateOf(false) }

    // Pantalla principal
    Column(modifier = Modifier.padding(16.dp).fillMaxSize().background(White)) {
        Text(text = "Registro de Personas", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para crear, editar o eliminar
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                // Mostrar formulario de creación
                showDialog = true
            }) {
                Text(text = "Crear")
            }

            Button(onClick = {
                // Mostrar formulario de edición (sin funcionalidad)
                showDialog = true
            }) {
                Text(text = "Editar")
            }

            Button(onClick = {
                // Mostrar opción de eliminar (sin funcionalidad)
            }) {
                Text(text = "Eliminar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de personas (solo como ejemplo, sin datos reales)
        LazyColumn {
            items(5) { index ->  // Aquí solo estamos mostrando 5 items de ejemplo
                PersonItem(index)
            }
        }

        // Mostrar el formulario cuando se quiere crear o editar
        if (showDialog) {
            PersonForm(onCancel = { showDialog = false })
        }
    }
}

    @Composable
    fun PersonItem(index: Int) {
        Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Nombre: Persona $index")
                Text(text = "Edad: ${20 + index}")
            }
        }
    }

    @Composable
    fun PersonForm(onCancel: () -> Unit) {
        Dialog(onDismissRequest = onCancel) {
            Surface(
                modifier = Modifier.padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Edad") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onCancel) {
                            Text(text = "Cancelar")
                        }

                        Button(onClick = onCancel) {
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }

