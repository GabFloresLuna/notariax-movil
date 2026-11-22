package cl.duoc.myapplication.model

import com.google.gson.annotations.SerializedName

// Cuerpo que se envía a POST /procedure
data class ProcedureRequest(
    val rut: String,
    @SerializedName("nombre_completo") val nombreCompleto: String,
    @SerializedName("tipo_persona") val tipoPersona: String,
    @SerializedName("tipo_firma") val tipoFirma: String,
    val direccion: String,
    val representante: String,
    @SerializedName("mecanismo_firma") val mecanismoFirma: String
)

// Trámite tal como lo devuelve Xano (los textos opcionales pueden venir nulos)
data class ProcedureResponse(
    val id: Int,
    @SerializedName("created_at") val createdAt: Long?,
    val rut: String?,
    @SerializedName("nombre_completo") val nombreCompleto: String?,
    @SerializedName("tipo_persona") val tipoPersona: String?,
    @SerializedName("tipo_firma") val tipoFirma: String?,
    val estado: String?,
    val direccion: String?,
    val representante: String?,
    @SerializedName("mecanismo_firma") val mecanismoFirma: String?
)

// Cuerpo que se envía a POST /documento
data class DocumentoRequest(
    @SerializedName("procedure_id") val procedureId: Int,
    val nombre: String,
    val uri: String
)

// Documento tal como lo devuelve Xano
data class DocumentoResponse(
    val id: Int,
    val nombre: String?,
    val uri: String?
)
