package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.ProcedureRepository
import cl.duoc.myapplication.data.repository.ProcedureRepositoryImpl
import cl.duoc.myapplication.model.ProcedureUiState
import cl.duoc.myapplication.validation.Validators
import com.google.android.gms.maps3d.Validators
import kotlinx.coroutines.launch

class NewProcedureViewModel(
    // Inyectamos el repositorio. Si no se pasa uno, usa el impl por defecto.
    private val repository: ProcedureRepository = ProcedureRepositoryImpl()
) : ViewModel() {

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
        // 1. Validamos (las reglas viven en Validators)
        val newState = uiState.copy(
            rutError = Validators.validarRut(uiState.rut),
            nombreError = Validators.validarNombre(uiState.nombre),
            apellidoError = Validators.validarNombre(uiState.apellido)
        )

        val hasError = listOf(
            newState.rutError,
            newState.nombreError,
            newState.apellidoError
        ).any { it != null }

        // Si hay errores, actualizamos la UI y cortamos la ejecución
        if (hasError) {
            uiState = newState
            return
        }

        // 2. Si todo está bien, creamos el trámite a través del repositorio
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.createProcedure(
                rut = uiState.rut,
                nombre = uiState.nombre,
                apellido = uiState.apellido,
                tipoPersona = uiState.tipoPersona,
                tipoFirma = uiState.tipoFirma
            )

            if (result.isSuccess) {
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Error al crear el trámite"
                )
            }
        }
    }

    // Limpia el formulario una vez confirmado el trámite, para que no reaparezca el modal
    fun onTramiteConfirmado() {
        uiState = ProcedureUiState()
    }
}