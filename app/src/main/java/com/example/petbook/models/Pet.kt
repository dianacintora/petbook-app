package com.example.petbook.models

import android.graphics.Bitmap
import com.example.petbook.util.base64toBitmap

class Pet(name: String, age: Int, description: String, image: String) {
    val name: String = name
    val age: Int = age
    val description: String = description
    val image: Bitmap? = base64toBitmap(image)
}