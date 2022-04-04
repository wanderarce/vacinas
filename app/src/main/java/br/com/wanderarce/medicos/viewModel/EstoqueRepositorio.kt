package br.com.wanderarce.medicos.viewModel

import br.com.wanderarce.medicos.model.Estoque
import br.com.wanderarce.medicos.repository.RetrofitInstance

class EstoqueRepositorio() {

    suspend fun fetchUnidades(nome:String): List<Estoque>? {
        return RetrofitInstance.retrofit.unidades(nome ).body()
    }
}
