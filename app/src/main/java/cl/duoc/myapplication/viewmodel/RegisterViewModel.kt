package cl.duoc.myapplication.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.AuthRepository
import cl.duoc.myapplication.data.repository.AuthRepositoryImpl
import cl.duoc.myapplication.model.UserUiState
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(UserUiState())
        private set

    // Funciones de actualización
    fun onNombreChange(nombre: String) {
        uiState = uiState.copy(nombre = nombre, nombreError = null)
    }
    fun onApellidosChange(apellidos: String) {
        uiState = uiState.copy(apellidos = apellidos, apellidosError = null)
    }
    fun onRutChange(rut: String) {
        uiState = uiState.copy(rut = rut, rutError = null)
    }
    fun onCorreoChange(correo: String) {
        uiState = uiState.copy(correo = correo, correoError = null)
    }
    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password, passwordError = null)
    }

    fun onCrearUsuarioClick() {
        var hasError = false
        var newState = uiState

        // Validaciones locales
        if (uiState.nombre.isBlank()) {
            newState = newState.copy(nombreError = "Campo obligatorio"); hasError = true
        }
        if (uiState.apellidos.isBlank()) {
            newState = newState.copy(apellidosError = "Campo obligatorio"); hasError = true
        }
        if (uiState.rut.isBlank()) {
            newState = newState.copy(rutError = "Campo obligatorio"); hasError = true
        }
        if (uiState.correo.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(uiState.correo).matches()) {
            newState = newState.copy(correoError = "Correo inválido"); hasError = true
        }
        if (uiState.password.length < 6) {
            newState = newState.copy(passwordError = "Mínimo 6 caracteres"); hasError = true
        }

        if (hasError) {
            uiState = newState
            return
        }

        // Llamada al servidor (Repositorio)
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val result = repository.registerUser(
                nombre = uiState.nombre,
                apellidos = uiState.apellidos,
                rut = uiState.rut,
                correo = uiState.correo,
                password = uiState.password
            )

            if (result.isSuccess) {
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    correoError = result.exceptionOrNull()?.message // Mostramos el error en el correo
                )
            }
        }
    }

    // Limpia el formulario una vez confirmado el registro, para que no reaparezca el modal
    fun onRegistroConfirmado() {
        uiState = UserUiState()
    }
}