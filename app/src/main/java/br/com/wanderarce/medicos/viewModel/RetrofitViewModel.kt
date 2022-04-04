package br.com.wanderarce.medicos.viewModel

import android.util.Log
import androidx.lifecycle.*
import br.com.wanderarce.medicos.model.Estoque
import br.com.wanderarce.medicos.repository.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import retrofit2.HttpException

import java.io.IOException
import java.lang.Exception

class RetrofitViewModel(repositorio: EstoqueRepositorio) : ViewModel() {

    private val _response = MutableLiveData<List<Estoque?>>()

    val response:LiveData<List<Estoque?>>
        get() = _response

    private val _busca = MutableLiveData<String?>()

    val busca: LiveData<String?>
        get() = _busca

    private val _erro = MutableLiveData<String?>()

    val erro: LiveData<String?>
        get() = _erro

    init {

    }

    fun  fetchVacinas(nome: String): Job = viewModelScope.launch{
        Log.i("START-REQUEST-APP", "START")
        try {
            if(nome != null && nome!!.isNotEmpty())
                _response.value = RetrofitInstance.retrofit.unidades(nome).body()
                print(response.value)
        } catch (e: IOException) {
            Log.e("ERROR_APP", "${e.message}")
        } catch (e: HttpException){
            Log.e("ERROR_APP", "${e.message}")
        } catch (e: Exception) {
            Log.e("ERROR_APP", "${e.message}")
        }
    }
}

// ViewModelFactory :  Uma classe que cria ViewModels (BOILERPLATE)
class ViewModelFactory(private val repositorio: EstoqueRepositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RetrofitViewModel::class.java)){
            return RetrofitViewModel(repositorio) as T // Retorna a ViewModel
        }
        throw IllegalArgumentException("ViewModel Desconhecida")
    }
}