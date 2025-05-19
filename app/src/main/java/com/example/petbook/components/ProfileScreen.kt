package com.example.petbook.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petbook.R
import com.example.petbook.activities.FeedActivity

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
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
                        context.startActivity(Intent(context, FeedActivity::class.java))
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
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", modifier = Modifier.size(32.dp).padding(horizontal = 4.dp))
                Icon(Icons.Default.Settings, contentDescription = "Configuración", modifier = Modifier.size(32.dp).padding(horizontal = 4.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_chat_bubble_outline),
                    contentDescription = "Mensaje",
                    modifier = Modifier.size(32.dp).padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Imagen de perfil con lupa y círculo rosa
        Box(contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(160.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.kero_selfie),
                    contentDescription = "Foto Perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .scale(1.08f)
                )

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
            "@kero",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF495C8E)
        )
        Text("GDL", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text("Zona Oblatos", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Datos de la mascota centrados
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileDataLine("Edad:", "3 años")
            ProfileDataLine("Peso:", "7.8 kg")
            ProfileDataLine("Raza:", "Maltés")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Acción editar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF495C8E)),
                modifier = Modifier.height(36.dp)
            ) {
                Text("EDITAR PERFIL", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                    "✦ @kero actualizó su foto",
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
                    text = "✦ @kero calificó una clínica veterinaria",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Veterinaria Droppy", color = Color(0xFF00B894), fontSize = 16.sp)
                Text("9.5", color = Color(0xFF00B894), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
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
