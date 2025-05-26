package com.example.petbook.models

import android.graphics.Bitmap
import com.example.petbook.util.base64toBitmap

class Pet(val name: String, val age: Int, val description: String, image: String) {
    val image: Bitmap? = base64toBitmap(image)
}