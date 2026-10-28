package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.model.MecanismoFirma
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.viewmodel.SimpleSignatureViewModel
import cl.duoc.myapplication.viewmodel.NewProcedureViewModel

@Composable
fun SimpleSignatureScreen(
    procedureViewModel: NewProcedureViewModel,
    viewModel: SimpleSignatureViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onProcedureCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState
    val procedureState = procedureViewModel.uiState
    val colorTrazo = MaterialTheme.colorScheme.onSurface

    if (procedureState.isSuccess) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "¡Trámite enviado!") },
            text = {
                Text(text = "Tu trámite fue firmado y enviado al notario para su revisión. Quedó en estado pendiente.")
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
            AppHeader(title = "Firma simple", onBackClick = onNavigateBack)
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Dibuja tu firma con el dedo dentro del recuadro.",
                style = MaterialTheme.typography.bodyLarge
            )

            // Recuadro donde sera dibujada la firma
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { punto -> viewModel.onTrazoIniciado(punto) },
                            onDrag = { cambio, _ ->
                                cambio.consume()
                                viewModel.onTrazoContinuado(cambio.position)
                            }
                        )
                    }
            ) {
                state.trazos.forEach { trazo ->
                    val camino = Path()
                    trazo.forEachIndexed { indice, punto ->
                        if (indice == 0) {
                            camino.moveTo(punto.x, punto.y)
                        } else {
                            camino.lineTo(punto.x, punto.y)
                        }
                    }
                    drawPath(
                        path = camino,
                        color = colorTrazo,
                        style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
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
                    if (viewModel.validarFirma()) {
                        procedureViewModel.onFirmaCompletada(MecanismoFirma.MANUSCRITA)
                    }
                },
                type = ButtonType.PRIMARY,
                isLoading = procedureState.isLoading
            )

            CustomButton(
                text = "Limpiar firma",
                onClick = { viewModel.onLimpiarClick() },
                type = ButtonType.SECONDARY
            )
        }
    }
}