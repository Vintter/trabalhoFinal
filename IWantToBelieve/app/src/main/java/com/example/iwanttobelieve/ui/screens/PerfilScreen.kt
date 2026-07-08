package com.example.iwanttobelieve.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iwanttobelieve.ui.data.AppViewModel

@Composable
fun PerfilScreen(
    viewModel: AppViewModel,
    onNavigateToCreatePost: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Meu Perfil", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        if (userProfile != null) {
            Text(text = "Nome: ${userProfile?.name}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Username: @${userProfile?.nickname}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        } else {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onNavigateToCreatePost,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Novo Post")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair da Conta")
        }
    }
}