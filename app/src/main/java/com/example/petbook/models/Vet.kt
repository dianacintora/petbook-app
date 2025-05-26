package com.example.petbook.models

import android.graphics.Bitmap
import com.example.petbook.util.base64toBitmap
import com.google.android.gms.maps.model.LatLng

class Vet(val name: String, val description: String, image: String, val location: LatLng) {
    val image: Bitmap? = base64toBitmap(image)
}