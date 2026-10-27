package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.model.MecanismoFirma
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.ui.components.CustomRadioGroup
import cl.duoc.myapplication.viewmodel.AdvancedSignatureViewModel
import cl.duoc.myapplication.viewmodel.NewProcedureViewModel

@Composable
fun AdvancedSignatureScreen(
    procedureViewModel: NewProcedureViewModel,
    viewModel: AdvancedSignatureViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onProcedureCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState
    val procedureState = procedureViewModel.uiState

    // Modal que confirma que el trámite fue firmado y enviado
    if (procedureState.isSuccess) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "¡Trámite enviado!") },
            text = {
                Text(text = "Tu trámite fue firmado con firma avanzada y enviado al notario para su revisión. Quedó en estado pendiente.")
            },
            confirmButton = {
                CustomButton(
                    text = "Aceptar",
                    onClick = {
                        procedureViewModel.onTramiteConfirmado()
                        onProcedureCreated()
                    },
                    type = ButtonType.SECONDARY
                )
            }
        )
    }

    Scaffold(
        topBar = {
            AppHeader(title = "Firma avanzada", onBackClick = onNavigateBack)
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
            Text(
                text = "Elige cómo quieres firmar",
                style = MaterialTheme.typography.titleMedium
            )

            CustomRadioGroup(
                options = listOf(
                    MecanismoFirma.E_TOKEN,
                    MecanismoFirma.CERTIFICADO,
                    MecanismoFirma.ADQUIRIR
                ),
                selectedOption = state.mecanismo,
                onOptionSelected = { viewModel.onMecanismoChange(it) },
                isHorizontal = false
            )

            Text(
                text = descripcionDelMecanismo(state.mecanismo),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CustomButton(
                text = "Verificar mecanismo",
                onClick = { viewModel.onVerificarClick() },
                type = ButtonType.SECONDARY,
                isLoading = state.isVerificando
            )

            if (state.verificacionExitosa) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Verificación exitosa (simulada)",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (procedureState.errorMessage != null) {
                Text(
                    text = procedureState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            CustomButton(
                text = "Firmar y enviar",
                onClick = {
                    if (viewModel.validarVerificacion()) {
                        procedureViewModel.onFirmaCompletada("${state.mecanismo} (simulado)")
                    }
                },
                type = ButtonType.PRIMARY,
                isLoading = procedureState.isLoading
            )
        }
    }
}

// Texto que explica cada mecanismo, dejando claro que en este MVP se simulan
private fun descripcionDelMecanismo(mecanismo: String): String {
    return when (mecanismo) {
        MecanismoFirma.E_TOKEN ->
            "En la práctica el e-token se conecta a un computador. Aquí la conexión con el dispositivo es simulada."
        MecanismoFirma.CERTIFICADO ->
            "Se usaría un certificado digital del usuario. Aquí se simula con un certificado de prueba."
        else ->
            "Se contrataría una firma electrónica avanzada con un proveedor. Aquí la contratación es simulada."
    }
}