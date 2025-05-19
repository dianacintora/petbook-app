package com.example.petbook.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.petbook.components.PostCard
import com.example.petbook.components.PostCardWithDrawer

class FeedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            // Aquí ya mandas a llamar tu interfaz
            PostCard()
            PostCardWithDrawer()
        }
    }
}