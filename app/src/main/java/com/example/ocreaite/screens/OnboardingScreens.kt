package com.example.ocreaite.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ocreaite.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreens(navController: NavController) {
    val pagerState = rememberPagerState { 3 }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    title = "Organize your best clothes here",
                    image = R.drawable.onboarding1
                )
                1 -> OnboardingPage(
                    title = "Easy to use and stylish",
                    image = R.drawable.onboarding2
                )
                2 -> OnboardingPage(
                    title = "Over 10,000 outfits waiting for you",
                    image = R.drawable.onboarding3
                )
            }
        }

        // Indicador
        PagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        )

        // Botão
        Button(
            onClick = {
                scope.launch {
                    if (pagerState.currentPage < 2) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        navController.navigate("login") {
                            popUpTo("splash1") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .width(200.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = if (pagerState.currentPage == 2) "Get Started" else "Next",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun OnboardingPage(title: String, image: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 32.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Black,
    inactiveColor: Color = Color.LightGray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(pagerState.pageCount) { index ->
            val color = if (pagerState.currentPage == index) activeColor else inactiveColor
            val width = if (pagerState.currentPage == index) 24.dp else 10.dp

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(width)
                    .height(10.dp)
                    .background(color, RoundedCornerShape(5.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreens() {
    OnboardingScreens(navController = rememberNavController())
}