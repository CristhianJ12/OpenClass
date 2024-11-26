package com.example.openclass.presentation.home.options.registropersonal

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AgregarRegistroScreen() {
    val db = FirebaseFirestore.getInstance()

    var idDocumento by remember { mutableStateOf(TextFieldValue("")) } // Campo para ID del documento
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var apellidos by remember { mutableStateOf(TextFieldValue("")) }
    var ingreso by remember { mutableStateOf(TextFieldValue("")) }
    var finalizacion by remember { mutableStateOf(TextFieldValue("")) }
    var nacimiento by remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo para ID del documento
        OutlinedTextField(
            value = idDocumento,
            onValueChange = { idDocumento = it },
            label = { Text("ID del Documento (opcional)") },
            placeholder = { Text("Dejar vacío para generar automáticamente") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ingreso,
            onValueChange = { ingreso = it },
            label = { Text("Fecha de Ingreso (dd/MM/yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = finalizacion,
            onValueChange = { finalizacion = it },
            label = { Text("Fecha de Finalización (dd/MM/yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nacimiento,
            onValueChange = { nacimiento = it },
            label = { Text("Fecha de Nacimiento (dd/MM/yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { launcher.launch("image/*") },

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3), // Celeste (código hexadecimal)
                contentColor = Color.White)) {
            Text("Seleccionar Imagen")
        }

        imageUri?.let { uri ->
            val context = LocalContext.current
            val contentResolver = context.contentResolver

            Button(
                onClick = {
                    isUploading = true
                    subirImagenACloudinary(
                        contentResolver = contentResolver,
                        imageUri = uri,
                        onSuccess = { url ->
                            uploadedImageUrl = url
                            isUploading = false
                        },
                        onError = { error ->
                            uploadError = error
                            isUploading = false
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3), // Celeste (código hexadecimal)
                    contentColor = Color.White)
            ) {
                Text("Subir Imagen")
            }
        }

        if (isUploading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        uploadError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                guardarEmpleadoConIdPersonalizado(
                    db = db,
                    idDocumento = idDocumento.text.trim().ifEmpty { null },
                    nombre = nombre.text,
                    apellidos = apellidos.text,
                    ingreso = ingreso.text,
                    finalizacion = finalizacion.text,
                    nacimiento = nacimiento.text,
                    imageUrl = uploadedImageUrl
                )

                // Limpiar los campos
                idDocumento = TextFieldValue("")
                nombre = TextFieldValue("")
                apellidos = TextFieldValue("")
                ingreso = TextFieldValue("")
                finalizacion = TextFieldValue("")
                nacimiento = TextFieldValue("")
                imageUri = null
                uploadedImageUrl = null
                uploadError = null
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3), // Celeste (código hexadecimal)
                contentColor = Color.White)
        ) {
            Text("Guardar Registro")
        }
    }
}

fun guardarEmpleadoConIdPersonalizado(
    db: FirebaseFirestore,
    idDocumento: String?,
    nombre: String,
    apellidos: String,
    ingreso: String,
    finalizacion: String,
    nacimiento: String,
    imageUrl: String?
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    try {
        val empleado = hashMapOf(
            "nombre" to nombre,
            "apellidos" to apellidos,
            "ingreso" to Timestamp(dateFormat.parse(ingreso) ?: Date()),
            "finalizacion" to Timestamp(dateFormat.parse(finalizacion) ?: Date()),
            "nacimiento" to Timestamp(dateFormat.parse(nacimiento) ?: Date()),
            "image" to (imageUrl ?: "")
        )

        val referencia = if (idDocumento != null) {
            db.collection("empleados").document(idDocumento)
        } else {
            db.collection("empleados").document()
        }

        referencia.set(empleado)
            .addOnSuccessListener { println("Empleado agregado correctamente") }
            .addOnFailureListener { println("Error al agregar empleado: ${it.localizedMessage}") }
    } catch (e: Exception) {
        println("Error al procesar las fechas o guardar: ${e.localizedMessage}")
    }
}

fun subirImagenACloudinary(
    contentResolver: ContentResolver, // Agregamos ContentResolver como parámetro
    imageUri: Uri,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val cloudName = "dxr9bdqzl"
    val uploadPreset = "ml_default12"

    val client = OkHttpClient()

    // Convertir el Uri en un archivo temporal
    val inputStream = contentResolver.openInputStream(imageUri)
    val tempFile = File.createTempFile("upload", ".png") // Ajusta la extensión según sea necesario
    inputStream?.use { input ->
        tempFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file",
            tempFile.name,
            tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        )
        .addFormDataPart("upload_preset", uploadPreset)
        .build()

    val request = Request.Builder()
        .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
        .post(requestBody)
        .build()

    Thread {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val json = JSONObject(responseBody ?: "")
                val url = json.getString("secure_url")
                onSuccess(url)
            } else {
                onError("Error al subir imagen: ${response.message}")
            }
        } catch (e: Exception) {
            onError("Excepción: ${e.message}")
        } finally {
            // Eliminar el archivo temporal después de usarlo
            tempFile.delete()
        }
    }.start()
}