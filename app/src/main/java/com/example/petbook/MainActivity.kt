package com.example.petbook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.petbook.activities.FeedActivity
import com.example.petbook.activities.LoginActivity
import com.example.petbook.activities.OnboardingPetForm
import com.example.petbook.activities.OnboardingProfileForm
import com.example.petbook.util.OnboardingStatus
import com.example.petbook.util.getCurrentUser
import com.example.petbook.util.getDocument
import com.example.petbook.util.storeDocument
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db: FirebaseFirestore = Firebase.firestore

        val currentUser = getCurrentUser()

        if (currentUser != null) {
            getDocument(db, "users", currentUser.uid, { _ ->
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }) { userData ->
                val onboardingStatus =
                    OnboardingStatus.valueOf(userData["onboardingStatus"].toString())
                val intent = when (onboardingStatus) {
                    OnboardingStatus.NOT_STARTED -> Intent(
                        this, OnboardingProfileForm::class.java
                    )

                    OnboardingStatus.PROFILE_COMPLETE -> Intent(
                        this, OnboardingPetForm::class.java
                    )

                    OnboardingStatus.ONBOARDING_COMPLETE -> Intent(
                        this, FeedActivity::class.java
                    )

                    OnboardingStatus.ERROR -> TODO()
                }
                startActivity(intent)
                finish()
            }
        }
        else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}