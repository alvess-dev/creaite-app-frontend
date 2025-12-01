package com.example.ocreaite.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegisterStep3Screen(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var showPasswordTips by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Pegar savedStateHandle da Step1
    val step1Entry = navController.getBackStackEntry("register/step1")
    val savedStateHandle = step1Entry.savedStateHandle

    val passwordStrength = remember(password) {
        when {
            password.isEmpty() -> 0
            password.length < 8 -> 1
            password.length < 12 -> 1
            password.length >= 12 && password.any { it.isDigit() } &&
                    password.any { !it.isLetterOrDigit() } -> 3
            password.length >= 12 -> 2
            else -> 1
        }
    }

    val strengthColor = when (passwordStrength) {
        1 -> Color(0xFFFF6B6B)
        2 -> Color(0xFFFFD93D)
        3 -> Color(0xFF6BCF7F)
        else -> Color(0xFFD9D9D9)
    }

    val strengthProgress = when (passwordStrength) {
        1 -> 0.33f
        2 -> 0.66f
        3 -> 1f
        else -> 0f
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (showPasswordTips) 10.dp else 0.dp)
        ) {
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
                        targetValue = 0.6f,
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
                            text = "Create password",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF121212)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "Password",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF121212)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "your password",
                                    color = Color.Gray
                                )
                            },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        val animatedProgress by animateFloatAsState(
                            targetValue = strengthProgress,
                            animationSpec = tween(durationMillis = 400)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .background(Color(0xFFD9D9D9), RoundedCornerShape(3.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animatedProgress)
                                    .fillMaxHeight()
                                    .background(strengthColor, RoundedCornerShape(3.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF121212),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                ) {
                                    append("Make it more complicated")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF121212),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                ) {
                                    append("\nUse 12 or more letters, numbers and symbols")
                                }
                            },
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { showPasswordTips = !showPasswordTips },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "Password tips ⓘ",
                                    fontSize = 12.sp,
                                    color = Color(0xFF121212),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        val buttonInteractionSource = remember { MutableInteractionSource() }
                        val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                        val buttonScale by animateFloatAsState(
                            targetValue = if (isButtonPressed) 0.95f else 1f,
                            animationSpec = tween(durationMillis = 100)
                        )

                        Button(
                            onClick = {
                                if (password.length >= 8) {
                                    // Salvar no savedStateHandle da Step1
                                    savedStateHandle.set("password", password)
                                    Log.d("RegisterStep3", "✅ Password saved (length: ${password.length})")

                                    visible = false
                                    navController.navigate("register/step4")
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Password must be at least 8 characters",
                                        Toast.LENGTH_SHORT
                                    ).show()
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

        if (showPasswordTips) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            showPasswordTips = false
                        }
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { },
                    color = Color(0xFF121212),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Password tips",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "A strong password helps keep your account safe.\nUse at least 12 letters, numbers and symbols.",
                            fontSize = 14.sp,
                            color = Color.White,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "What to avoid:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "• Common passwords, words and names",
                                fontSize = 14.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "• Recent dates or dates associated with you",
                                fontSize = 14.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "• Simple patterns and repeated text",
                                fontSize = 14.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { showPasswordTips = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF121212)
                            )
                        ) {
                            Text(
                                text = "Okay",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}