// app/src/main/java/com/example/ocreaite/screens/EditClothingMetadataScreen.kt
package com.example.ocreaite.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ocreaite.data.api.ClothesApiService
import com.example.ocreaite.data.local.TokenManager
import kotlinx.coroutines.launch

data class ClothingMetadata(
    val imageUri: Uri,
    val imageBase64: String,
    var name: String = "New Item",
    var category: String = "SHIRT",
    var color: String = "Unknown",
    var brand: String = "Unknown",
    var description: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }
    val apiService = remember { ClothesApiService(tokenManager) }

    // Estado para cada roupa
    var clothingItems by remember {
        mutableStateOf(
            imageUris.mapIndexed { index, uri ->
                ClothingMetadata(
                    imageUri = uri,
                    imageBase64 = imagesBase64[index]
                )
            }
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isUploading by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }

    val categories = listOf("SHIRT", "PANTS", "SHORTS", "SHOES", "HEADWEAR", "ACCESSORIES", "OUTERWEAR")
    val currentItem = clothingItems[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF121212)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Edit Details (${currentIndex + 1}/${clothingItems.size})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF121212)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Imagem
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE5E5E5))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(currentItem.imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Item",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Nome
                Column {
                    Text(
                        text = "Name",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = currentItem.name,
                        onValueChange = {
                            clothingItems = clothingItems.toMutableList().apply {
                                this[currentIndex] = currentItem.copy(name = it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Item name", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF121212),
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            cursorColor = Color(0xFF121212)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Categoria
                Column {
                    Text(
                        text = "Category",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box {
                        OutlinedTextField(
                            value = currentItem.category,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDropdown = !showDropdown },
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color(0xFF121212)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color(0xFFD9D9D9),
                                disabledTextColor = Color(0xFF121212)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            category,
                                            color = Color(0xFF121212)
                                        )
                                    },
                                    onClick = {
                                        clothingItems = clothingItems.toMutableList().apply {
                                            this[currentIndex] = currentItem.copy(category = category)
                                        }
                                        showDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Cor
                Column {
                    Text(
                        text = "Color",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = currentItem.color,
                        onValueChange = {
                            clothingItems = clothingItems.toMutableList().apply {
                                this[currentIndex] = currentItem.copy(color = it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Color", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF121212),
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            cursorColor = Color(0xFF121212)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Marca
                Column {
                    Text(
                        text = "Brand",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = currentItem.brand,
                        onValueChange = {
                            clothingItems = clothingItems.toMutableList().apply {
                                this[currentIndex] = currentItem.copy(brand = it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Brand", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF121212),
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            cursorColor = Color(0xFF121212)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Descrição
                Column {
                    Text(
                        text = "Description (optional)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF121212),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = currentItem.description,
                        onValueChange = {
                            clothingItems = clothingItems.toMutableList().apply {
                                this[currentIndex] = currentItem.copy(description = it)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("Add details about this item", color = Color.Gray) },
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF121212),
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            cursorColor = Color(0xFF121212)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Bottom buttons
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botão Previous
                    if (currentIndex > 0) {
                        val prevInteractionSource = remember { MutableInteractionSource() }
                        val isPrevPressed by prevInteractionSource.collectIsPressedAsState()
                        val prevScale by animateFloatAsState(
                            targetValue = if (isPrevPressed) 0.95f else 1f,
                            animationSpec = tween(100)
                        )

                        Button(
                            onClick = { currentIndex-- },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .graphicsLayer {
                                    scaleX = prevScale
                                    scaleY = prevScale
                                },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE5E5E5),
                                contentColor = Color(0xFF121212)
                            ),
                            shape = RoundedCornerShape(28.dp),
                            interactionSource = prevInteractionSource
                        ) {
                            Text(
                                "Previous",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Botão Next ou Upload All
                    val nextInteractionSource = remember { MutableInteractionSource() }
                    val isNextPressed by nextInteractionSource.collectIsPressedAsState()
                    val nextScale by animateFloatAsState(
                        targetValue = if (isNextPressed) 0.95f else 1f,
                        animationSpec = tween(100)
                    )

                    Button(
                        onClick = {
                            if (currentIndex < clothingItems.size - 1) {
                                currentIndex++
                            } else {
                                // Upload todos
                                scope.launch {
                                    isUploading = true
                                    try {
                                        // TODO: Implementar upload batch avançado
                                        Toast.makeText(
                                            context,
                                            "Uploading ${clothingItems.size} items...",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Simula upload
                                        kotlinx.coroutines.delay(2000)

                                        Toast.makeText(
                                            context,
                                            "All items uploaded!",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("wardrobe") {
                                            popUpTo("add") { inclusive = true }
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } finally {
                                        isUploading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .graphicsLayer {
                                scaleX = nextScale
                                scaleY = nextScale
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF121212),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp),
                        interactionSource = nextInteractionSource,
                        enabled = !isUploading
                    ) {
                        if (isUploading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                if (currentIndex < clothingItems.size - 1) "Next" else "Upload All",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Loading overlay
        if (isUploading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Uploading...",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}