package br.com.wanderarce.medicos.interfaces

import br.com.wanderarce.medicos.model.Estoque
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    @GET("estoques/vacina/{nome}")
    suspend fun unidades(@Path("nome") nome: String) : Response<List<Estoque>>
}