package com.example.ocreaite.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ocreaite.R
import kotlin.math.absoluteValue
import kotlin.random.Random

data class OutfitRow(
    val id: Int,
    val items: List<ClothingItem>,
    var currentIndex: Int = 0,
    var isPinned: Boolean = false
)

@Composable
fun StylingScreen(navController: NavController) {
    var numberOfRows by remember { mutableStateOf(3) }
    var visible by remember { mutableStateOf(false) }

    // Sample clothing items for each row
    val availableItems = remember {
        listOf(
            ClothingItem(1, "Gray T-Shirt", "Tops", "https://oqvestir.fbitsstatic.net/img/p/t-shirt-em-modal-e-seda-gola-redonda-cinza-184634/488101.jpg?w=1600&h=2133&v=202501231557"),
            ClothingItem(2, "Blue Shirt", "Tops", "https://cottonon.com/dw/image/v2/BBDS_PRD/on/demandware.static/-/Sites-catalog-master-men/default/dwf789fe11/3611849/3611849-13-5.jpg?sw=640&sh=960&sm=fit"),
            ClothingItem(3, "Black Shorts", "Bottoms", "https://cdn.vnda.com.br/bolovo/2024/06/20/09_08_35_847_9_6_9_935_onlinebolovo_0051_blv51.jpg?v=1718885328"),
            ClothingItem(4, "White Sneakers", "Footwear", "https://oqvestir.fbitsstatic.net/img/p/t-shirt-em-modal-e-seda-gola-redonda-cinza-184634/488101.jpg?w=1600&h=2133&v=202501231557"),
            ClothingItem(5, "Red Jacket", "Outerwear", "https://cottonon.com/dw/image/v2/BBDS_PRD/on/demandware.static/-/Sites-catalog-master-men/default/dwf789fe11/3611849/3611849-13-5.jpg?sw=640&sh=960&sm=fit"),
        )
    }

    var outfitRows by remember {
        mutableStateOf(
            (0 until 3).map { rowIndex ->
                OutfitRow(
                    id = rowIndex,
                    items = availableItems.shuffled().take(Random.nextInt(2, 5)),
                    currentIndex = 0,
                    isPinned = false
                )
            }
        )
    }

    // Update rows when numberOfRows changes
    LaunchedEffect(numberOfRows) {
        val currentSize = outfitRows.size
        if (numberOfRows > currentSize) {
            outfitRows = outfitRows + (currentSize until numberOfRows).map { rowIndex ->
                OutfitRow(
                    id = rowIndex,
                    items = availableItems.shuffled().take(Random.nextInt(2, 5)),
                    currentIndex = 0,
                    isPinned = false
                )
            }
        } else if (numberOfRows < currentSize) {
            outfitRows = outfitRows.take(numberOfRows)
        }
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                BottomNavigationBarStyling(navController = navController, currentRoute = "outfit")
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
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 48.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Styling",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF121212)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Filter button - same height as Save button
                            val filterInteractionSource = remember { MutableInteractionSource() }
                            val isFilterPressed by filterInteractionSource.collectIsPressedAsState()
                            val filterScale by animateFloatAsState(
                                targetValue = if (isFilterPressed) 0.9f else 1f,
                                animationSpec = tween(durationMillis = 100)
                            )

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .aspectRatio(1f)
                                    .graphicsLayer {
                                        scaleX = filterScale
                                        scaleY = filterScale
                                    }
                                    .background(Color(0xFF121212), RoundedCornerShape(8.dp))
                                    .clickable(
                                        interactionSource = filterInteractionSource,
                                        indication = null,
                                        onClick = { /* TODO: Open filter */ }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_filter),
                                    contentDescription = "Filter",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            val saveInteractionSource = remember { MutableInteractionSource() }
                            val isSavePressed by saveInteractionSource.collectIsPressedAsState()
                            val saveScale by animateFloatAsState(
                                targetValue = if (isSavePressed) 0.95f else 1f,
                                animationSpec = tween(durationMillis = 100)
                            )

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .graphicsLayer {
                                        scaleX = saveScale
                                        scaleY = saveScale
                                    }
                                    .background(Color(0xFF121212), RoundedCornerShape(8.dp))
                                    .clickable(
                                        interactionSource = saveInteractionSource,
                                        indication = null,
                                        onClick = { /* TODO: Save outfit */ }
                                    )
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Save",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Outfit rows with carousel - dividindo a tela útil
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        outfitRows.forEach { row ->
                            OutfitCarouselRow(
                                row = row,
                                numberOfRows = numberOfRows,
                                onSwipe = { newIndex ->
                                    outfitRows = outfitRows.map {
                                        if (it.id == row.id) it.copy(currentIndex = newIndex)
                                        else it
                                    }
                                },
                                onPinToggle = {
                                    outfitRows = outfitRows.map {
                                        if (it.id == row.id) it.copy(isPinned = !it.isPinned)
                                        else it
                                    }
                                },
                                onItemClick = { index ->
                                    outfitRows = outfitRows.map {
                                        if (it.id == row.id) it.copy(currentIndex = index)
                                        else it
                                    }
                                }
                            )
                        }
                    }

                    // Bottom controls
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .height(60.dp)
                            .background(Color(0xFF121212), RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GridButton(
                                iconRes = R.drawable.logo,
                                isSelected = numberOfRows == 2,
                                onClick = { numberOfRows = 2 }
                            )

                            GridButton(
                                iconRes = R.drawable.logo,
                                isSelected = numberOfRows == 3,
                                onClick = { numberOfRows = 3 }
                            )

                            GridButton(
                                iconRes = R.drawable.logo,
                                isSelected = numberOfRows == 4,
                                onClick = { numberOfRows = 4 }
                            )

                            DiceButton(
                                onClick = {
                                    outfitRows = outfitRows.map { row ->
                                        if (!row.isPinned && row.items.isNotEmpty()) {
                                            row.copy(currentIndex = Random.nextInt(row.items.size))
                                        } else row
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.OutfitCarouselRow(
    row: OutfitRow,
    numberOfRows: Int,
    onSwipe: (Int) -> Unit,
    onPinToggle: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Calcula o tamanho do item baseado na largura da tela e espaçamento
    val horizontalPadding = 24.dp * 2 // padding das laterais
    val spacing = 12.dp * 2 // espaçamento entre os items (left-center, center-right)
    val availableWidth = screenWidth - horizontalPadding - spacing

    // Ajusta o tamanho baseado no número de linhas
    val centerWidthPercentage = when (numberOfRows) {
        2 -> 0.55f // 2 linhas = peças maiores (55% da largura)
        3 -> 0.45f // 3 linhas = tamanho médio (45% da largura)
        4 -> 0.38f // 4 linhas = peças menores (38% da largura)
        else -> 0.45f
    }

    // Item central ocupa a porcentagem definida da largura disponível
    val centerItemSize = availableWidth * centerWidthPercentage
    // Items laterais são ~70% do tamanho do item central
    val sideItemSize = centerItemSize * 0.7f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f), // Divide igualmente o espaço entre as linhas
        contentAlignment = Alignment.Center
    ) {
        if (row.items.isEmpty()) {
            // Show Add Item centered
            Box(
                modifier = Modifier
                    .size(centerItemSize)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE5E5E5))
                    .clickable { /* TODO: Navigate to add item */ },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item",
                        tint = Color(0xFF808080),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add Item",
                        fontSize = 12.sp,
                        color = Color(0xFF808080)
                    )
                }
            }
        } else {
            // Show carousel com 3 colunas: esquerda, centro, direita
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(row.items.size) {
                        if (!row.isPinned) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    val threshold = 50f
                                    if (offsetX.absoluteValue > threshold) {
                                        val newIndex = if (offsetX > 0) {
                                            // Swipe right = go to previous item
                                            (row.currentIndex - 1 + row.items.size) % row.items.size
                                        } else {
                                            // Swipe left = go to next item
                                            (row.currentIndex + 1) % row.items.size
                                        }
                                        onSwipe(newIndex)
                                    }
                                    offsetX = 0f
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    offsetX += dragAmount
                                }
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Calculate indices for left, center, right (infinite carousel loop)
                    val centerIndex = row.currentIndex % row.items.size
                    val leftIndex = (row.currentIndex - 1 + row.items.size) % row.items.size
                    val rightIndex = (row.currentIndex + 1) % row.items.size

                    // Left item (previous item, clipped/cut off)
                    Box(
                        modifier = Modifier
                            .width(sideItemSize * 0.6f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        CarouselItem(
                            item = row.items[leftIndex],
                            size = sideItemSize,
                            isCenter = false,
                            isPinned = false,
                            onPinToggle = {},
                            onClick = { onItemClick(leftIndex) },
                            offsetX = offsetX,
                            alignment = Alignment.CenterEnd
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Center item (current item, fully visible with pin)
                    CarouselItem(
                        item = row.items[centerIndex],
                        size = centerItemSize,
                        isCenter = true,
                        isPinned = row.isPinned,
                        onPinToggle = onPinToggle,
                        onClick = {},
                        offsetX = offsetX,
                        alignment = Alignment.Center
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Right item (next item, clipped/cut off)
                    Box(
                        modifier = Modifier
                            .width(sideItemSize * 0.6f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        CarouselItem(
                            item = row.items[rightIndex],
                            size = sideItemSize,
                            isCenter = false,
                            isPinned = false,
                            onPinToggle = {},
                            onClick = { onItemClick(rightIndex) },
                            offsetX = offsetX,
                            alignment = Alignment.CenterStart
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CarouselItem(
    item: ClothingItem,
    size: Dp,
    isCenter: Boolean,
    isPinned: Boolean,
    onPinToggle: () -> Unit,
    onClick: () -> Unit,
    offsetX: Float,
    alignment: Alignment = Alignment.Center
) {
    val alpha = if (isCenter) 1f else 0.6f

    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                this.alpha = alpha
                this.translationX = offsetX * (if (isCenter) 1f else 0.3f)
            },
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE5E5E5))
                .clickable(onClick = onClick)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Pin button only on center item
        if (isCenter) {
            val pinInteractionSource = remember { MutableInteractionSource() }
            val isPinPressed by pinInteractionSource.collectIsPressedAsState()
            val pinScale by animateFloatAsState(
                targetValue = if (isPinPressed) 0.8f else 1f,
                animationSpec = tween(durationMillis = 150)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .graphicsLayer {
                        scaleX = pinScale
                        scaleY = pinScale
                    }
                    .background(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = pinInteractionSource,
                        indication = null,
                        onClick = onPinToggle
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Pin",
                    tint = if (isPinned) Color(0xFFFF6B6B) else Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun GridButton(
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Box(
        modifier = Modifier
            .size(if (isSelected) 32.dp else 28.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "Grid",
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DiceButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Box(
        modifier = Modifier
            .size(32.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Random",
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun BottomNavigationBarStyling(navController: NavController, currentRoute: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(Color.White)
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            NavBarItemStyling(
                icon = R.drawable.icon_wardrobe,
                contentDescription = "Wardrobe",
                route = "wardrobe",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )

            NavBarItemStyling(
                icon = R.drawable.icon_calendar,
                contentDescription = "Calendar",
                route = "calendar",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )

            NavBarItemStyling(
                icon = R.drawable.logo,
                contentDescription = "AI",
                route = "ai",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )

            NavBarItemStyling(
                icon = R.drawable.icon_outfit,
                contentDescription = "Outfit",
                route = "outfit",
                currentRoute = currentRoute,
                navController = navController,
                isAddButton = false
            )

            NavBarItemStyling(
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
private fun RowScope.NavBarItemStyling(
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