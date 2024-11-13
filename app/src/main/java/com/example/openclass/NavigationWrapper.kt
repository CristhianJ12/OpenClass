package com.example.openclass

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.openclass.menu.MenuActivity
import com.example.openclass.menu.MenuRecursosHumanos
import com.example.openclass.presentation.initial.InitialScreen
import com.example.openclass.presentation.login.LoginScreen
import com.example.openclass.presentation.signup.SignUpScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = "initial"){
        composable("inital"){
            InitialScreen()
        }
        composable("Login"){
            LoginScreen()
        }
        composable("SignUp"){
            SignUpScreen()
        }
    }
}