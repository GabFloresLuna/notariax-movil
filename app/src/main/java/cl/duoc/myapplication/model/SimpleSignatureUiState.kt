package cl.duoc.myapplication.model

import androidx.compose.ui.geometry.Offset

/**
 * Estado de la pantalla de firma simple (firma manuscrita digital).
 *
 * @param trazos Cada trazo es la lista de puntos que el usuario dibujó sin levantar el dedo.
 * @param errorMessage Mensaje de error si intenta firmar sin dibujar nada.
 */
data class SimpleSignatureUiState(
    val trazos: List<List<Offset>> = emptyList(),
    val errorMessage: String? = null
)