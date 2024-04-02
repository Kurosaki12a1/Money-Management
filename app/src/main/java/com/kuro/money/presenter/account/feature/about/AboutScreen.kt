package com.kuro.money.presenter.account.feature.about

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.extension.noRippleClickable

@Composable
fun AboutScreen(
    navController: NavController
) {
    BackHandler { navController.popBackStack() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Toolbar(navController)
    }
}

@Composable
private fun Toolbar(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .align(Alignment.TopStart)
                .noRippleClickable { navController.popBackStack() }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_noel_round),
                contentDescription = "Icon App"
            )
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "Version 1.0.0",
                color = Color.Black.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "By Thịnh pro",
                color = Color.Black.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        }
    }
}