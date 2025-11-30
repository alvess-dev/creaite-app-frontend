package com.example.ocreaite.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ocreaite.R

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var navigateBack by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("from_navigation", true)
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Botão voltar
        IconButton(
            onClick = {
                visible = false
                navigateBack = true
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp)
                .align(Alignment.TopStart),
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color(0xFF121212)
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(250)
            ) + fadeIn(animationSpec = tween(250)),
            exit = fadeOut(animationSpec = tween(0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                // Título
                Text(
                    text = "Log in",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF121212)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Email/Phone field
                Text(
                    text = "Email address/phone number",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF121212),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF121212),
                        unfocusedBorderColor = Color(0xFFD9D9D9),
                        cursorColor = Color(0xFF121212),
                        focusedTextColor = Color(0xFF121212),
                        unfocusedTextColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password field
                Text(
                    text = "Password",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF121212),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF121212),
                        unfocusedBorderColor = Color(0xFFD9D9D9),
                        cursorColor = Color(0xFF121212),
                        focusedTextColor = Color(0xFF121212),
                        unfocusedTextColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                // Forgot password
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = { /* TODO: Forgot password */ },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Forgot password?",
                            fontSize = 12.sp,
                            color = Color(0xFF121212),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login button
                val loginInteractionSource = remember { MutableInteractionSource() }
                val isLoginPressed by loginInteractionSource.collectIsPressedAsState()
                val loginScale by animateFloatAsState(
                    targetValue = if (isLoginPressed) 0.95f else 1f,
                    animationSpec = tween(durationMillis = 100)
                )

                Button(
                    onClick = { /* TODO: Login logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .graphicsLayer {
                            scaleX = loginScale
                            scaleY = loginScale
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    interactionSource = loginInteractionSource
                ) {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Or Login with
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFD9D9D9)
                    )
                    Text(
                        text = "Or Login with",
                        fontSize = 12.sp,
                        color = Color(0xFF808080),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFD9D9D9)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Google button
                val googleInteractionSource = remember { MutableInteractionSource() }
                val isGooglePressed by googleInteractionSource.collectIsPressedAsState()
                val googleScale by animateFloatAsState(
                    targetValue = if (isGooglePressed) 0.95f else 1f,
                    animationSpec = tween(durationMillis = 100)
                )

                OutlinedButton(
                    onClick = { /* TODO: Google sign in */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .graphicsLayer {
                            scaleX = googleScale
                            scaleY = googleScale
                        },
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF121212)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFD9D9D9)),
                    interactionSource = googleInteractionSource
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Sign up link
                var isSignUpPressed by remember { mutableStateOf(false) }

                val signUpAlpha by animateFloatAsState(
                    targetValue = if (isSignUpPressed) 0.5f else 1f,
                    animationSpec = tween(durationMillis = 100)
                )

                val signUpScale by animateFloatAsState(
                    targetValue = if (isSignUpPressed) 0.95f else 1f,
                    animationSpec = tween(durationMillis = 100)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontSize = 14.sp,
                        color = Color(0xFF121212),
                        fontWeight = FontWeight.Normal
                    )

                    Text(
                        text = "Sign Up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF121212).copy(alpha = signUpAlpha),
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = signUpScale
                                scaleY = signUpScale
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isSignUpPressed = true
                                        val released = tryAwaitRelease()
                                        isSignUpPressed = false
                                        if (released) {
                                            navController.navigate("signup") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    }
                                )
                            }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}