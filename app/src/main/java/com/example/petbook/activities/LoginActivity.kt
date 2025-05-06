package com.example.petbook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.petbook.R
import com.example.petbook.components.FormField
import com.example.petbook.ui.theme.PetBookTheme
import com.example.petbook.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.text.input.TextFieldState
import com.google.firebase.auth.ktx.auth

class LoginActivity : ComponentActivity() {

    private enum class AuthMode {
        SIGN_UP, LOG_IN
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        enableEdgeToEdge()
        setContent {
            var authMode by remember { mutableStateOf(AuthMode.LOG_IN) }

            PetBookTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(top = 28.dp)
                            .padding(top = innerPadding.calculateTopPadding()),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.petbook_logo),
                                    contentDescription = "petbook_logo",
                                    modifier = Modifier.size(width = 250.dp, height = 92.dp)
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.app_logo),
                                    contentDescription = "app_logo",
                                    modifier = Modifier.size(133.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.End
                            ) {
                                ElevatedButton(
                                    onClick = {
                                        authMode = if (authMode == AuthMode.SIGN_UP)
                                            AuthMode.LOG_IN else AuthMode.SIGN_UP
                                    },
                                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        when (authMode) {
                                            AuthMode.SIGN_UP -> "INICIAR SESIÓN"
                                            AuthMode.LOG_IN -> "REGISTRARME"
                                        }
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 16.dp),
                                shape = RoundedCornerShape(topStart = 64.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                val formFieldModifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)

                                when (authMode) {
                                    AuthMode.LOG_IN -> LoginForm(formFieldModifier)
                                    AuthMode.SIGN_UP -> SignupForm(formFieldModifier)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LoginForm(modifier: Modifier) {
        val emailTextFieldState = remember { TextFieldState() }
        val passwordTextFieldState = remember { TextFieldState() }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            FormField(
                text = "Correo electrónico:",
                modifier = modifier,
                inputType = KeyboardType.Email,
                textFieldState = emailTextFieldState
            )
            Spacer(modifier = Modifier.height(8.dp))
            FormField(
                text = "Introduzca su contraseña:",
                modifier = modifier,
                inputType = KeyboardType.Password,
                textFieldState = passwordTextFieldState,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.padding(vertical = 8.dp),
                onClick = {
                    val email = emailTextFieldState.text.toString()
                    val password = passwordTextFieldState.text.toString()

                    if (email.isEmpty()) {
                        Toast.makeText(this@LoginActivity, "El correo no puede estar vacío", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password.isEmpty()) {
                        Toast.makeText(this@LoginActivity, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    doLogin(email, password)
                }
            ) {
                Text("Continuar")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("¿Problemas para iniciar sesión?")
            Button(onClick = {
                val email = emailTextFieldState.text.toString()
                if (email.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Introduce tu correo electrónico", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                passwordReset(
                    auth,
                    email,
                    onEmailSent = {
                        Toast.makeText(this@LoginActivity, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                    },
                    onFail = { e ->
                        Toast.makeText(this@LoginActivity, "Error: $e", Toast.LENGTH_SHORT).show()
                    }
                )
            }) {
                Text("Olvidé mi contraseña")
            }
        }
    }

    @Composable
    private fun SignupForm(modifier: Modifier) {
        val emailTextFieldState = remember { TextFieldState() }
        val passwordTextFieldState = remember { TextFieldState() }
        val confirmPasswordTextFieldState = remember { TextFieldState() }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            FormField(
                text = "Correo electrónico:",
                modifier = modifier,
                inputType = KeyboardType.Email,
                textFieldState = emailTextFieldState
            )
            Spacer(modifier = Modifier.height(8.dp))
            FormField(
                text = "Elija una contraseña:",
                modifier = modifier,
                inputType = KeyboardType.Password,
                textFieldState = passwordTextFieldState,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            FormField(
                text = "Confirme su contraseña:",
                modifier = modifier,
                inputType = KeyboardType.Password,
                textFieldState = confirmPasswordTextFieldState,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.padding(vertical = 8.dp),
                onClick = {
                    val email = emailTextFieldState.text.toString()
                    val password = passwordTextFieldState.text.toString()
                    val confirmPassword = confirmPasswordTextFieldState.text.toString()

                    if (email.isEmpty()) {
                        Toast.makeText(this@LoginActivity, "El correo no puede estar vacío", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password.isEmpty()) {
                        Toast.makeText(this@LoginActivity, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password != confirmPassword) {
                        Toast.makeText(this@LoginActivity, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    doSignUp(email, password)
                }
            ) {
                Text("Continuar")
            }
        }
    }

    private fun doLogin(email: String, password: String) {
        loginFirebase(auth, email, password, onFail = {
            Toast.makeText(this, "El usuario no existe o sus credenciales son incorrectas", Toast.LENGTH_SHORT).show()
        }) { user ->
            getDocument(db, "users", user.uid, { _ ->
                storeDocument(
                    db,
                    "users",
                    user.uid,
                    hashMapOf("onboardingStatus" to OnboardingStatus.NOT_STARTED),
                    {
                        startActivity(Intent(this, OnboardingProfileForm::class.java))
                        finish()
                    },
                    { errorMessage ->
                        Toast.makeText(this, "Error Firestore: $errorMessage", Toast.LENGTH_LONG).show()
                    })
            }) { userData ->
                val onboardingStatus =
                    OnboardingStatus.valueOf(userData["onboardingStatus"].toString())
                val intent = when (onboardingStatus) {
                    OnboardingStatus.NOT_STARTED -> Intent(this, OnboardingProfileForm::class.java)
                    OnboardingStatus.PROFILE_COMPLETE -> Intent(this, OnboardingPetForm::class.java)
                    OnboardingStatus.ONBOARDING_COMPLETE -> Intent(this, FeedActivity::class.java)
                    OnboardingStatus.ERROR -> return@getDocument
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun doSignUp(email: String, password: String) {
        signUpFirebase(auth, email, password, onFail = {
            Toast.makeText(this, "Ha ocurrido un error al registrarse", Toast.LENGTH_SHORT).show()
        }) { user ->
            storeDocument(
                db,
                "users",
                user.uid,
                hashMapOf("onboardingStatus" to OnboardingStatus.NOT_STARTED),
                {
                    startActivity(Intent(this, OnboardingProfileForm::class.java))
                    finish()
                },
                { errorMessage ->
                    Toast.makeText(this, "Error Firestore: $errorMessage", Toast.LENGTH_LONG).show()
                })
        }
    }
}
