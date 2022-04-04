package br.com.wanderarce.medicos.model

data class Estoque(
    val `data`: String,//mudar
    val id: Int,
    val quantidade: Int,
    val unidade: Unidade,
    val vacina: Vacina
)