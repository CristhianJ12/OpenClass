package com.example.openclass.presentation.home.options.asistencia

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationAsist() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        // Ruta para la pantalla principal que muestra la lista de empleados
        composable("home") { ControlAsistenciaScreen(navController) }

        // Ruta para mostrar los detalles de un empleado
        composable("detalles/{nombre}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre")
            if (nombre != null) {
                // Aquí simplemente pasas el nombre al detalle
                DetallesEmpleadoScreen(navController, nombre)
            }
        }
    }
}
