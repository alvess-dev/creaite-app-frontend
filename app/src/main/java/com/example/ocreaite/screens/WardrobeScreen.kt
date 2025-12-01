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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ocreaite.R

data class ClothingItem(
    val id: Int,
    val name: String,
    val category: String,
    val imageUrl: String? = null,
    var isFavorite: Boolean = false
)

@Composable
fun WardrobeScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchText by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    var clothingItems by remember {
        mutableStateOf(
            listOf(
                ClothingItem(
                    1,
                    "Gray Modal T-Shirt",
                    "Tops",
                    "https://oqvestir.fbitsstatic.net/img/p/t-shirt-em-modal-e-seda-gola-redonda-cinza-184634/488101.jpg?w=1600&h=2133&v=202501231557",
                    false
                ),
                ClothingItem(
                    2,
                    "Blue Casual Shirt",
                    "Tops",
                    "https://cottonon.com/dw/image/v2/BBDS_PRD/on/demandware.static/-/Sites-catalog-master-men/default/dwf789fe11/3611849/3611849-13-5.jpg?sw=640&sh=960&sm=fit",
                    false
                ),
                ClothingItem(
                    3,
                    "Black Shorts",
                    "Bottoms",
                    "https://cdn.vnda.com.br/bolovo/2024/06/20/09_08_35_847_9_6_9_935_onlinebolovo_0051_blv51.jpg?v=1718885328",
                    true
                ),
                ClothingItem(
                    4,
                    "White Sneakers",
                    "Footwear",
                    "https://oqvestir.fbitsstatic.net/img/p/t-shirt-em-modal-e-seda-gola-redonda-cinza-184634/488101.jpg?w=1600&h=2133&v=202501231557",
                    false
                ),
                ClothingItem(
                    5,
                    "Red Jacket",
                    "Outerwear",
                    "https://cottonon.com/dw/image/v2/BBDS_PRD/on/demandware.static/-/Sites-catalog-master-men/default/dwf789fe11/3611849/3611849-13-5.jpg?sw=640&sh=960&sm=fit",
                    false
                ),
                ClothingItem(
                    6,
                    "Black Jeans",
                    "Bottoms",
                    "https://cdn.vnda.com.br/bolovo/2024/06/20/09_08_35_847_9_6_9_935_onlinebolovo_0051_blv51.jpg?v=1718885328",
                    false
                ),
            )
        )
    }

    val availableCategories = remember(clothingItems) {
        clothingItems.map { it.category }.distinct()
    }

    val filteredItems = remember(selectedCategory, searchText, clothingItems, showOnlyFavorites) {
        clothingItems.filter { item ->
            val matchesCategory = selectedCategory == null || item.category == selectedCategory
            val matchesSearch = searchText.isEmpty() ||
                    item.name.contains(searchText, ignoreCase = true)
            val matchesFavorite = !showOnlyFavorites || item.isFavorite
            matchesCategory && matchesSearch && matchesFavorite
        }
    }

    LaunchedEffect(Unit) {
        visible = true
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CategoryCircle(
                                text = "All",
                                imageUrl = clothingItems.firstOrNull()?.imageUrl,
                                isSelected = selectedCategory == null,
                                onClick = { selectedCategory = null }
                            )

                            availableCategories.forEach { category ->
                                val categoryItem = clothingItems.firstOrNull { it.category == category }
                                CategoryCircle(
                                    text = category,
                                    imageUrl = categoryItem?.imageUrl,
                                    isSelected = selectedCategory == category,
                                    onClick = { selectedCategory = category }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

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

                        IconButton(
                            onClick = { showOnlyFavorites = !showOnlyFavorites },
                            modifier = Modifier
                                .size(52.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (showOnlyFavorites) Color(0xFF121212) else Color(0xFFD9D9D9),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = if (showOnlyFavorites) Color(0xFF121212) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                imageVector = if (showOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorites",
                                tint = if (showOnlyFavorites) Color.White else Color(0xFF121212)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (filteredItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (showOnlyFavorites) "No favorites yet" else "Start styling",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF000000)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            val addInteractionSource = remember { MutableInteractionSource() }
                            val isAddPressed by addInteractionSource.collectIsPressedAsState()
                            val addScale by animateFloatAsState(
                                targetValue = if (isAddPressed) 0.9f else 1f,
                                animationSpec = tween(durationMillis = 100)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = addInteractionSource,
                                        indication = null,
                                        onClick = { /* TODO: Navigate to add item */ }
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .graphicsLayer {
                                            scaleX = addScale
                                            scaleY = addScale
                                        }
                                        .background(
                                            color = Color(0xFFD9D9D9),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add items",
                                        tint = Color(0xFF000000),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Add items",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF000000)
                                )
                            }
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
                                onFavoriteClick = {
                                    clothingItems = clothingItems.map {
                                        if (it.id == item.id) it.copy(isFavorite = !it.isFavorite)
                                        else it
                                    }
                                },
                                onClick = { /* TODO: Navigate to item detail */ }
                            )
                        }
                    }
                }
            }
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
                AsyncImage(
                    model = imageUrl,
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
fun ClothingItemCard(
    item: ClothingItem,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    var isFavoritePressedLocal by remember { mutableStateOf(false) }

    val cardInteractionSource = remember { MutableInteractionSource() }
    val isCardPressed by cardInteractionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isCardPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    val favoriteInteractionSource = remember { MutableInteractionSource() }
    val isFavoritePressed by favoriteInteractionSource.collectIsPressedAsState()
    val favoriteScale by animateFloatAsState(
        targetValue = if (isFavoritePressed) 0.8f else 1f,
        animationSpec = tween(durationMillis = 150)
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
                )
        ) {
            if (item.imageUrl != null) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(item.name, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp)
                .graphicsLayer {
                    scaleX = favoriteScale
                    scaleY = favoriteScale
                }
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = favoriteInteractionSource,
                    indication = null
                ) {
                    isFavoritePressedLocal = true
                    onFavoriteClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (item.isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (isFavoritePressedLocal) Color.Black else (if (item.isFavorite) Color.Red else Color.Black),
                modifier = Modifier.size(18.dp)
            )
        }
    }

    LaunchedEffect(isFavoritePressedLocal) {
        if (isFavoritePressedLocal) {
            kotlinx.coroutines.delay(150)
            isFavoritePressedLocal = false
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