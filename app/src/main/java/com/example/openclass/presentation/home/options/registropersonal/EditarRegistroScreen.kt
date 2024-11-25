package com.example.openclass.presentation.home.options.registropersonal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditarRegistroScreen() {
    val db = FirebaseFirestore.getInstance()

    var empleadoId by remember { mutableStateOf("") }
    var empleadoData by remember { mutableStateOf<EmployeeData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = empleadoId,
            onValueChange = { empleadoId = it },
            label = { Text("Buscar empleado por ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Button(
            onClick = {
                buscarEmpleadoParaEditar(
                    db = db,
                    empleadoId = empleadoId.trim(),
                    onLoading = { isLoading = true },
                    onSuccess = { data ->
                        empleadoData = data
                        isLoading = false
                        errorMessage = null
                        successMessage = null
                    },
                    onError = { error ->
                        errorMessage = error
                        isLoading = false
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cargar datos del empleado")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        empleadoData?.let { data ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = data.nombre,
                    onValueChange = { empleadoData = data.copy(nombre = it) },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = data.apellidos,
                    onValueChange = { empleadoData = data.copy(apellidos = it) },
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = data.ingreso.toFormattedDate(),
                    onValueChange = {},
                    label = { Text("Fecha de ingreso") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
                OutlinedTextField(
                    value = data.finalizacion.toFormattedDate(),
                    onValueChange = { date ->
                        val timestamp = date.toTimestampOrNull()
                        if (timestamp != null) {
                            empleadoData = data.copy(finalizacion = timestamp)
                        }
                    },
                    label = { Text("Fecha de finalización") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
                OutlinedTextField(
                    value = data.nacimiento.toFormattedDate(),
                    onValueChange = { date ->
                        val timestamp = date.toTimestampOrNull()
                        if (timestamp != null) {
                            empleadoData = data.copy(nacimiento = timestamp)
                        }
                    },
                    label = { Text("Fecha de nacimiento") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                Button(
                    onClick = {
                        guardarEmpleadoEditado(
                            db = db,
                            empleadoId = empleadoId.trim(),
                            empleadoData = data,
                            onSaving = { isSaving = true },
                            onSuccess = {
                                successMessage = "Empleado actualizado con éxito"
                                errorMessage = null
                                isSaving = false
                            },
                            onError = { error ->
                                errorMessage = error
                                successMessage = null
                                isSaving = false
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }

                Button(
                    onClick = {
                        eliminarEmpleado(
                            db = db,
                            empleadoId = empleadoId.trim(),
                            onDeleting = { isDeleting = true },
                            onSuccess = {
                                successMessage = "Empleado eliminado con éxito"
                                empleadoData = null
                                errorMessage = null
                                isDeleting = false
                            },
                            onError = { error ->
                                errorMessage = error
                                successMessage = null
                                isDeleting = false
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar Registro")
                }
            }
        }

        if (isSaving || isDeleting) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        successMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}

fun eliminarEmpleado(
    db: FirebaseFirestore,
    empleadoId: String,
    onDeleting: () -> Unit,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    onDeleting()
    db.collection("empleados")
        .document(empleadoId)
        .delete()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { error ->
            onError("Error al eliminar empleado: ${error.localizedMessage}")
        }
}

fun buscarEmpleadoParaEditar(
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

fun guardarEmpleadoEditado(
    db: FirebaseFirestore,
    empleadoId: String,
    empleadoData: EmployeeData,
    onSaving: () -> Unit,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    onSaving()
    val dataMap = mapOf(
        "nombre" to empleadoData.nombre,
        "apellidos" to empleadoData.apellidos,
        "finalizacion" to empleadoData.finalizacion,
        "nacimiento" to empleadoData.nacimiento
    )

    db.collection("empleados")
        .document(empleadoId)
        .set(dataMap, SetOptions.merge())
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { error ->
            onError("Error al guardar cambios: ${error.localizedMessage}")
        }
}

fun String.toTimestampOrNull(): Timestamp? {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = formatter.parse(this)
        if (date != null) Timestamp(date) else null
    } catch (e: Exception) {
        null
    }
}
