package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.ProcedureRepository
import cl.duoc.myapplication.data.repository.ProcedureRepositoryImpl
import cl.duoc.myapplication.model.HistoryUiState
import cl.duoc.myapplication.model.ProcedureStatus
import kotlinx.coroutines.launch

class StatusViewModel(
    // Se usa el repositorio, en caso tal de estar vacio se usa el Impl
    private val repository: ProcedureRepository = ProcedureRepositoryImpl()
) : ViewModel() {

    // El estado de la pantalla
    var uiState by mutableStateOf(HistoryUiState())
        private set

    // Tramites en curso
    private val estadosEnCurso = listOf(ProcedureStatus.PENDIENTE, ProcedureStatus.EN_REVISION)

    // Se carga automáticamente al entrar a la pantalla
    init {
        loadProcedures()
    }

    fun loadProcedures() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.getProcedures()

            if (result.isSuccess) {
                val tramites = result.getOrNull() ?: emptyList()

                uiState = uiState.copy(
                    isLoading = false,
                    procedures = tramites.filter { it.estado in estadosEnCurso }
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Error al cargar"
                )
            }
        }
    }
}