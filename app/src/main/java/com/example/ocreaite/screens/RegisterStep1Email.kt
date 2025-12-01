package com.example.ocreaite.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ocreaite.viewmodels.AuthViewModel

@Composable
fun RegisterStep1Screen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }
    val emailValidationState by viewModel.emailValidationState.collectAsState()

    LaunchedEffect(Unit) {
        visible = true
    }

    // Observar mudanças no estado de validação de email
    LaunchedEffect(emailValidationState) {
        when (emailValidationState) {
            is AuthViewModel.EmailValidationState.Valid -> {
                // Email válido, pode prosseguir
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("email", email)
                viewModel.resetEmailValidation()
                visible = false
                navController.navigate("register/step2")
            }
            is AuthViewModel.EmailValidationState.Invalid -> {
                val message = (emailValidationState as AuthViewModel.EmailValidationState.Invalid).message
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.resetEmailValidation()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // Loading indicator
        if (emailValidationState is AuthViewModel.EmailValidationState.Loading) {
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
                // Barra de progresso no topo (tela inteira)
                val animatedTopProgress by animateFloatAsState(
                    targetValue = 0.2f,
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

                // Header com seta
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

                // Conteúdo
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
                        text = "Email address",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "your email",
                                color = Color.Gray
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Checkbox customizado e texto
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Checkbox customizado
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(
                                    color = if (termsAccepted) Color(0xFF121212) else Color(0xFFD9D9D9),
                                    shape = CircleShape
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    termsAccepted = !termsAccepted
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = termsAccepted,
                                enter = scaleIn(tween(150)) + fadeIn(tween(150)),
                                exit = scaleOut(tween(150)) + fadeOut(tween(150))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Checked",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Texto com "terms" e "privacy policy" em negrito e sublinhado
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF121212),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                ) {
                                    append("I accept the ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF121212),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append("terms")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF121212),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                ) {
                                    append(" and ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF121212),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append("privacy policy")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botão Next
                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isButtonPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Button(
                        onClick = {
                            if (email.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Please enter your email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (!termsAccepted) {
                                Toast.makeText(
                                    context,
                                    "Please accept the terms and privacy policy",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Validar email no backend
                                viewModel.validateEmail(email)
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