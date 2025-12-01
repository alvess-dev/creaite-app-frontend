package com.example.ocreaite.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ocreaite.viewmodels.AuthViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun RegisterStep5Screen(navController: NavController) {
    var birthDate by remember { mutableStateOf<LocalDate?>(null) }
    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }
    val authState by viewModel.authState.collectAsState()

    // Recuperar dados das etapas anteriores
    val email = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("email") ?: ""
    val name = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("name") ?: ""
    val password = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("password") ?: ""
    val username = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("username") ?: ""

    // Date Picker Dialog
    val calendar = Calendar.getInstance()
    calendar.set(1995, 6, 14) // Data padrão: Jul 14, 1995

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                birthDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Define data máxima como hoje
            datePicker.maxDate = System.currentTimeMillis()
            // Define data mínima como 1900
            val minCalendar = Calendar.getInstance()
            minCalendar.set(1900, 0, 1)
            datePicker.minDate = minCalendar.timeInMillis
        }
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    // Observer para estado de autenticação
    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                val userName = (authState as AuthViewModel.AuthState.Success).userName
                viewModel.resetState()
                // Navega para a tela de interesses após cadastro
                navController.navigate("interests") {
                    popUpTo("splash1") { inclusive = true }
                }
            }

            is AuthViewModel.AuthState.Error -> {
                val message = (authState as AuthViewModel.AuthState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
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
        if (authState is AuthViewModel.AuthState.Loading) {
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
                    targetValue = 1f,
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
                        text = "Hi, ${name.ifBlank { "user" }}! Enter your birthdate",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF121212),
                        lineHeight = 38.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "See why ⓘ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF121212)
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Spacer(modifier = Modifier.height(60.dp))

                    // Date picker visual
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                datePickerDialog.show()
                            }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = birthDate?.format(
                                        DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
                                    ) ?: "Select date",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (birthDate != null) Color(0xFF121212) else Color.Gray
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "▲",
                                        fontSize = 20.sp,
                                        color = Color(0xFF121212)
                                    )

                                    Spacer(modifier = Modifier.height(0.dp))

                                    Text(
                                        text = "▼",
                                        fontSize = 20.sp,
                                        color = Color(0xFF121212)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Use your own age",
                                fontSize = 12.sp,
                                color = Color(0xFF121212)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botão Next e Skip for now
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isButtonPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Button(
                        onClick = {
                            if (birthDate != null) {
                                // Registrar usuário com data de nascimento
                                viewModel.register(
                                    email = email,
                                    password = password,
                                    username = username,
                                    name = name,
                                    birthDate = birthDate.toString()
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please select your birth date",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .graphicsLayer {
                                scaleX = buttonScale
                                scaleY = buttonScale
                            },
                        shape = RoundedCornerShape(28.dp),
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

                    Spacer(modifier = Modifier.height(12.dp))

                    var isSkipPressed by remember { mutableStateOf(false) }

                    val skipAlpha by animateFloatAsState(
                        targetValue = if (isSkipPressed) 0.5f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    val skipScale by animateFloatAsState(
                        targetValue = if (isSkipPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Text(
                        text = "Skip for now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212).copy(alpha = skipAlpha),
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = skipScale
                                scaleY = skipScale
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isSkipPressed = true
                                        val released = tryAwaitRelease()
                                        isSkipPressed = false
                                        if (released) {
                                            // Registrar sem data de nascimento
                                            viewModel.register(
                                                email = email,
                                                password = password,
                                                username = username,
                                                name = name,
                                                birthDate = null
                                            )
                                        }
                                    }
                                )
                            }
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}