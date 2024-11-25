package com.example.openclass.presentation.home.options.registropersonal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import coil.ImageLoader
import coil.util.DebugLogger

@Composable
fun RegistroPersonalScreen() {
    val db = FirebaseFirestore.getInstance()

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var employeeData by remember { mutableStateOf<EmployeeData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo de texto para la búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar empleado por ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    buscarEmpleado(
                        db = db,
                        empleadoId = searchQuery.text.trim(),
                        onLoading = { isLoading = true },
                        onSuccess = { data ->
                            employeeData = data
                            isLoading = false
                        },
                        onError = { error ->
                            errorMessage = error
                            isLoading = false
                        }
                    )
                }
            )
        )

        // Botón de búsqueda
        Button(
            onClick = {
                buscarEmpleado(
                    db = db,
                    empleadoId = searchQuery.text.trim(),
                    onLoading = { isLoading = true },
                    onSuccess = { data ->
                        employeeData = data
                        isLoading = false
                    },
                    onError = { error ->
                        errorMessage = error
                        isLoading = false
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        // Indicador de carga mientras se busca el empleado
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Mostrar los datos del empleado si los encontramos
        employeeData?.let { data ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MostrarDatosEmpleado(data) // Ahora esto maneja la imagen y los datos
            }
        }

        // Mensaje de error si ocurre
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}

fun buscarEmpleado(
    db: FirebaseFirestore,
    empleadoId: String,
    onLoading: () -> Unit,
    onSuccess: (EmployeeData) -> Unit,
    onError: (String) -> Unit
) {
    onLoading()
    db.collection("empleados")
        .document(empleadoId)
        .get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val data = documentSnapshot.toObject(EmployeeData::class.java)
                if (data != null) onSuccess(data) else onError("Error al procesar datos del empleado")
            } else {
                onError("Empleado no encontrado")
            }
        }
        .addOnFailureListener { error ->
            onError("Error al buscar empleado: ${error.localizedMessage}")
        }
}

data class EmployeeData(
    val nombre: String = "",
    val apellidos: String = "",
    val ingreso: Timestamp = Timestamp(Date()),
    val finalizacion: Timestamp = Timestamp(Date()),
    val nacimiento: Timestamp = Timestamp(Date()),
    val image: String = "" // URL de la imagen
)

fun Timestamp.toFormattedDate(): String {
    val date = this.toDate()
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date)
}

@Composable
fun MostrarDatosEmpleado(data: EmployeeData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Aquí agregamos la imagen del empleado
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .logger(DebugLogger())
            .build()

        // Cargar la imagen utilizando AsyncImage de Coil
        AsyncImage(
            model = data.image, // La URL de la imagen que ya está en los datos del empleado
            contentDescription = "Foto del empleado",
            imageLoader = imageLoader,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Ajusta el tamaño de la imagen según lo necesites
        )

        // Tarjeta para contener los datos del empleado
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Títulos y valores organizados
                DataRow(title = "Nombre", value = data.nombre)
                DataRow(title = "Apellidos", value = data.apellidos)
                DataRow(title = "Ingreso", value = data.ingreso.toFormattedDate())
                DataRow(title = "Finalización", value = data.finalizacion.toFormattedDate())
                DataRow(title = "Nacimiento", value = data.nacimiento.toFormattedDate())
            }
        }
    }
}

@Composable
fun DataRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}