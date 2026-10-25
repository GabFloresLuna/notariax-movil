package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            AppHeader(
                title = "Recuperar Contraseña",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Ingresa tu correo para la recuperación.")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { viewModel.onCorreoChange(it) },
                label = { Text("Correo electrónico") },
                isError = uiState.correoError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.correoError != null) {
                Text(text = uiState.correoError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                text = "Enviar instrucciones",
                onClick = { viewModel.onRecoverClick() },
                type = ButtonType.PRIMARY,
                isLoading = uiState.isLoading
            )

            if (uiState.mensaje != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.mensaje,
                    color = if (uiState.isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}