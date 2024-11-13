package com.example.openclass.menu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "menu") {
                composable("menu") { MenuRecursosHumanos(navController) }
                composable("registro_personal") { RegistroPersonalScreen() }
                composable("datos_contrato") { DatosContratoScreen() }
                composable("beneficios") { BeneficiosScreen() }
                composable("desempeno") { DesempenoScreen() }
                composable("control_asistencias") { ControlAsistenciasScreen() }
                composable("tareas") { TareasScreen() }
            }
        }
    }
}

@Composable
fun MenuRecursosHumanos(navController: NavController) {
    val tablas = listOf(
        "Registro del Personal",
        "Datos del Contrato",
        "Beneficios",
        "Desempeño por Empleado",
        "Control de Asistencias",
        "Tareas"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Menú de Recursos Humanos", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        for (tabla in tablas) {
            OpcionMenu(tabla) {
                when (tabla) {
                    "Registro del Personal" -> navController.navigate("registro_personal")
                    "Datos del Contrato" -> navController.navigate("datos_contrato")
                    "Beneficios" -> navController.navigate("beneficios")
                    "Desempeño por Empleado" -> navController.navigate("desempeno")
                    "Control de Asistencias" -> navController.navigate("control_asistencias")
                    "Tareas" -> navController.navigate("tareas")
                    else -> println("Se seleccionó: $tabla")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun OpcionMenu(tabla: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(text = tabla, fontSize = 18.sp)
    }
}

@Composable
fun RegistroPersonalScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Registro de Nuevo Personal", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "Nombre:")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Acción de guardado */ }) {
            Text("Guardar")
        }
    }
}

@Composable
fun DatosContratoScreen() {
    val nombreEmpleado = remember { mutableStateOf("") }
    val datosContrato = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Datos del Contrato", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = nombreEmpleado.value,
            onValueChange = { nombreEmpleado.value = it },
            label = { Text("Nombre del Empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            datosContrato.value = "Datos del contrato para ${nombreEmpleado.value}"
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (datosContrato.value.isNotEmpty()) {
            Text(datosContrato.value, fontSize = 16.sp)
        }
    }
}

@Composable
fun BeneficiosScreen() {
    val nombreEmpleado = remember { mutableStateOf("") }
    val beneficios = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Beneficios del Empleado", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = nombreEmpleado.value,
            onValueChange = { nombreEmpleado.value = it },
            label = { Text("Nombre del Empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            beneficios.value = "Beneficios de ${nombreEmpleado.value}: Seguro médico, Vacaciones, Bonificaciones"
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (beneficios.value.isNotEmpty()) {
            Text(beneficios.value, fontSize = 16.sp)
        }
    }
}

@Composable //1
fun DesempenoScreen() {
    val nombreEmpleado = remember { mutableStateOf("") }
    val desempeno = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Desempeño del Empleado", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = nombreEmpleado.value,
            onValueChange = { nombreEmpleado.value = it },
            label = { Text("Nombre del Empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            desempeno.value = "Desempeño de ${nombreEmpleado.value}: Excelente, con un puntaje de 9.5"
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (desempeno.value.isNotEmpty()) {
            Text(desempeno.value, fontSize = 16.sp)
        }
    }
}

@Composable
fun ControlAsistenciasScreen() {
    val nombreEmpleado = remember { mutableStateOf("") }
    val asistencias = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Control de Asistencias", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = nombreEmpleado.value,
            onValueChange = { nombreEmpleado.value = it },
            label = { Text("Nombre del Empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            asistencias.value = "Asistencias de ${nombreEmpleado.value}: 20 días de asistencia, 2 días de ausencia"
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (asistencias.value.isNotEmpty()) {
            Text(asistencias.value, fontSize = 16.sp)
        }
    }
}

@Composable
fun TareasScreen() {
    val nombreEmpleado = remember { mutableStateOf("") }
    val tareas = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Tareas Asignadas", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = nombreEmpleado.value,
            onValueChange = { nombreEmpleado.value = it },
            label = { Text("Nombre del Empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            tareas.value = "Tareas asignadas a ${nombreEmpleado.value}: Reporte mensual, Análisis de datos, Actualización de registros"
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (tareas.value.isNotEmpty()) {
            Text(tareas.value, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuRecursosHumanos() {
    val navController = rememberNavController()
    MenuRecursosHumanos(navController)
}