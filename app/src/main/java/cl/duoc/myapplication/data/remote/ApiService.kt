package cl.duoc.myapplication.data.remote

import cl.duoc.myapplication.model.AuthResponse
import cl.duoc.myapplication.model.DocumentoRequest
import cl.duoc.myapplication.model.DocumentoResponse
import cl.duoc.myapplication.model.LoginRequest
import cl.duoc.myapplication.model.MeResponse
import cl.duoc.myapplication.model.ProcedureRequest
import cl.duoc.myapplication.model.ProcedureResponse
import cl.duoc.myapplication.model.RecoverRequest
import cl.duoc.myapplication.model.RecoverResponse
import cl.duoc.myapplication.model.SignupRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Los endpoints con token reciben el valor "Bearer <authToken>" en el encabezado Authorization
interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("auth/recover")
    suspend fun recover(@Body request: RecoverRequest): RecoverResponse

    @GET("auth/me")
    suspend fun me(@Header("Authorization") token: String): MeResponse

    @GET("procedure")
    suspend fun getProcedures(@Header("Authorization") token: String): List<ProcedureResponse>

    @GET("procedure/{id}")
    suspend fun getProcedure(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): ProcedureResponse

    @POST("procedure")
    suspend fun createProcedure(
        @Header("Authorization") token: String,
        @Body request: ProcedureRequest
    ): ProcedureResponse

    @GET("documento")
    suspend fun getDocumentos(
        @Header("Authorization") token: String,
        @Query("procedure_id") procedureId: Int
    ): List<DocumentoResponse>

    @POST("documento")
    suspend fun createDocumento(
        @Header("Authorization") token: String,
        @Body request: DocumentoRequest
    ): DocumentoResponse
}
