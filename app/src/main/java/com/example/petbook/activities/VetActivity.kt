package com.example.petbook.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.petbook.R
import com.example.petbook.components.AlertDialogExample
import com.example.petbook.components.Card
import com.example.petbook.models.Vet
import com.example.petbook.ui.theme.PetBookTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class VetActivity: ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: MutableState<Location?> = mutableStateOf(null)
    private var showModal = mutableStateOf(false)
    private var vetList: Array<Vet> = arrayOf(Vet("Vet1", "Description", "", LatLng(20.730117, -103.428105)), Vet("Vet2", "Description", "", LatLng(20.730117, -103.428105)))

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            obtainLastLocation()
        } else {
            showModal.value = true
        }
    }

    private fun requestLocationPermission() : Boolean {
        return when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                true
            }

            else -> {
                false
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun obtainLastLocation() {
        if(!requestLocationPermission()) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    this.location.value = location
                }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtainLastLocation()
        setContent {
            val currentLocation by this.location
            val showModal by this.showModal
            PetBookTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding()),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Regresar",
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .size(32.dp)
                                        .clickable {
                                            finish()
                                        }
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.app_logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier
                                        .size(84.dp)
                                        .align(Alignment.Center)
                                )
                            }
                            Text(
                                "Veterinarios en tu zona",
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center
                            )
                            if (showModal) {
                                AlertDialogExample(
                                    onDismissRequest = {
                                        this@VetActivity.showModal.value = false
                                        finish()
                                    }, onConfirmation = {
                                        this@VetActivity.showModal.value = false
                                        obtainLastLocation()
                                    }, "Acceso a la ubicación", "Para continuar es necesario solicitar acceso a la localización"
                                )
                            }
                            for(vet in vetList) {
                                if(currentLocation != null) {
                                    val distance = calculateDistance(currentLocation!!, vet.location)
                                    if(distance < 5000)
                                    {
                                        Card(vet.name, vet.description, vet.image, onclick = {
                                            val intent = Intent(this@VetActivity, MapActivity::class.java)
                                            intent.putExtra("longitude", vet.location.longitude)
                                            intent.putExtra("latitude", vet.location.latitude)
                                            startActivity(intent)
                                        })
                                    }
                                }
                                else {
                                    Card(vet.name, vet.description, vet.image, onclick = {
                                        val intent = Intent(this@VetActivity, MapActivity::class.java)
                                        intent.putExtra("longitude", vet.location.longitude)
                                        intent.putExtra("latitude", vet.location.latitude)
                                        startActivity(intent)
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private fun calculateDistance(currentLocation: Location, target: LatLng) : Float {
        val results = FloatArray(1)
        val targetLocation = Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, target.latitude, target.longitude, results)
        return results[0]
    }
}