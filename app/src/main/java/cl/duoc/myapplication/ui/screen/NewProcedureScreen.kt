package cl.duoc.myapplication.ui.screen

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.model.Documento
import cl.duoc.myapplication.model.TipoFirma
import cl.duoc.myapplication.model.TipoPersona
import cl.duoc.myapplication.ui.components.AppHeader
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.ui.components.CustomRadioGroup
import cl.duoc.myapplication.ui.components.CustomTextField
import cl.duoc.myapplication.viewmodel.NewProcedureViewModel
import java.io.File

@Composable
fun NewProcedureScreen(
    viewModel: NewProcedureViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSignature: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    val selectorDeDocumentos = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        val documentos = uris.map { uri ->
            Documento(uri = uri.toString(), nombre = obtenerNombreArchivo(context, uri))
        }
        viewModel.onDocumentosSeleccionados(documentos)
    }

    // Ruta de la foto que se está tomando, para usarla cuando la cámara responde
    var fotoPendiente by rememberSaveable { mutableStateOf<String?>(null) }

    // Cámara del sistema: guarda la foto en el archivo que le pasamos
    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { fotoTomada ->
        val ruta = fotoPendiente
        if (ruta != null) {
            val archivo = File(ruta)
            if (fotoTomada) {
                viewModel.onDocumentosSeleccionados(
                    listOf(Documento(uri = obtenerUriDeFoto(context, archivo).toString(), nombre = archivo.name))
                )
            } else {
                archivo.delete() // El usuario canceló: no dejamos un archivo vacío
            }
        }
        fotoPendiente = null
    }

    LaunchedEffect(state.listoParaFirmar) {
        if (state.listoParaFirmar) {
            viewModel.onFirmaIniciada()
            onNavigateToSignature(state.tipoFirma)
        }
    }

    Scaffold(
        topBar = {
            AppHeader(title = "Ingresar trámite", onBackClick = onNavigateBack)
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomRadioGroup(
                options = listOf(TipoPersona.NATURAL, TipoPersona.EMPRESA),
                selectedOption = state.tipoPersona,
                onOptionSelected = { viewModel.onTipoPersonaChange(it) }
            )

            AnimatedContent(
                targetState = state.tipoPersona,
                label = "camposSegunTipoPersona"
            ) { tipoPersona ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (tipoPersona == TipoPersona.EMPRESA) {
                        CustomTextField(
                            value = state.rut,
                            onValueChange = { viewModel.onRutChange(it) },
                            label = "RUT de la empresa",
                            placeholder = "76.123.456-0",
                            leadingIcon = Icons.Filled.Badge,
                            isError = state.rutError != null,
                            errorMessage = state.rutError
                        )

                        CustomTextField(
                            value = state.razonSocial,
                            onValueChange = { viewModel.onRazonSocialChange(it) },
                            label = "Razón social",
                            leadingIcon = Icons.Filled.Business,
                            isError = state.razonSocialError != null,
                            errorMessage = state.razonSocialError
                        )

                        CustomTextField(
                            value = state.direccion,
                            onValueChange = { viewModel.onDireccionChange(it) },
                            label = "Dirección",
                            leadingIcon = Icons.Filled.LocationOn,
                            isError = state.direccionError != null,
                            errorMessage = state.direccionError
                        )

                        CustomTextField(
                            value = state.representante,
                            onValueChange = { viewModel.onRepresentanteChange(it) },
                            label = "Representante legal",
                            leadingIcon = Icons.Filled.Person,
                            isError = state.representanteError != null,
                            errorMessage = state.representanteError
                        )
                    } else {
                        CustomTextField(
                            value = state.rut,
                            onValueChange = { viewModel.onRutChange(it) },
                            label = "RUT",
                            placeholder = "12.345.678-5",
                            leadingIcon = Icons.Filled.Badge,
                            isError = state.rutError != null,
                            errorMessage = state.rutError
                        )

                        CustomTextField(
                            value = state.nombre,
                            onValueChange = { viewModel.onNombreChange(it) },
                            label = "Nombre",
                            leadingIcon = Icons.Filled.Person,
                            isError = state.nombreError != null,
                            errorMessage = state.nombreError
                        )

                        CustomTextField(
                            value = state.apellido,
                            onValueChange = { viewModel.onApellidoChange(it) },
                            label = "Apellido",
                            leadingIcon = Icons.Filled.Person,
                            isError = state.apellidoError != null,
                            errorMessage = state.apellidoError
                        )
                    }
                }
            }

            Text(
                text = "Documentos del trámite",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Adjunta o fotografía la hoja del trámite. Usa solo documentos de prueba, sin datos reales.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CustomButton(
                text = "Adjuntar documentos",
                onClick = { selectorDeDocumentos.launch(arrayOf("application/pdf", "image/*")) },
                type = ButtonType.SECONDARY
            )

            CustomButton(
                text = "Tomar foto",
                onClick = {
                    val archivo = crearArchivoDeFoto(context)
                    fotoPendiente = archivo.absolutePath
                    camara.launch(obtenerUriDeFoto(context, archivo))
                },
                type = ButtonType.SECONDARY
            )

            state.documentos.forEach { documento ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Description,
                        contentDescription = null
                    )
                    Text(
                        text = documento.nombre,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(onClick = { viewModel.onDocumentoQuitado(documento) }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Quitar documento"
                        )
                    }
                }
            }

            if (state.documentosError != null) {
                Text(
                    text = state.documentosError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "Tipo de firma",
                style = MaterialTheme.typography.titleMedium
            )

            CustomRadioGroup(
                options = listOf(TipoFirma.SIMPLE, TipoFirma.AVANZADA),
                selectedOption = state.tipoFirma,
                onOptionSelected = { viewModel.onTipoFirmaChange(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomButton(
                text = "Iniciar",
                onClick = { viewModel.onIniciarClick() },
                type = ButtonType.PRIMARY
            )
        }
    }
}

private fun obtenerNombreArchivo(context: Context, uri: Uri): String {
    context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME),
        null,
        null,
        null
    )?.use { cursor ->
        val columnaNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && columnaNombre >= 0) {
            return cursor.getString(columnaNombre)
        }
    }
    return "Documento"
}

// Crea el archivo vacío donde la cámara va a guardar la foto
private fun crearArchivoDeFoto(context: Context): File {
    val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
    return File(carpeta, "foto_tramite_${System.currentTimeMillis()}.jpg")
}

// Uri segura (FileProvider) para que la cámara pueda escribir en nuestro archivo
private fun obtenerUriDeFoto(context: Context, archivo: File): Uri {
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
}