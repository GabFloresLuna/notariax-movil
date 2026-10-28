package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.viewmodel.LoginViewModel
import cl.duoc.myapplication.R


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(), //se llama al viewModel de login
    onBackClick: () -> Unit = {}, //vuelve al menú principal
    onNavigateToRegisterScreen: () -> Unit = {}, //lleva a la creación de usuario
    onNavigateToForgotPassword: () -> Unit = {},  //redirecciona a olvidó su contraseña
    onLoginSuccess: () -> Unit = {} //vuelve al menú cuando el login es correcto
) {

    val uiState = viewModel.uiState  //conectado con el LoginUiState

    // Cuando el login es correcto, se avisa a la navegación
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onLoginSuccess()
    }


    Scaffold(
        topBar = {
            AppHeader(
                title = "Iniciar Sesión",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_editado),
                contentDescription = "Logo de Notaría X",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 32.dp)
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(text = "Correo")
                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.onCorreoChange(it) },
                    label = { Text("Ingrese su correo") },
                    isError = uiState.correoError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.correoError != null) {
                    Text(text = uiState.correoError!!, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Contraseña")

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Ingrese su contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = uiState.passwordError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.passwordError != null) {
                    Text(text = uiState.passwordError!!, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(32.dp))


                CustomButton(
                    text = "Iniciar Sesión",
                    onClick = { viewModel.onLoginClick() },
                    type = ButtonType.PRIMARY,
                    isLoading = uiState.isLoading
                )

                Spacer(modifier = Modifier.height(8.dp))


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomButton(
                        text = "¿Olivdó su contraseña?",
                        onClick = onNavigateToForgotPassword,
                        type = ButtonType.TEXT
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomButton(
                        text = "Crear usuario",
                        onClick = onNavigateToRegisterScreen,
                        type = ButtonType.TEXT
                    )
                }
            }
        }
    }
}