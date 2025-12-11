// app/src/main/java/com/example/ocreaite/screens/WardrobeScreen.kt
package com.example.ocreaite.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
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
    var favorites by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.loadUserClothes()
    }

    LaunchedEffect(selectedCategory) {
        viewModel.loadUserClothes(selectedCategory)
    }

    val clothingItems = when (clothesState) {
        is WardrobeViewModel.ClothesState.Success -> {
            (clothesState as WardrobeViewModel.ClothesState.Success).items
        }
        else -> emptyList()
    }

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
                    // Header
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
                                // Estado vazio melhorado
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Start styling",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF121212),
                                            modifier = Modifier.padding(bottom = 24.dp)
                                        )

                                        val addInteractionSource = remember { MutableInteractionSource() }
                                        val isAddPressed by addInteractionSource.collectIsPressedAsState()
                                        val addScale by animateFloatAsState(
                                            targetValue = if (isAddPressed) 0.9f else 1f,
                                            animationSpec = tween(100)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .graphicsLayer {
                                                    scaleX = addScale
                                                    scaleY = addScale
                                                }
                                                .background(Color(0xFF121212), CircleShape)
                                                .clickable(
                                                    interactionSource = addInteractionSource,
                                                    indication = null,
                                                    onClick = { navController.navigate("add") }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add items",
                                                tint = Color.White,
                                                modifier = Modifier.size(40.dp)
                                            )
                                        }

                                        Text(
                                            text = "Add items",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF121212),
                                            modifier = Modifier.padding(top = 16.dp)
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
                                            isFavorite = favorites.contains(item.id),
                                            onFavoriteClick = {
                                                favorites = if (favorites.contains(item.id)) {
                                                    favorites - item.id
                                                } else {
                                                    favorites + item.id
                                                }
                                                // TODO: Enviar para o banco
                                            },
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
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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

        // Coração favorito (outline/preenchido)
        val favoriteInteractionSource = remember { MutableInteractionSource() }
        val isFavoritePressed by favoriteInteractionSource.collectIsPressedAsState()
        val favoriteScale by animateFloatAsState(
            targetValue = if (isFavoritePressed) 0.85f else 1f,
            animationSpec = tween(100)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp)
                .graphicsLayer {
                    scaleX = favoriteScale
                    scaleY = favoriteScale
                }
                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                .clickable(
                    interactionSource = favoriteInteractionSource,
                    indication = null,
                    onClick = onFavoriteClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color(0xFFFF6B6B) else Color(0xFF121212),
                modifier = Modifier.size(18.dp)
            )
        }
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