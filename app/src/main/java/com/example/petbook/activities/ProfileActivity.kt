package com.example.petbook.activities

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petbook.R
import com.example.petbook.util.Base64toBitmap
import com.example.petbook.util.getCurrentUser
import com.example.petbook.util.getDocument
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class ProfileActivity : ComponentActivity() {

    private lateinit var db: FirebaseFirestore
    private var profilePictureState: MutableState<Bitmap?> = mutableStateOf(null)
    private var usernameState: MutableState<String> = mutableStateOf("")
    private var townState: MutableState<String> = mutableStateOf("")


    override fun onCreate(savedInstanceState: Bundle?) {
        db = Firebase.firestore
        loadProfile(db)
        super.onCreate(savedInstanceState)
        setContent {
            val profilePicture by profilePictureState
            val username by usernameState
            val town by townState
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFCCE1F9))
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // 🔹 Top bar
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

                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(horizontal = 4.dp)
                        )
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Configuración",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(horizontal = 4.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_chat_bubble_outline),
                            contentDescription = "Mensaje",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(horizontal = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🔹 Imagen de perfil con lupa y círculo rosa
                Box(contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(160.dp)) {
                        profilePicture?.asImageBitmap()?.let {
                            Image(
                                bitmap = it,
                                contentDescription = "Foto Perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(CircleShape)
                                    .scale(1.08f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.TopStart)
                                .offset(x = 10.dp, y = 4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFC6D8))
                        )
                    }

                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Nombre y ubicación
                Text(
                    "@${username}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF495C8E)
                )
                Text(town, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Datos de la mascota centrados
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //ProfileDataLine("Edad:", "3 años")
                    //ProfileDataLine("Peso:", "7.8 kg")
                    //ProfileDataLine("Raza:", "Maltés")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /* Acción editar */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF495C8E)),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            "EDITAR PERFIL",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Publicación
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "✦ @${username} actualizó su foto",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Image(
                            painter = painterResource(id = R.drawable.kero_selfie),
                            contentDescription = "Post",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .scale(1.1f),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                            Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 🔹 Calificación veterinaria
                Card(
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✦ @${username} calificó una clínica veterinaria",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Veterinaria Droppy", color = Color(0xFF00B894), fontSize = 16.sp)
                        Text(
                            "9.5",
                            color = Color(0xFF00B894),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    private fun loadProfile(db: FirebaseFirestore) {
        getDocument(db, "users", "${getCurrentUser()?.uid}",
            { _ ->
                print("Fail")
            }) { profileData ->
                profilePictureState.value = Base64toBitmap(profileData["profilePicture"].toString())
                usernameState.value = profileData["nickname"].toString()
                townState.value = profileData["town"].toString()
        }
    }
}

@Composable
fun ProfileDataLine(label: String, value: String) {
    Text(
        buildAnnotatedString {
            append("$label ")
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), 0, label.length)
            append(value)
        },
        fontSize = 14.sp
    )
}