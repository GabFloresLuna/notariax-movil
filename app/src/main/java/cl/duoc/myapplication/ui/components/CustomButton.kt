package cl.duoc.myapplication.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tipos de botón disponibles para el diseño.
 */
enum class ButtonType {
    PRIMARY,   // Botón sólido (ej: "Login", "Iniciar")
    SECONDARY, // Botón con borde (ej: "Ver estado")
    TEXT       // Botón solo texto (ej: "Olvidé contraseña")
}

/**
 * Componente de botón reutilizable para toda la aplicación.
 *
 * @param text El texto que mostrará el botón.
 * @param onClick Acción que se ejecuta al presionar.
 * @param modifier Modificador para personalizar tamaño o márgenes.
 * @param type Tipo de botón (PRIMARY, SECONDARY, TEXT).
 * @param isLoading Si es true, muestra un indicador de carga y deshabilita el botón.
 * @param enabled Si es false, el botón se deshabilita.
 * @param icon Ícono opcional (si se proporciona, aparece a la izquierda del texto).
 * @param containerColor Color de fondo personalizado (opcional).
 * @param contentColor Color del texto/ícono personalizado (opcional).
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    containerColor: Color? = null,
    contentColor: Color? = null
) {
    // Determinamos los colores según el tipo de botón o si se pasan manualmente
    val finalContainerColor = containerColor ?: when (type) {
        ButtonType.PRIMARY -> MaterialTheme.colorScheme.primary
        ButtonType.SECONDARY -> Color.Transparent
        ButtonType.TEXT -> Color.Transparent
    }

    val finalContentColor = contentColor ?: when (type) {
        ButtonType.PRIMARY -> MaterialTheme.colorScheme.onPrimary
        ButtonType.SECONDARY -> MaterialTheme.colorScheme.primary
        ButtonType.TEXT -> MaterialTheme.colorScheme.primary
    }

    // Contenido interno del botón (ya sea texto, ícono o loader)
    val content: @Composable () -> Unit = {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = finalContentColor,
                strokeWidth = 2.dp
            )
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                androidx.compose.foundation.layout.Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    // Renderizamos el botón según su tipo
    when (type) {
        ButtonType.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp), // Altura estándar para botones de la app
                enabled = enabled && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = finalContainerColor,
                    contentColor = finalContentColor
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                content()
            }
        }

        ButtonType.SECONDARY -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = enabled && !isLoading,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = finalContentColor
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, finalContentColor),
                shape = MaterialTheme.shapes.medium
            ) {
                content()
            }
        }

        ButtonType.TEXT -> {
            TextButton(
                onClick = onClick,
                modifier = modifier, // No forzamos ancho completo para botones de texto
                enabled = enabled && !isLoading,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = finalContentColor
                )
            ) {
                content()
            }
        }
    }
}