package com.example.ocreaite.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ocreaite.viewmodels.AuthViewModel

@Composable
fun RegisterStep4Screen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }
    val usernameValidationState by viewModel.usernameValidationState.collectAsState()

    // Pegar savedStateHandle da Step1
    val step1Entry = navController.getBackStackEntry("register/step1")
    val savedStateHandle = step1Entry.savedStateHandle

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(usernameValidationState) {
        when (usernameValidationState) {
            is AuthViewModel.UsernameValidationState.Valid -> {
                // Salvar no savedStateHandle da Step1
                savedStateHandle.set("username", username)
                Log.d("RegisterStep4", "✅ Username saved: '$username'")

                viewModel.resetUsernameValidation()
                visible = false
                navController.navigate("register/step5")
            }
            is AuthViewModel.UsernameValidationState.Invalid -> {
                val message = (usernameValidationState as AuthViewModel.UsernameValidationState.Invalid).message
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.resetUsernameValidation()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        if (usernameValidationState is AuthViewModel.UsernameValidationState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val animatedTopProgress by animateFloatAsState(
                    targetValue = 0.8f,
                    animationSpec = tween(durationMillis = 600)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(Color(0xFFD9D9D9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedTopProgress)
                            .fillMaxHeight()
                            .background(Color(0xFF121212))
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            visible = false
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color(0xFF121212)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "How we can call you?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF121212)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Create your username",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF121212)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Username",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "your username",
                                color = Color.Gray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF121212),
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            cursorColor = Color(0xFF121212),
                            focusedTextColor = Color(0xFF121212),
                            unfocusedTextColor = Color(0xFF121212)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isButtonPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Button(
                        onClick = {
                            if (username.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Please enter a username",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.validateUsername(username)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .padding(bottom = 24.dp)
                            .graphicsLayer {
                                scaleX = buttonScale
                                scaleY = buttonScale
                            },
                        shape = RoundedCornerShape(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF121212),
                            contentColor = Color.White
                        ),
                        interactionSource = buttonInteractionSource
                    ) {
                        Text(
                            text = "Next",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}