package com.example.openclass.presentation.home.options.asistencia

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun DetallesEmpleadoScreen(navController: NavController, nombre: String) {
    // State para almacenar el empleado
    var empleado by remember { mutableStateOf<Empleado?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Usamos LaunchedEffect para llamar la función suspendida de Firebase
    LaunchedEffect(nombre) {
        // Llamada a la base de datos para obtener el empleado por nombre
        try {
            val snapshot = db.collection("asistencias")
                .whereEqualTo("nombre", nombre)
                .get()
                .await() // Función suspendida llamada dentro de una corrutina

            // Extraemos el primer empleado encontrado
            empleado = snapshot.documents.firstNotNullOfOrNull { document ->
                val nombreEmpleado = document.getString("nombre")
                val apellidos = document.getString("apellidos")
                val asistencia = document.getLong("asistencia")?.toInt()
                val faltas = document.getLong("faltas")?.toInt()
                val periodo = document.getString("periodo")

                if (nombreEmpleado != null && apellidos != null && asistencia != null && faltas != null && periodo != null) {
                    Empleado(nombreEmpleado, apellidos, asistencia, faltas, periodo)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("Firebase", "Error al obtener el empleado", e)
        }
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detalles de $nombre",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp),
            color = Color(0xFF00BFFF)
        )

        // Mostrar la fila de asistencia con los datos del empleado
        if (empleado != null) {
            // Tarjeta para mostrar los detalles del empleado
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Nombre completo
                    Text(
                        text = "${empleado!!.nombre} ${empleado!!.apellidos}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Asistencia
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Asistencia: ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${empleado!!.asistencia}%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Faltas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Faltas: ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${empleado!!.faltas}%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Periodo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Periodo: ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = empleado!!.periodo,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Gráfico de barras para mostrar asistencia y faltas
            Spacer(modifier = Modifier.height(24.dp))
            BarChart(
                asistencia = empleado!!.asistencia.toFloat(),
                faltas = empleado!!.faltas.toFloat()
            )
        } else {
            Text("Empleado no encontrado.", fontSize = 18.sp, color = Color.Gray)
        }

        // Botón para regresar
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF00BFFF))
        ) {
            Text(text = "Volver", fontSize = 18.sp)
        }
    }
}


@Composable
fun BarChart(asistencia: Float, faltas: Float) {
    // Calcular la proporción de cada barra respecto al total
    val total = asistencia + faltas
    val asistenciaWidth = asistencia / total * 100f
    val faltasWidth = faltas / total * 100f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Centrar todo el contenido
        verticalArrangement = Arrangement.Center // Centrar de manera vertical
    ) {
        // Primera fila con las dos barras
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), // Asignar altura suficiente para las barras
            horizontalArrangement = Arrangement.Center // Centrar las barras en la fila
        ) {
            // Barra de Asistencia
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(asistenciaWidth.dp)
                    .background(Color(0xFF006400)) // Verde para Asistencia
            )

            // Spacer para crear separación entre las columnas
            Spacer(modifier = Modifier.width(60.dp)) // Espacio de 16dp entre las barras

            // Barra de Faltas
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(faltasWidth.dp)
                    .background(Color.Red) // Rojo para Faltas
            )
        }

        // Segunda fila con los números de días debajo de las barras
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre las barras y los números
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Centrar los números debajo de las barras
        ) {
            // Número de días de Asistencia
            Text(
                text = "${(asistencia * 30 / 100).toInt()} días",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF006400),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            // Spacer para separar los números de días
            Spacer(modifier = Modifier.width(70.dp)) // Espacio de 16dp entre los números

            // Número de días de Faltas
            Text(
                text = "${(faltas * 30 / 100).toInt()} días",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
