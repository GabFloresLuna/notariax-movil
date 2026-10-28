package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.AuthRepository
import cl.duoc.myapplication.data.repository.AuthRepositoryImpl
import cl.duoc.myapplication.model.MainMenuUiState
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(MainMenuUiState())
        private set

    fun cargarSesion() {
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val correo = repository.getUsuarioLogueado()

            uiState = uiState.copy(
                isLoading = false,
                nombreUsuario = correo ?: "",
                estaLogueado = correo != null
            )
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            repository.logout()
            uiState = uiState.copy(nombreUsuario = "", estaLogueado = false)
        }
    }
}
