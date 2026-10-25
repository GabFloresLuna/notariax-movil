package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.ui.components.CustomTextField
import cl.duoc.myapplication.viewmodel.RegisterViewModel

@Composable
public fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
){
    val state = viewModel.uiState
    var showSuccessDialog by remember { mutableStateOf(false)}
    LaunchedEffect(state.isSuccess){
        if (state.isSuccess){
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog){
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
            },
            title = {Text(text="¡Registro Exitoso!")},
            text = {Text(text="Tu cuenta ha sido creada correctamente. Ya puedes iniciar sesión")},
            confirmButton = {
                CustomButton(
                    text = "Aceptar",
                    onClick = {
                        showSuccessDialog = false
                        onRegisterSuccess()
                    },
                    type = ButtonType.SECONDARY
                )
            }
        )
    }

    Scaffold(
        topBar ={
            AppHeader(title = "Creación de usuario", onBackClick = onNavigateBack)
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CustomTextField(
                value = state.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = "Nombre",
                leadingIcon = Icons.Filled.Person,
                isError = state.nombreError != null,
                errorMessage = state.nombreError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = state.apellidos,
                onValueChange = { viewModel.onApellidosChange(it) },
                label = "Apellidos",
                leadingIcon = Icons.Filled.Person,
                isError = state.apellidosError != null,
                errorMessage = state.apellidosError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = state.rut,
                onValueChange = { viewModel.onRutChange(it) },
                label = "RUT",
                placeholder = "12.345.678-9",
                leadingIcon = Icons.Filled.Badge,
                isError = state.rutError != null,
                errorMessage = state.rutError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = state.correo,
                onValueChange = { viewModel.onCorreoChange(it) },
                label = "Correo",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email,
                isError = state.correoError != null,
                errorMessage = state.correoError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Contraseña",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true,
                isError = state.passwordError != null,
                errorMessage = state.passwordError
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                text = "Crear Usuario",
                onClick = { viewModel.onCrearUsuarioClick() },
                type = ButtonType.PRIMARY,
                isLoading = state.isLoading
            )
        }

    }
}