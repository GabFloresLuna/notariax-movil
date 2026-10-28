package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import cl.duoc.myapplication.model.SimpleSignatureUiState

class SimpleSignatureViewModel : ViewModel() {

    var uiState by mutableStateOf(SimpleSignatureUiState())
        private set

    fun onTrazoIniciado(punto: Offset) {
        uiState = uiState.copy(
            trazos = uiState.trazos + listOf(listOf(punto)),
            errorMessage = null
        )
    }

    fun onTrazoContinuado(punto: Offset) {
        if (uiState.trazos.isEmpty()) return

        val trazoActualizado = uiState.trazos.last() + punto
        uiState = uiState.copy(trazos = uiState.trazos.dropLast(1) + listOf(trazoActualizado))
    }

    fun onLimpiarClick() {
        uiState = uiState.copy(trazos = emptyList(), errorMessage = null)
    }

    fun validarFirma(): Boolean {
        if (uiState.trazos.isEmpty()) {
            uiState = uiState.copy(errorMessage = "Dibuja tu firma para continuar")
            return false
        }
        return true
    }
}