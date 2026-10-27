package cl.duoc.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.myapplication.model.Procedure
import cl.duoc.myapplication.model.ProcedureStatus

/**
 * Tarjeta que resume un trámite: a quién pertenece, su tipo de firma,
 * cuántos documentos tiene y en qué estado está.
 * Se reutiliza en las pantallas de estado e historial.
 */
@Composable
fun ProcedureCard(
    procedure: Procedure,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = procedure.nombreCompleto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(estado = procedure.estado)
            }

            Text(
                text = "RUT: ${procedure.rut}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${procedure.tipoPersona} · Firma ${procedure.tipoFirma.lowercase()}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (procedure.mecanismoFirma.isNotBlank()) {
                Text(
                    text = "Mecanismo: ${procedure.mecanismoFirma}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Documentos: ${procedure.documentos.size} · Creado: ${procedure.fechaCreacion}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Etiqueta de color que indica el estado del trámite
@Composable
private fun StatusChip(estado: ProcedureStatus) {
    val (colorFondo, colorTexto) = coloresDelEstado(estado)

    Surface(
        shape = RoundedCornerShape(50),
        color = colorFondo
    ) {
        Text(
            text = etiquetaDelEstado(estado),
            style = MaterialTheme.typography.labelMedium,
            color = colorTexto,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

private fun etiquetaDelEstado(estado: ProcedureStatus): String {
    return when (estado) {
        ProcedureStatus.PENDIENTE -> "Pendiente"
        ProcedureStatus.EN_REVISION -> "En revisión"
        ProcedureStatus.APROBADO -> "Aprobado"
        ProcedureStatus.RECHAZADO -> "Rechazado"
    }
}

// Devuelve el par (color de fondo, color de texto) de cada estado
private fun coloresDelEstado(estado: ProcedureStatus): Pair<Color, Color> {
    return when (estado) {
        ProcedureStatus.PENDIENTE -> Color(0xFFFFE0B2) to Color(0xFF5D4037)
        ProcedureStatus.EN_REVISION -> Color(0xFFBBDEFB) to Color(0xFF0D47A1)
        ProcedureStatus.APROBADO -> Color(0xFFC8E6C9) to Color(0xFF1B5E20)
        ProcedureStatus.RECHAZADO -> Color(0xFFFFCDD2) to Color(0xFFB71C1C)
    }
}