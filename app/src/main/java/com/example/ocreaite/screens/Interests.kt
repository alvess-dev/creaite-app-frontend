package com.example.ocreaite.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Interest(
    val id: Int,
    val name: String,
    val emoji: String
)

@Composable
fun Interests(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var selectedInterests by remember { mutableStateOf(setOf<Int>()) }
    var visible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val interests = remember {
        listOf(
            Interest(1, "Streetwear", "👕"),
            Interest(2, "Sport life", "⚽"),
            Interest(3, "Gothic", "🦇"),
            Interest(4, "Minimalist", "⚪"),
            Interest(5, "Casual", "👔"),
            Interest(6, "Formal", "🎩"),
            Interest(7, "Vintage", "📻"),
            Interest(8, "Y2K", "💿"),
            Interest(9, "Boho", "🌼"),
            Interest(10, "Punk", "🎸"),
            Interest(11, "Preppy", "📚"),
            Interest(12, "Athleisure", "🏃")
        )
    }

    val filteredInterests = remember(searchText, interests) {
        if (searchText.isEmpty()) {
            interests
        } else {
            interests.filter { it.name.contains(searchText, ignoreCase = true) }
        }
    }

    LaunchedEffect(Unit) {
        visible = true
        kotlinx.coroutines.delay(200)
        buttonVisible = true
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
                // Header com seta (opcional - pode remover se não quiser voltar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Removido botão de voltar para primeira vez
                    Spacer(modifier = Modifier.height(48.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Conteúdo com scroll
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "What are you interested in?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF121212),
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Select all that describes your interests or style",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Search bar
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = "Search",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Grid de interesses
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        filteredInterests.chunked(3).forEach { rowInterests ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                rowInterests.forEach { interest ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        InterestCard(
                                            interest = interest,
                                            isSelected = selectedInterests.contains(interest.id),
                                            onClick = {
                                                selectedInterests = if (selectedInterests.contains(interest.id)) {
                                                    selectedInterests - interest.id
                                                } else {
                                                    selectedInterests + interest.id
                                                }
                                            }
                                        )
                                    }
                                }
                                // Preencher espaços vazios
                                repeat(3 - rowInterests.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão Continue
                    AnimatedVisibility(
                        visible = buttonVisible,
                        enter = slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(500)
                        ) + fadeIn(animationSpec = tween(500))
                    ) {
                        val buttonInteractionSource = remember { MutableInteractionSource() }
                        val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                        val buttonScale by animateFloatAsState(
                            targetValue = if (isButtonPressed) 0.95f else 1f,
                            animationSpec = tween(durationMillis = 100),
                            label = "buttonScale"
                        )

                        Button(
                            onClick = {
                                if (selectedInterests.isNotEmpty()) {
                                    // Navegar para guarda-roupa
                                    navController.navigate("wardrobe") {
                                        popUpTo("interests") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please select at least one interest",
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
                                containerColor = if (selectedInterests.isNotEmpty())
                                    Color(0xFF121212)
                                else
                                    Color(0xFFD9D9D9),
                                contentColor = if (selectedInterests.isNotEmpty())
                                    Color.White
                                else
                                    Color.Gray
                            ),
                            enabled = selectedInterests.isNotEmpty(),
                            interactionSource = buttonInteractionSource
                        ) {
                            Text(
                                text = "Continue",
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

@Composable
fun InterestCard(
    interest: Interest,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "cardScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    color = if (isSelected) Color(0xFF121212) else Color(0xFFE5E5E5),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) Color(0xFF121212) else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = interest.emoji,
                fontSize = 36.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = interest.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF121212),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}