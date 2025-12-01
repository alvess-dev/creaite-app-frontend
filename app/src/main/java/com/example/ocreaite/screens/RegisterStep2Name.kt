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

@Composable
fun RegisterStep2Screen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Pegar savedStateHandle da Step1
    val step1Entry = navController.getBackStackEntry("register/step1")
    val savedStateHandle = step1Entry.savedStateHandle

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
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
                    targetValue = 0.4f,
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
                        text = "Create account",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF121212)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Name",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "your name",
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
                            if (name.isNotBlank()) {
                                // Salvar no savedStateHandle da Step1
                                savedStateHandle.set("name", name)
                                Log.d("RegisterStep2", "✅ Name saved: '$name'")

                                visible = false
                                navController.navigate("register/step3")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please enter your name",
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
}