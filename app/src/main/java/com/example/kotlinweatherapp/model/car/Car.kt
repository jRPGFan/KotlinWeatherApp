package com.example.kotlinweatherapp.model.car

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.kotlinweatherapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DEFAULT_CAR_SPEED = 1000L

class Car(private var carActions: ICarActions) {
    private var moving = false
    private var movementSpeed = DEFAULT_CAR_SPEED

    fun setCarIcon() : Bitmap {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = 4
        return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.car_top_view_icon, bitmapOptions)
    }

    fun start() {
        moving = true
        drive()
    }

    fun stop() {
        moving = false
        carActions.stop()
    }

    private fun drive() {
        GlobalScope.launch(context = Dispatchers.Main) {
            while (moving) {
                carActions.move()
                delay(movementSpeed)
            }
        }
    }
}