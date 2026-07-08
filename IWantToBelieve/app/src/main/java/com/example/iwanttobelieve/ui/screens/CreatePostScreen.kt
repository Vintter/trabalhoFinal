package com.example.iwanttobelieve.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onPostPublished: (String) -> Unit,
    onBack: () -> Unit
) {
    var postText by remember { mutableStateOf("") }
    val maxCharLimit = 250

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Nova Publicação",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = postText,
            onValueChange = { newValue ->
                if (newValue.length <= maxCharLimit) {
                    postText = newValue
                }
            },
            label = { Text("O que você está pensando?") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 8,
            singleLine = false,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${postText.length} / $maxCharLimit",
                style = MaterialTheme.typography.bodySmall,
                color = if (postText.length >= maxCharLimit) MaterialTheme.colorScheme.error else Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (postText.isNotBlank()) {
                    onPostPublished(postText)
                }
            },
            enabled = postText.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Publicar Post")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostScreenPreview() {
    CreatePostScreen(onPostPublished = {}, onBack = {})
}