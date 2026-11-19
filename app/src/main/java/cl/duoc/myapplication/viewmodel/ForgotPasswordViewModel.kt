package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.AuthRepository
import cl.duoc.myapplication.data.repository.AuthRepositoryImpl
import cl.duoc.myapplication.model.ForgotPasswordUiState
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(ForgotPasswordUiState())
        private set

    fun onCorreoChange(correo: String) {
        uiState = uiState.copy(correo = correo, correoError = null, mensaje = null)
    }

    fun onRecoverClick() {
        if (uiState.correo.isBlank()) {
            uiState = uiState.copy(correoError = "El correo es obligatorio")
            return
        }

        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val result = repository.recoverPassword(uiState.correo)

            if (result.isSuccess) {
                uiState = uiState.copy(
                    isLoading = false,
                    isSuccess = true,
                    mensaje = "Instrucciones enviadas a tu correo."
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    isSuccess = false,
                    mensaje = result.exceptionOrNull()?.message ?: "Error desconocido"
                )
            }
        }
    }
}