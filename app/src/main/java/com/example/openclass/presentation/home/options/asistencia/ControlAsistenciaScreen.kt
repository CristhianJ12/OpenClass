package com.example.openclass.presentation.home.options.asistencia

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Empleado(
    val nombre: String,
    val apellidos: String,
    val asistencia: Int,
    val faltas: Int,
    val periodo: String
)

@Composable
fun ControlAsistenciaScreen(navController: NavController) {
    var empleados by remember { mutableStateOf<List<Empleado>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    // Obtener los datos de Firebase
    LaunchedEffect(true) {
        try {
            val snapshot = db.collection("asistencias").get().await()
            empleados = snapshot.documents.map { document ->
                Empleado(
                    nombre = document.getString("nombre") ?: "",
                    apellidos = document.getString("apellidos") ?: "",
                    asistencia = (document.getLong("asistencia") ?: 0).toInt(),
                    faltas = (document.getLong("faltas") ?: 0).toInt(),
                    periodo = document.getString("periodo") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("Firebase", "Error al obtener los datos", e)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Control de Asistencia",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Cabecera de la tabla con sombra celeste y letras blancas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(Color(0xFF00BFFF))  // Celeste de fondo para toda la fila
                ,  // Sombra celeste
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Empleado",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,  // Blanco
                modifier = Modifier
                    .weight(2f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            Text(
                text = "A",  // Para "Asistencia"
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,  // Blanco
                modifier = Modifier
                    .weight(.5f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            Text(
                text = "F",  // Para "Faltas"
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,  // Blanco
                modifier = Modifier
                    .weight(.5f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            Text(
                text = "Periodo",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,  // Blanco
                modifier = Modifier
                    .weight(2f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }

        // Mostrar la lista de empleados
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(empleados) { empleado ->
                AsistenciaRow(empleado = empleado, navController = navController)
            }
        }
    }
}

@Composable
fun AsistenciaRow(empleado: Empleado, navController: NavController) {
    val asistenciaColor = Color(0xFF006400)  // Verde para asistencia
    val faltasColor = Color.Red  // Rojo para faltas

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("detalles/${empleado.nombre}") }
            .background(MaterialTheme.colorScheme.surface),  // Background color using modifier
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)  // Correctly defined CardElevation here
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),  // Celeste de fondo para toda la fila de datos
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna para el nombre completo
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = "${empleado.nombre} ${empleado.apellidos}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Columna para la asistencia
            Column(
                modifier = Modifier
                    .weight(.8f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "${empleado.asistencia}%",
                    color = asistenciaColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // Columna para las faltas
            Column(
                modifier = Modifier
                    .weight(.8f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "${empleado.faltas}%",
                    color = faltasColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // Columna para el periodo
            Column(
                modifier = Modifier
                    .weight(2f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = empleado.periodo,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

