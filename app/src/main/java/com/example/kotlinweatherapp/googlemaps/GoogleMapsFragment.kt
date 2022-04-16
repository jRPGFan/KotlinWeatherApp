package com.example.kotlinweatherapp.googlemaps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentGoogleMapsMainBinding
import com.example.kotlinweatherapp.model.car.GoogleMapsCar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.fragment_google_maps_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.StringBuilder
import java.net.URL

class GoogleMapsFragment : Fragment() {

    private var _binding: FragmentGoogleMapsMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private lateinit var googleMapsCar: GoogleMapsCar

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initialPlace = LatLng(52.52000659999999, 13.404953999999975)
        googleMap.addMarker(
            MarkerOptions().position(initialPlace).title(getString(R.string.marker_start))
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        googleMap.setOnMapLongClickListener { latLng ->
            getAddressAsync(latLng)
            addMarkerToArray(latLng)
            drawLine()
            getRoute()
        }
        activateMyLocation(googleMap)
        googleMapsCar = GoogleMapsCar(map)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
        binding.buttonPlaceCar.setOnClickListener { placeCarOnMap() }
        binding.buttonDrive.setOnClickListener { startCar() }
        binding.buttonStop.setOnClickListener { stopCar() }
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)
            Thread {
                try {
                    val addresses =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    textAddress.post { textAddress.text = addresses[0].getAddressLine(0) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.GREEN)
                    .width(5f)
            )
        }
    }

    private fun getRoute() : List<LatLng>? {
        if (markers.size < 2) {
            Toast.makeText(requireContext(), "Place a marker first!", Toast.LENGTH_SHORT).show()
            return null
        }

        val url = getURL(markers[0].position, markers[markers.size - 1].position)
        var decodedPoints: List<LatLng>? = null

        GlobalScope.launch(context = Dispatchers.IO) {
            val result = URL(url).readText()
            val parser = Parser()
            val stringBuilder = StringBuilder(result)
            val json: JsonObject = parser.parse(stringBuilder) as JsonObject
            val routes = json.array<JsonObject>("routes")
            val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
            val polylinePoints = points.flatMap {
                PolyUtil.decode(it.obj("polyline")?.string("points")!!) }
            decodedPoints = polylinePoints
        }

        return decodedPoints
    }

    private fun getURL(from: LatLng, to: LatLng): String {
        val originationPoint = "origin" + from.latitude + "," + from.longitude
        val destinationPoint = "destination" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val queryParams = "$originationPoint&$destinationPoint&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$queryParams"
    }

    private fun placeCarOnMap() {
        if (markers.size > 0)
            googleMapsCar.placeCarOnMap(markers[0])
        else
            Toast.makeText(requireContext(), "Place a marker first!", Toast.LENGTH_SHORT).show()
    }

    private fun startCar() {
        getRoute()?.size ?: googleMapsCar.carStart(getRoute()!!)
    }

    private fun stopCar() {
        googleMapsCar.stop()
    }

    private fun initSearchByAddress() {
        binding.buttonSearch.setOnClickListener {
            val geocoder = Geocoder(it.context)
            val searchText = searchAddress.text.toString()
            Thread {
                try {
                    val addresses = geocoder.getFromLocationName(searchText, 1)
                    if (addresses.size > 0) {
                        goToAddress(addresses, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
        searchText: String
    ) {
        val location = LatLng(addresses[0].latitude, addresses[0].longitude)

        view.post {
            setMarker(location, searchText, R.drawable.ic_map_marker)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            googleMap.isMyLocationEnabled = isPermissionGranted
            googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted
        }
    }

    companion object {
        fun newInstance() = GoogleMapsFragment()
    }
}