package cl.duoc.myapplication.model

/**
 * Estado de la interfaz para las pantallas que muestran listas de trámites
 * (Historial y Ver estado de trámite).
 */
data class HistoryUiState(
    val procedures: List<Procedure> = emptyList(), // Lista de trámites a mostrar
    val isLoading: Boolean = false,                // Mientras carga la lista del servidor
    val errorMessage: String? = null               // Si falla la carga (ej: sin internet)
)