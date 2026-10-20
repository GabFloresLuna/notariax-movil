package cl.duoc.myapplication.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

/**
 * Barra superior (TopAppBar) reutilizable para las pantallas de la app.
 *
 * @param title El título que aparecerá en la barra (ej: "LOGIN", "Ingresar tramite").
 * @param onBackClick Callback opcional. Si se proporciona, muestra la flecha de volver atrás.
 * @param actions Acciones opcionales (íconos) que aparecen a la derecha.
 * @param modifier Modificador para personalizar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = {
            // Solo mostramos el ícono si onBackClick no es nulo
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atrás"
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface, // Color de fondo
            titleContentColor = MaterialTheme.colorScheme.onSurface, // Color del texto
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface // Color del ícono
        )
    )
}