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

class OldStatusViewModel(
    private val repository: ProcedureRepository = ProcedureRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(HistoryUiState())
        private set

    private val estadosFinalizados = listOf(ProcedureStatus.APROBADO, ProcedureStatus.RECHAZADO)

    init {
        loadHistory()
    }

    fun loadHistory() {
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val result = repository.getProcedures()

            if (result.isSuccess) {
                val tramites = result.getOrNull() ?: emptyList()

                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = null,
                    procedures = tramites.filter { it.estado in estadosFinalizados }
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