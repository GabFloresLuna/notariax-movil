package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.ProcedureRepository
import cl.duoc.myapplication.data.repository.ProcedureRepositoryImpl
import cl.duoc.myapplication.model.Documento
import cl.duoc.myapplication.model.ProcedureUiState
import cl.duoc.myapplication.model.TipoPersona
import cl.duoc.myapplication.validation.Validators
import kotlinx.coroutines.launch

class NewProcedureViewModel(
    private val repository: ProcedureRepository = ProcedureRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf(ProcedureUiState())
        private set

    fun onTipoPersonaChange(tipo: String) {
        if (tipo == uiState.tipoPersona) return

        uiState = ProcedureUiState(
            tipoPersona = tipo,
            tipoFirma = uiState.tipoFirma,
            documentos = uiState.documentos
        )
    }

    fun onRutChange(rut: String) {
        uiState = uiState.copy(rut = rut, rutError = null)
    }

    fun onNombreChange(nombre: String) {
        uiState = uiState.copy(nombre = nombre, nombreError = null)
    }

    fun onApellidoChange(apellido: String) {
        uiState = uiState.copy(apellido = apellido, apellidoError = null)
    }

    fun onRazonSocialChange(razonSocial: String) {
        uiState = uiState.copy(razonSocial = razonSocial, razonSocialError = null)
    }

    fun onDireccionChange(direccion: String) {
        uiState = uiState.copy(direccion = direccion, direccionError = null)
    }

    fun onRepresentanteChange(representante: String) {
        uiState = uiState.copy(representante = representante, representanteError = null)
    }

    fun onTipoFirmaChange(firma: String) {
        uiState = uiState.copy(tipoFirma = firma)
    }

    fun onDocumentosSeleccionados(nuevosDocumentos: List<Documento>) {
        val documentosActuales = uiState.documentos
        val documentosAgregados = nuevosDocumentos.filter { nuevo ->
            documentosActuales.none { it.uri == nuevo.uri }
        }

        uiState = uiState.copy(
            documentos = documentosActuales + documentosAgregados,
            documentosError = null
        )
    }

    fun onDocumentoQuitado(documento: Documento) {
        uiState = uiState.copy(documentos = uiState.documentos - documento)
    }

    fun onIniciarClick() {
        val esEmpresa = uiState.tipoPersona == TipoPersona.EMPRESA

        val estadoConDocumentos = uiState.copy(
            documentosError = Validators.validarDocumentos(uiState.documentos.size)
        )

        val newState = if (esEmpresa) {
            estadoConDocumentos.copy(
                rutError = Validators.validarRut(uiState.rut),
                razonSocialError = Validators.validarRazonSocial(uiState.razonSocial),
                direccionError = Validators.validarDireccion(uiState.direccion),
                representanteError = Validators.validarNombre(uiState.representante)
            )
        } else {
            estadoConDocumentos.copy(
                rutError = Validators.validarRut(uiState.rut),
                nombreError = Validators.validarNombre(uiState.nombre),
                apellidoError = Validators.validarNombre(uiState.apellido)
            )
        }

        val hasError = listOf(
            newState.rutError,
            newState.nombreError,
            newState.apellidoError,
            newState.razonSocialError,
            newState.direccionError,
            newState.representanteError,
            newState.documentosError
        ).any { it != null }

        if (hasError) {
            uiState = newState
            return
        }

        uiState = newState.copy(listoParaFirmar = true)
    }

    fun onFirmaIniciada() {
        uiState = uiState.copy(listoParaFirmar = false)
    }

    fun onFirmaCompletada(mecanismoFirma: String) {
        val esEmpresa = uiState.tipoPersona == TipoPersona.EMPRESA

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val nombreCompleto = if (esEmpresa) {
                uiState.razonSocial
            } else {
                "${uiState.nombre} ${uiState.apellido}"
            }

            val result = repository.createProcedure(
                rut = uiState.rut,
                nombreCompleto = nombreCompleto,
                tipoPersona = uiState.tipoPersona,
                tipoFirma = uiState.tipoFirma,
                direccion = uiState.direccion,
                representante = uiState.representante,
                documentos = uiState.documentos,
                mecanismoFirma = mecanismoFirma
            )

            if (result.isSuccess) {
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Error al crear el trámite"
                )
            }
        }
    }

    fun onTramiteConfirmado() {
        uiState = ProcedureUiState()
    }
}