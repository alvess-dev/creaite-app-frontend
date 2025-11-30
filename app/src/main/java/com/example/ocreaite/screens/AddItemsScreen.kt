package com.example.ocreaite.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun AddItemsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Camera roll, 1 = Database
    var selectedImages by remember { mutableStateOf(setOf<Uri>()) }
    var visible by remember { mutableStateOf(false) }
    var selectedAlbum by remember { mutableStateOf("All Photos") }
    var galleryImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var availableAlbums by remember { mutableStateOf<List<String>>(emptyList()) }
    var showAlbumDropdown by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Verificar permissão
    LaunchedEffect(Unit) {
        visible = true
        hasPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Launcher para solicitar permissão
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            // Carregar imagens
            loadGalleryImages(context) { images ->
                galleryImages = images
            }
        }
    }

    // URI temporária para a câmera
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para abrir câmera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            // Foto tirada com sucesso, adicionar às selecionadas
            selectedImages = selectedImages + tempPhotoUri!!
        }
    }

    // Launcher para solicitar permissão de câmera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val photoUri = createImageUri(context)
            tempPhotoUri = photoUri
            photoUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        }
    }

    // Launcher para selecionar múltiplas imagens
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImages = uris.toSet()
        }
    }

    // Carregar imagens da galeria quando tiver permissão
    LaunchedEffect(hasPermission, selectedAlbum) {
        if (hasPermission) {
            // Carregar álbuns disponíveis
            loadAvailableAlbums(context) { albums ->
                availableAlbums = albums
            }

            // Carregar imagens do álbum selecionado
            if (selectedAlbum == "All Photos") {
                loadGalleryImages(context) { images ->
                    galleryImages = images
                }
            } else {
                loadAlbumImages(context, selectedAlbum) { images ->
                    galleryImages = images
                }
            }
        }
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
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = {
                                        visible = false
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier.align(Alignment.CenterStart)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Voltar",
                                        tint = Color(0xFF121212)
                                    )
                                }

                                Text(
                                    text = "Add items",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF121212),
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        // Tabs
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Camera roll tab
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                    ) { selectedTab = 0 },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // TODO: Substituir por ícone de câmera (PhotoCamera)
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Camera roll",
                                    tint = if (selectedTab == 0) Color(0xFF121212) else Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Camera roll",
                                    fontSize = 12.sp,
                                    color = if (selectedTab == 0) Color(0xFF121212) else Color.Gray,
                                    fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (selectedTab == 0) {
                                    Box(
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(2.dp)
                                            .background(Color(0xFF121212))
                                    )
                                }
                            }

                            // Divider vertical
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color(0xFFD9D9D9))
                            )

                            // Database tab
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                    ) { selectedTab = 1 },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // TODO: Substituir por ícone de imagem/database (Image ou Collections)
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Database",
                                    tint = if (selectedTab == 1) Color(0xFF121212) else Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Database",
                                    fontSize = 12.sp,
                                    color = if (selectedTab == 1) Color(0xFF121212) else Color.Gray,
                                    fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (selectedTab == 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(2.dp)
                                            .background(Color(0xFF121212))
                                    )
                                }
                            }
                        }

                        // Album selector (apenas no Camera roll)
                        if (selectedTab == 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8E8E8))
                                    .clickable { showAlbumDropdown = !showAlbumDropdown }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$selectedAlbum ▼",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF121212)
                                )
                            }
                        }
                    }
                }

                // Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (selectedTab) {
                        0 -> CameraRollContent(
                            galleryImages = galleryImages,
                            selectedImages = selectedImages,
                            hasPermission = hasPermission,
                            onCameraClick = {
                                // Verificar permissão de câmera
                                val hasCameraPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasCameraPermission) {
                                    val photoUri = createImageUri(context)
                                    tempPhotoUri = photoUri
                                    photoUri?.let { uri ->
                                        cameraLauncher.launch(uri)
                                    }
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            onRequestPermission = {
                                val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    Manifest.permission.READ_MEDIA_IMAGES
                                } else {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                                permissionLauncher.launch(permission)
                            },
                            onImageSelected = { uri ->
                                selectedImages = if (selectedImages.contains(uri)) {
                                    selectedImages - uri
                                } else {
                                    selectedImages + uri
                                }
                            }
                        )
                        1 -> DatabaseContent()
                    }
                }

                // Bottom button
                if (selectedImages.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Button(
                            onClick = { /* Upload images */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            shape = RoundedCornerShape(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF121212),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Upload (${selectedImages.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Dropdown overlay (aparece por cima de tudo)
        if (showAlbumDropdown && selectedTab == 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    ) {
                        showAlbumDropdown = false
                    }
            )

            // Calcular posição do dropdown (abaixo do botão Album)
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Espaço para header + tabs + album button
                Spacer(modifier = Modifier.height(180.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable(
                            indication = null,
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                        ) { /* Previne cliques de passarem */ },
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text(
                                text = "All Photos",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedAlbum = "All Photos"
                                        showAlbumDropdown = false
                                    }
                                    .padding(20.dp),
                                fontSize = 16.sp,
                                color = if (selectedAlbum == "All Photos") Color(0xFF121212) else Color.Gray,
                                fontWeight = if (selectedAlbum == "All Photos") FontWeight.Bold else FontWeight.Normal
                            )
                            Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                        }

                        items(availableAlbums.size) { index ->
                            val album = availableAlbums[index]
                            Column {
                                Text(
                                    text = album,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedAlbum = album
                                            showAlbumDropdown = false
                                        }
                                        .padding(20.dp),
                                    fontSize = 16.sp,
                                    color = if (selectedAlbum == album) Color(0xFF121212) else Color.Gray,
                                    fontWeight = if (selectedAlbum == album) FontWeight.Bold else FontWeight.Normal
                                )
                                if (index < availableAlbums.size - 1) {
                                    Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
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
fun CameraRollContent(
    galleryImages: List<Uri>,
    selectedImages: Set<Uri>,
    hasPermission: Boolean,
    onCameraClick: () -> Unit,
    onRequestPermission: () -> Unit,
    onImageSelected: (Uri) -> Unit
) {
    if (!hasPermission) {
        // Mostrar botão para solicitar permissão
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Permission needed",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF121212)
                )
                Text(
                    text = "Allow access to your photos",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF121212)
                    )
                ) {
                    Text("Grant Permission")
                }
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Primeiro item - Botão da câmera
            item {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
                        .clickable { onCameraClick() },
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Substituir por ícone de câmera (PhotoCamera ou AddAPhoto)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Open camera",
                        tint = Color(0xFF121212),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Imagens da galeria
            items(galleryImages.size) { index ->
                val imageUri = galleryImages[index]
                val isSelected = selectedImages.contains(imageUri)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable { onImageSelected(imageUri) }
                ) {
                    // Imagem real da galeria
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Gallery image",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE8E8E8), RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay quando selecionado
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF121212).copy(alpha = 0.3f))
                        )
                    }

                    // Checkmark
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(24.dp)
                                .background(Color(0xFF121212), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DatabaseContent() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Placeholders para itens do database
        items(12) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFE8E8E8), RoundedCornerShape(8.dp))
                    .clickable { /* Selecionar item */ }
            ) {
                // Placeholder para item do database
                Text(
                    text = "Item ${index + 1}",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Funções auxiliares
private fun loadAvailableAlbums(context: android.content.Context, onAlbumsLoaded: (List<String>) -> Unit) {
    val albums = mutableSetOf<String>()

    val projection = arrayOf(
        android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    context.contentResolver.query(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        null
    )?.use { cursor ->
        val bucketColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val bucketName = cursor.getString(bucketColumn)
            if (bucketName != null) {
                albums.add(bucketName)
            }
        }
    }

    onAlbumsLoaded(albums.toList().sorted())
}

private fun loadAlbumImages(context: android.content.Context, albumName: String, onImagesLoaded: (List<Uri>) -> Unit) {
    val images = mutableListOf<Uri>()

    val projection = arrayOf(
        android.provider.MediaStore.Images.Media._ID,
        android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        android.provider.MediaStore.Images.Media.DATE_ADDED
    )

    val selection = "${android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(albumName)
    val sortOrder = "${android.provider.MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = Uri.withAppendedPath(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id.toString()
            )
            images.add(contentUri)
        }
    }

    onImagesLoaded(images)
}

private fun loadGalleryImages(context: android.content.Context, onImagesLoaded: (List<Uri>) -> Unit) {
    val images = mutableListOf<Uri>()

    val projection = arrayOf(
        android.provider.MediaStore.Images.Media._ID,
        android.provider.MediaStore.Images.Media.DATE_ADDED
    )

    val sortOrder = "${android.provider.MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = Uri.withAppendedPath(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id.toString()
            )
            images.add(contentUri)
        }
    }

    onImagesLoaded(images)
}

private fun createImageUri(context: android.content.Context): Uri? {
    val contentResolver = context.contentResolver
    val contentValues = android.content.ContentValues().apply {
        put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
        put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}