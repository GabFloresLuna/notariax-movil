package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.ui.components.CustomRadioGroup
import cl.duoc.myapplication.ui.components.CustomTextField
import cl.duoc.myapplication.viewmodel.NewProcedureViewModel

@Composable
fun NewProcedureScreen(
    viewModel: NewProcedureViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onProcedureCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            showSuccessDialog = true
        }
    }

    // Modal que confirma que el trámite fue creado
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
            },
            title = { Text(text = "¡Trámite ingresado!") },
            text = { Text(text = "Tu trámite fue creado y quedó en estado pendiente de revisión.") },
            confirmButton = {
                CustomButton(
                    text = "Aceptar",
                    onClick = {
                        showSuccessDialog = false
                        viewModel.onTramiteConfirmado()
                        onProcedureCreated()
                    },
                    type = ButtonType.SECONDARY
                )
            }
        )
    }

    Scaffold(
        topBar = {
            AppHeader(title = "Ingresar trámite", onBackClick = onNavigateBack)
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomRadioGroup(
                options = listOf("Persona natural", "Empresa"),
                selectedOption = state.tipoPersona,
                onOptionSelected = { viewModel.onTipoPersonaChange(it) }
            )

            CustomTextField(
                value = state.rut,
                onValueChange = { viewModel.onRutChange(it) },
                label = "RUT",
                placeholder = "12.345.678-5",
                leadingIcon = Icons.Filled.Badge,
                isError = state.rutError != null,
                errorMessage = state.rutError
            )

            CustomTextField(
                value = state.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = "Nombre",
                leadingIcon = Icons.Filled.Person,
                isError = state.nombreError != null,
                errorMessage = state.nombreError
            )

            CustomTextField(
                value = state.apellido,
                onValueChange = { viewModel.onApellidoChange(it) },
                label = "Apellido",
                leadingIcon = Icons.Filled.Person,
                isError = state.apellidoError != null,
                errorMessage = state.apellidoError
            )

            Text(
                text = "Tipo de firma",
                style = MaterialTheme.typography.titleMedium
            )

            CustomRadioGroup(
                options = listOf("Simple", "Avanzada"),
                selectedOption = state.tipoFirma,
                onOptionSelected = { viewModel.onTipoFirmaChange(it) }
            )

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            CustomButton(
                text = "Iniciar",
                onClick = { viewModel.onIniciarClick() },
                type = ButtonType.PRIMARY,
                isLoading = state.isLoading
            )
        }
    }
}