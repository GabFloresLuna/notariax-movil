package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.model.AdvancedSignatureUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdvancedSignatureViewModel : ViewModel() {

    // El estado de la pantalla
    var uiState by mutableStateOf(AdvancedSignatureUiState())
        private set

    // Al cambiar de mecanismo hay que volver a verificar
    fun onMecanismoChange(mecanismo: String) {
        uiState = uiState.copy(
            mecanismo = mecanismo,
            verificacionExitosa = false,
            errorMessage = null
        )
    }

    // Simula la conexión con el e-token, el certificado o el proveedor de firma
    fun onVerificarClick() {
        uiState = uiState.copy(isVerificando = true, errorMessage = null)

        viewModelScope.launch {
            delay(2000) // Simulamos la comunicación con el mecanismo externo
            uiState = uiState.copy(isVerificando = false, verificacionExitosa = true)
        }
    }

    // Devuelve true si el mecanismo ya fue verificado
    fun validarVerificacion(): Boolean {
        if (!uiState.verificacionExitosa) {
            uiState = uiState.copy(errorMessage = "Primero verifica el mecanismo de firma")
            return false
        }
        return true
    }
}