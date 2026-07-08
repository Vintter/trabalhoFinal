package com.example.iwanttobelieve.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.iwanttobelieve.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.example.iwanttobelieve.ui.data.AppViewModel
import com.example.iwanttobelieve.ui.theme.IWantToBelieveTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF8BE5F7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.iwanttobelieve),
                contentDescription = "I want to believe",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        if (email == "teste@exemplo.com" && password == "12345") {
                            onLoginSuccess()
                        } else {
                            errorMessage = "E-mail ou senha inválidos."
                        }
                    } else {
                        errorMessage = "Por favor, preencha todos os campos."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        if (email == "teste@exemplo.com" && password == "12345") {
                            onLoginSuccess()
                        } else {
                            errorMessage = "E-mail ou senha inválidos."
                        }
                    } else {
                        errorMessage = "Por favor, preencha todos os campos."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    IWantToBelieveTheme {
        LoginScreen(
            viewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
            onNavigateToRegister = {},
            onLoginSuccess = {}
        )
    }
}