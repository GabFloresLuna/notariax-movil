package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import cl.duoc.myapplication.model.ProcedureUiState

class NewProcedureViewModel : ViewModel() {
    
    // Aquí vive el estado
    var uiState by mutableStateOf(ProcedureUiState())
        private set

    // Funciones para actualizar los campos
    fun onTipoPersonaChange(tipo: String) {
        uiState = uiState.copy(tipoPersona = tipo)
    }

    fun onRutChange(rut: String) {
        uiState = uiState.copy(rut = rut, rutError = null) // Limpiamos el error al escribir
    }

    fun onNombreChange(nombre: String) {
        uiState = uiState.copy(nombre = nombre, nombreError = null)
    }

    fun onApellidoChange(apellido: String) {
        uiState = uiState.copy(apellido = apellido, apellidoError = null)
    }

    fun onTipoFirmaChange(firma: String) {
        uiState = uiState.copy(tipoFirma = firma)
    }

    // Lógica al presionar "Iniciar"
    fun onIniciarClick() {
        // 1. Validamos (ejemplo básico)
        var hasError = false
        var newState = uiState

        if (uiState.rut.isBlank()) {
            newState = newState.copy(rutError = "El RUT es obligatorio")
            hasError = true
        }
        if (uiState.nombre.isBlank()) {
            newState = newState.copy(nombreError = "El nombre es obligatorio")
            hasError = true
        }
        if (uiState.apellido.isBlank()) {
            newState = newState.copy(apellidoError = "El apellido es obligatorio")
            hasError = true
        }

        // Si hay errores, actualizamos la UI y cortamos la ejecución
        if (hasError) {
            uiState = newState
            return
        }

        // 2. Si todo está bien, simulamos la carga
        uiState = uiState.copy(isLoading = true)
        
        viewModelScope.launch {
            delay(1500) // Simulamos conexión a red
            uiState = uiState.copy(isLoading = false)
            // Aquí harías la navegación a la siguiente pantalla de éxito
        }
    }
}