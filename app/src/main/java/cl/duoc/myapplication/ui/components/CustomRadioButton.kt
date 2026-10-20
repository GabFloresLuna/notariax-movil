package cl.duoc.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Componente reutilizable para agrupar botones de selección única (Radio Buttons).
 *
 * @param options Lista de opciones (ej: listOf("Persona natural", "Empresa")).
 * @param selectedOption La opción que está actualmente seleccionada.
 * @param onOptionSelected Callback que devuelve la opción elegida al hacer clic.
 * @param modifier Modificador para márgenes o tamaños.
 * @param isHorizontal Si es true, se muestran en fila; si es false, en columna.
 */
@Composable
fun CustomRadioGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = true // Por defecto en fila (como en tu prototipo)
) {
    // Contenedor principal que decide si es Fila o Columna
    val containerModifier = modifier
        .fillMaxWidth()
        .selectableGroup() // Mejora la accesibilidad

    val content: @Composable () -> Unit = {
        options.forEach { text ->
            Row(
                modifier = Modifier
                    // Hacemos que toda la fila (botón + texto) sea clickeable, no solo el círculo
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(end = 16.dp, bottom = if (isHorizontal) 0.dp else 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null, // null porque el clic ya lo maneja la fila completa
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

    if (isHorizontal) {
        Row(
            modifier = containerModifier,
            horizontalArrangement = Arrangement.Start
        ) {
            content()
        }
    } else {
        Column(modifier = containerModifier) {
            content()
        }
    }
}