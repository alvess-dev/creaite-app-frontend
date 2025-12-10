// app/src/main/java/com/example/ocreaite/screens/WardrobeScreen.kt
package com.example.ocreaite.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ocreaite.R
import com.example.ocreaite.components.SmartImage
import com.example.ocreaite.data.models.ClothingItem
import com.example.ocreaite.data.models.ProcessingStatus
import com.example.ocreaite.viewmodels.WardrobeViewModel

@Composable
fun WardrobeScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = remember { WardrobeViewModel(context) }
    val clothesState by viewModel.clothesState.collectAsState()

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchText by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    // ✅ Carrega as roupas mockadas quando a tela aparece
    LaunchedEffect(Unit) {
        visible = true
        viewModel.loadUserClothes()
    }

    // ✅ Recarrega quando categoria muda
    LaunchedEffect(selectedCategory) {
        viewModel.loadUserClothes(selectedCategory)
    }

    val clothingItems = when (clothesState) {
        is WardrobeViewModel.ClothesState.Success -> {
            (clothesState as WardrobeViewModel.ClothesState.Success).items
        }
        else -> emptyList()
    }

    // ✅ Pega todas as categorias disponíveis (das roupas mockadas)
    val availableCategories = remember(clothingItems) {
        clothingItems.mapNotNull { it.category }.distinct().sorted()
    }

    // ✅ Filtra por busca
    val filteredItems = remember(searchText, clothingItems) {
        clothingItems.filter { item ->
            val matchesSearch = searchText.isEmpty() ||
                    (item.name?.contains(searchText, ignoreCase = true) == true) ||
                    (item.category?.contains(searchText, ignoreCase = true) == true)
            matchesSearch
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = "wardrobe")
        },
        containerColor = Color.White
    ) { paddingValues ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 48.dp)
                    ) {
                        Text(
                            text = "Wardrobe",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF121212)
                        )

                        if (clothingItems.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            // ✅ Filtro de categorias funcionando
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Botão "All"
                                CategoryCircle(
                                    text = "All",
                                    imageUrl = clothingItems.firstOrNull()?.clothingPictureUrl,
                                    isSelected = selectedCategory == null,
                                    onClick = { selectedCategory = null }
                                )

                                // Categorias disponíveis
                                availableCategories.forEach { category ->
                                    val categoryItem = clothingItems.firstOrNull { it.category == category }
                                    CategoryCircle(
                                        text = category,
                                        imageUrl = categoryItem?.clothingPictureUrl,
                                        isSelected = selectedCategory == category,
                                        onClick = { selectedCategory = category }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Search Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                placeholder = {
                                    Text(
                                        text = "Search",
                                        color = Color.Gray,
                                        fontSize = 15.sp
                                    )
                                },
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 15.sp
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(22.dp)
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

                            IconButton(
                                onClick = { /* TODO: Open filter */ },
                                modifier = Modifier
                                    .size(52.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFD9D9D9),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_filter),
                                    contentDescription = "Filter",
                                    tint = Color(0xFF121212),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Content
                    when (clothesState) {
                        is WardrobeViewModel.ClothesState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFF121212))
                            }
                        }

                        is WardrobeViewModel.ClothesState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (clothesState as WardrobeViewModel.ClothesState.Error).message,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(32.dp)
                                )
                            }
                        }

                        else -> {
                            if (filteredItems.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (searchText.isEmpty()) "No clothes found" else "No results",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF121212)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = if (searchText.isEmpty()) "Add some clothes to get started" else "Try a different search",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 24.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(bottom = 16.dp)
                                ) {
                                    items(filteredItems) { item ->
                                        ClothingItemCard(
                                            item = item,
                                            onDelete = { viewModel.deleteClothing(item.id) },
                                            onClick = { /* TODO: Navigate to item detail */ }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClothingItemCard(
    item: ClothingItem,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val cardInteractionSource = remember { MutableInteractionSource() }
    val isCardPressed by cardInteractionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isCardPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE5E5E5))
                .clickable(
                    interactionSource = cardInteractionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            when (item.processingStatus) {
                ProcessingStatus.PENDING, ProcessingStatus.PROCESSING -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF121212),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Processing...",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }

                ProcessingStatus.FAILED -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Processing failed",
                            fontSize = 12.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                ProcessingStatus.COMPLETED -> {
                    SmartImage(
                        imageUrl = item.clothingPictureUrl,
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Badge com nome da categoria
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
                .background(
                    color = Color(0xFF121212).copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = item.category ?: "Unknown",
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CategoryCircle(
    text: String,
    imageUrl: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clip(CircleShape)
                .background(Color(0xFFE5E5E5))
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) Color(0xFF121212) else Color.Transparent,
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                SmartImage(
                    imageUrl = imageUrl,
                    contentDescription = text,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = text,
                    tint = Color(0xFF808080),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = Color(0xFF121212),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                clip = true,
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            )
            .background(Color.White)
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            NavBarItem(
                icon = R.drawable.icon_wardrobe,
                contentDescription = "Wardrobe",
                route = "wardrobe",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )
            NavBarItem(
                icon = R.drawable.icon_calendar,
                contentDescription = "Calendar",
                route = "calendar",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )

            NavBarItem(
                icon = R.drawable.logo,
                contentDescription = "AI",
                route = "ai",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )

            NavBarItem(
                icon = R.drawable.icon_outfit,
                contentDescription = "Outfit",
                route = "outfit",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )

            NavBarItem(
                icon = null,
                contentDescription = "Add",
                route = "add",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = true
            )
        }
    }
}

@Composable
private fun RowScope.NavBarItem(
    icon: Int?,
    contentDescription: String,
    route: String,
    currentRoute: String,
    navController: NavController,
    isAddButton: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val iconScale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 100)
    )
    NavigationBarItem(
        icon = {
            if (isAddButton) {
                Box(
                    modifier = Modifier
                        .size(if (currentRoute == route) 28.dp else 24.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF121212),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = iconScale
                                scaleY = iconScale
                            }
                            .size(18.dp),
                        tint = Color(0xFF121212)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = icon!!),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                        }
                        .size(if (currentRoute == route) 28.dp else 24.dp),
                    tint = Color(0xFF121212)
                )
            }
        },
        selected = currentRoute == route,
        onClick = { navController.navigate(route) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF121212),
            unselectedIconColor = Color(0xFF121212),
            indicatorColor = Color.Transparent
        ),
        interactionSource = interactionSource
    )
}