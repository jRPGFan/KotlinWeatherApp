package com.example.kotlinweatherapp.model.car

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class GoogleMapsCar(private val googleMap: GoogleMap) : ICarActions {

    private lateinit var car: Car
    private lateinit var carMarker: Marker
    private lateinit var carPosition: LatLng
    private var drivenDistance: Double = 0.0
    private lateinit var route: List<LatLng>
    private var routePivot = 0
//    private var carRotation: Float = 0.0F

    fun placeCarOnMap(marker: Marker) {
        car = Car(this)
        val carPosition = LatLng(marker.position.latitude, marker.position.longitude)
        carMarker = googleMap.addMarker(
            MarkerOptions().position(carPosition)
                .icon(BitmapDescriptorFactory.fromBitmap(car.setCarIcon()))
                .draggable(false)
                .anchor(0.5f,0.5f)
        )!!
        this.carPosition = carPosition
    }

    fun carStart(route: List<LatLng>) {
        this.route = route
        drawRoute(route)
        car.start()
    }

    private fun drawRoute(route: List<LatLng>) {
        val options = PolylineOptions()
            .color(Color.MAGENTA)
            .width(5f)
            .addAll(route)
        googleMap.addPolyline(options)
    }

//    private fun setCarRotation(start: LatLng, stop: LatLng) {
//        carRotation = positionToRotation(start, stop)
//    }

    override fun move() {
        drivenDistance += 0.001

        val lon: Double
        val lat: Double
        val lon1: Double
        val lat1: Double
        val lon2: Double
        val lat2: Double
        val yy: Double

        if (route[routePivot].longitude < route[routePivot + 1].longitude) {
            lon1 = route[routePivot].longitude
            lat1 = route[routePivot].latitude
            lon2 = route[routePivot + 1].longitude
            lat2 = route[routePivot + 1].latitude
            lat = lat1 - drivenDistance
            yy = lat2
        } else {
            lon1 = route[routePivot + 1].longitude
            lat1 = route[routePivot + 1].latitude
            lon2 = route[routePivot].longitude
            lat2 = route[routePivot].latitude
            lat = lat2 - drivenDistance
            yy = lat1
        }

        lon = ((lat - lat1) / (lat2 - lat1) * (lon2 - lon1)) + lon1

        if (lat <= yy) {
            routePivot++
            if (routePivot >= route.size - 1) {
                car.stop()
                routePivot = 0
                return
            }
            drivenDistance = 0.0
        }

        carMarker.position = LatLng(lat, lon)
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}