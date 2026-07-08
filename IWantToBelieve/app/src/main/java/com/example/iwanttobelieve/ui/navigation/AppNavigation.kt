package com.example.iwanttobelieve.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iwanttobelieve.ui.data.AppViewModel
import com.example.iwanttobelieve.ui.screens.LoginScreen
import com.example.iwanttobelieve.ui.screens.RegisterScreen
import com.example.iwanttobelieve.ui.screens.PerfilScreen
import com.example.iwanttobelieve.ui.screens.CreatePostScreen

@Composable
fun AppNavigation() {
    val appViewModel: AppViewModel = viewModel()
    val navController = rememberNavController()
    val currentUser by appViewModel.currentUser.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (currentUser != null) "profile" else "login",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("login") {
                LoginScreen(
                    viewModel = appViewModel,
                    onNavigateToRegister = { navController.navigate("register") },
                    onLoginSuccess = {
                        navController.navigate("profile") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("register") {
                RegisterScreen(
                    viewModel = appViewModel,
                    onNavigateToLogin = { navController.navigate("login") },
                    onRegisterSuccess = {
                        navController.navigate("profile") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("profile") {
                PerfilScreen(
                    viewModel = appViewModel,
                    onNavigateToCreatePost = { navController.navigate("create_post") },
                    onLogout = {
                        appViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    }
                )
            }

            composable("create_post") {
                CreatePostScreen(
                    viewModel = appViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onPostPublished = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}