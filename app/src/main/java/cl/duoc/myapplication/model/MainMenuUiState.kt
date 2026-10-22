package cl.duoc.myapplication.model
/**
 * Estado de la interfaz para el Menú Principal.
 */
data class MainMenuUiState(
    val nombreUsuario: String = "", // Para mostrar un saludo tipo "Hola, Juan"
    val isLoading: Boolean = false
)