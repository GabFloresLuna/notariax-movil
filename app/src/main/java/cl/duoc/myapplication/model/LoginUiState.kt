package cl.duoc.myapplication.model


/**
 * Estado de la interfaz para la pantalla de Login.
 */
data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    
    val correoError: String? = null,
    val passwordError: String? = null,
    
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false // Para saber si podemos navegar al menú principal
)