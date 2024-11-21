package com.example.openclass

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.openclass.presentation.home.HomeScreen
import com.example.openclass.presentation.home.MenuScreen
import com.example.openclass.presentation.home.options.BeneficiosScreen
import com.example.openclass.presentation.home.options.registropersonal.RegistroPersonalScreen
import com.example.openclass.presentation.home.options.ControlAsistenciaScreen
import com.example.openclass.presentation.home.options.DatosContratoScreen
import com.example.openclass.presentation.home.options.DesempenoEmpleadoScreen
import com.example.openclass.presentation.home.options.TareasScreen
import com.example.openclass.presentation.initial.InitialScreen
import com.example.openclass.presentation.login.LoginScreen
import com.example.openclass.presentation.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationWrapper(
    navHostController: NavHostController
    ,auth: FirebaseAuth
    ,db: FirebaseFirestore
){

    NavHost(navController = navHostController, startDestination = "initial"){
        composable("initial"){
            InitialScreen(
                navigateToLogin = {navHostController.navigate("Login")},
                navigateToSignUp = {navHostController.navigate("SignUp")}
            )
        }
        composable("Login"){
            LoginScreen(auth){ navHostController.navigate("home")}
        }
        composable("SignUp"){
            SignUpScreen(auth)
        }
        composable("home"){
            MenuScreen(
                navigateToRegistroPersonal = {navHostController.navigate("RegistroPersonal")},
                navigateToTareas = {navHostController.navigate("Tareas")},
                navigateToBeneficios = {navHostController.navigate("ControlAsistencia")},
                navigateToDatosContrato = {navHostController.navigate("DatosContrato")},
                navigateToControlAsistencia = {navHostController.navigate("Beneficios")},
                navigateToDesempenoEmpleado = {navHostController.navigate("DesempenoEmpleado")}
            )
        }

        //OPTIONS
            composable("RegistroPersonal"){
                RegistroPersonalScreen()
            }
            composable("Tareas"){
                TareasScreen()
            }
            composable("ControlAsistencia"){
                ControlAsistenciaScreen()
            }
            composable("DatosContrato"){
                DatosContratoScreen()
            }
            composable("Beneficios"){
                BeneficiosScreen()
            }
            composable("DesempenoEmpleado"){
                DesempenoEmpleadoScreen()
            }

        //CURSO
        composable("Detalles"){
            HomeScreen(db)
        }

    }
}