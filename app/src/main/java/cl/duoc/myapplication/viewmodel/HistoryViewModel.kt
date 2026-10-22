package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.ProcedureRepository
import cl.duoc.myapplication.data.repository.ProcedureRepositoryImpl
import cl.duoc.myapplication.model.HistoryUiState
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: ProcedureRepository = ProcedureRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(HistoryUiState())
        private set

    // Esta función se llamará automáticamente al entrar a la pantalla
    fun loadHistory() {
        uiState = uiState.copy(isLoading = true)
        
        viewModelScope.launch {
            val result = repository.getProcedures()
            
            if (result.isSuccess) {
                uiState = uiState.copy(
                    isLoading = false,
                    procedures = result.getOrNull() ?: emptyList()
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