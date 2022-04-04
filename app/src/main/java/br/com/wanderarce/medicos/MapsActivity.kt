package br.com.wanderarce.medicos

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.wanderarce.medicos.databinding.ActivityMapsBinding
import br.com.wanderarce.medicos.model.Estoque
import br.com.wanderarce.medicos.viewModel.EstoqueRepositorio
import br.com.wanderarce.medicos.viewModel.RetrofitViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: RetrofitViewModel
    private lateinit var  repository:EstoqueRepositorio
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repository = EstoqueRepositorio()
        viewModel = RetrofitViewModel(repository)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnBuscar.setOnClickListener{
            if(binding.editTextSearch.text != null && binding.editTextSearch.text.isNotEmpty()) {
                Log.i("SHOW_APP", binding.editTextSearch.text.toString() )

                getUnidades(binding.editTextSearch.text.toString())
            }else {
                Toast.makeText(applicationContext, "Iforme uma vacina", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getUnidades(nome: String)  {

        viewModel.fetchVacinas(nome)
        var points:LatLng;
        if(viewModel.response.value != null ) {
            Toast.makeText(applicationContext, "Nenhum resultado encontrado", Toast.LENGTH_LONG).show()
        }
        if(viewModel.response.value != null && viewModel.response.value!!.isNotEmpty()) {
            val estoques = viewModel.response.value
            if (estoques != null) {
                for (estoque in estoques){
                    points = LatLng(estoque!!.unidade.latitude, estoque.unidade.longitutde)
                    setMarker(points, estoque)
                }
            }
        }
    }

    private fun setMarker(
        points: LatLng,
        estoque: Estoque
    ) {
        var markerOptions = MarkerOptions()
        markerOptions.position(points).title(estoque.unidade.nome).zIndex(1.0f)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            .draggable(true)
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(points));

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        // Add a marker in Sydney and move the camera
        val cg = LatLng(-20.513545, -54.654386)
        mMap.addMarker(MarkerOptions().position(cg).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cg,15.0f) )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cg))
    }

    fun geocodingRequest(endereco: String) : LatLng? {
        var geocoder = Geocoder(
            applicationContext,
            Locale.getDefault()
        )

        try {
            val local = geocoder.getFromLocationName(endereco, 1)
            if(local != null && local.size >0) {
                var destino = LatLng(local[0].latitude, local[0].longitude)
                return destino
            }
        } catch (e: IOException) {
            Log.e("ERROR-APP", "${e.message}")
        }
        return null
    }

    fun reverseGeocodingRequest(latLng: LatLng?) : String? {
        var geocoder = Geocoder(
            applicationContext,
            Locale.getDefault()
        )

        try {
            val local = latLng?.let { geocoder.getFromLocation(latLng.latitude, it.longitude, 1) }
            if(local != null && local.size >0) {

                return local[0].getAddressLine(0).toString()
            }
        } catch (e: IOException) {
            Log.e("ERROR-APP", "${e.message}")
        }
        return null
    }
}