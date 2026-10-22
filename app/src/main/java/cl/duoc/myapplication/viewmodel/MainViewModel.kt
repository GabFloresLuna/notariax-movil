package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.model.MainMenuUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var uiState by mutableStateOf(MainMenuUiState())
        private set

    // Simulamos que este ViewModel se inicializa al entrar a la pantalla
    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        uiState = uiState.copy(isLoading = true)
        
        viewModelScope.launch {
            delay(500) // Simulamos traer los datos del usuario desde el servidor o base de datos local
            // En un caso real, aquí llamarías a un UserRepository.getUserProfile()
            
            uiState = uiState.copy(
                isLoading = false,
                nombreUsuario = "Usuario Notaría" // Aquí iría el nombre real
            )
        }
    }

    /**
     * Lógica para cerrar sesión.
     * Aquí es donde en el futuro limpiarías los tokens de sesión (SharedPreferences, DataStore).
     */
    fun onLogoutClick() {
        // Lógica para borrar la sesión...
        // (La navegación al Login se manejará desde la UI observando un estado o llamando al NavController)
    }
}