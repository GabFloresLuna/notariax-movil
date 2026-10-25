package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.AuthRepository
import cl.duoc.myapplication.data.repository.AuthRepositoryImpl
import cl.duoc.myapplication.model.UserUiState
import cl.duoc.myapplication.validation.Validators
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
        // Validaciones locales (las reglas viven en Validators)
        val newState = uiState.copy(
            nombreError = Validators.validarNombre(uiState.nombre),
            apellidosError = Validators.validarNombre(uiState.apellidos),
            rutError = Validators.validarRut(uiState.rut),
            correoError = Validators.validarCorreo(uiState.correo),
            passwordError = Validators.validarPassword(uiState.password)
        )

        val hasError = listOf(
            newState.nombreError,
            newState.apellidosError,
            newState.rutError,
            newState.correoError,
            newState.passwordError
        ).any { it != null }

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