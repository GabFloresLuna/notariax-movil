package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.AuthRepository
import cl.duoc.myapplication.data.repository.AuthRepositoryImpl
import cl.duoc.myapplication.model.LoginUiState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onCorreoChange(correo: String) {
        uiState = uiState.copy(correo = correo, correoError = null)
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password, passwordError = null)
    }

    fun onLoginClick() {
        var hasError = false
        var newState = uiState

        if (uiState.correo.isBlank()) {
            newState = newState.copy(correoError = "El correo es obligatorio")
            hasError = true
        }
        if (uiState.password.isBlank()) {
            newState = newState.copy(passwordError = "La contraseña es obligatoria")
            hasError = true
        }

        if (hasError) {
            uiState = newState
            return
        }

        uiState = uiState.copy(isLoading = true)
        
        viewModelScope.launch {
            val result = repository.login(uiState.correo, uiState.password)
            
            if (result.isSuccess) {
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    passwordError = result.exceptionOrNull()?.message ?: "Error desconocido"
                )
            }
        }
    }
}