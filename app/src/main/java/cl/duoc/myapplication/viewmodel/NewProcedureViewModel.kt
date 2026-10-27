package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.ProcedureRepository
import cl.duoc.myapplication.data.repository.ProcedureRepositoryImpl
import cl.duoc.myapplication.model.ProcedureUiState
import kotlinx.coroutines.launch

class NewProcedureViewModel(
    private val repository: ProcedureRepository = ProcedureRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(ProcedureUiState())
        private set

    fun onTipoPersonaChange(tipo: String) {
        uiState = uiState.copy(tipoPersona = tipo)
    }
    fun onRutChange(rut: String) {
        uiState = uiState.copy(rut = rut, rutError = null)
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

    fun onIniciarClick() {
        var hasError = false
        var newState = uiState

        if (uiState.rut.isBlank()) {
            newState = newState.copy(rutError = "El RUT es obligatorio"); hasError = true
        }
        if (uiState.nombre.isBlank()) {
            newState = newState.copy(nombreError = "El nombre es obligatorio"); hasError = true
        }
        if (uiState.apellido.isBlank()) {
            newState = newState.copy(apellidoError = "El apellido es obligatorio"); hasError = true
        }

        if (hasError) {
            uiState = newState
            return
        }

        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val result = repository.createProcedure(
                rut = uiState.rut,
                nombre = uiState.nombre,
                apellido = uiState.apellido,
                tipoPersona = uiState.tipoPersona,
                tipoFirma = uiState.tipoFirma
            )

            if (result.isSuccess) {
                // Podríamos tener un isSuccess en ProcedureUiState para navegar
                uiState = uiState.copy(isLoading = false)
                // Aquí podrías limpiar el formulario o navegar atrás
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    // Puedes agregar un campo general de error si lo deseas
                )
            }
        }
    }
}