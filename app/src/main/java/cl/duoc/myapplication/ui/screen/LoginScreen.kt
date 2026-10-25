package cl.duoc.myapplication.ui.screen

import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.model.LoginUiState
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel() //se llama al viewModel de login
) {

    val uiState = viewModel.uiState  //conectado con el LoginUiState
    var modal by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            AppHeader(
                title = "Iniciar Sesión",
                onBackClick = null
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
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
                        onClick = { modal = true/* llevar a creacicón de usuario */ },
                        type = ButtonType.TEXT
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomButton(
                        text = "Crear usuario",
                        onClick = { modal = true/* llevar a creacicón de usuario */ },
                        type = ButtonType.TEXT
                    )
                }
            }
        }
    }
}