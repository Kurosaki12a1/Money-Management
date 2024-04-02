package com.kuro.money.presenter.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuro.money.domain.model.BottomNavItem
import com.kuro.money.domain.model.SelectionUI
import com.kuro.money.domain.model.generateListBottomNavItem
import com.kuro.money.navigation.graph.RootNavGraph
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.ui.theme.Teal200
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Init
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainViewModel
            MainScreen()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navRouteDestination = navBackStackEntry?.destination?.route
    val currentRoute = remember { mutableStateOf(SelectionUI.HOME.route) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (navRouteDestination == SelectionUI.HOME.route
                || navRouteDestination == SelectionUI.TRANSACTION.route
                || navRouteDestination == SelectionUI.BUDGETS.route
                || navRouteDestination == SelectionUI.ACCOUNT.route
            ) {
                BottomBar(routeSelected = currentRoute.value) {
                    currentRoute.value = it
                    navController.navigate(currentRoute.value) {
                        launchSingleTop = true
                    }
                }
            }
        },
        floatingActionButton = {
            if (navRouteDestination == SelectionUI.HOME.route
                || navRouteDestination == SelectionUI.TRANSACTION.route
                || navRouteDestination == SelectionUI.BUDGETS.route
                || navRouteDestination == SelectionUI.ACCOUNT.route
            ) {
                MainFloatingButton {
                    currentRoute.value = it
                    navController.navigate(currentRoute.value) {
                        launchSingleTop = true
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { RootNavGraph(navController, NavigationGraphRoute.HomeGraph) })
}

@Composable
private fun MainFloatingButton(
    onClick: (String) -> Unit
) {
    FloatingActionButton(
        onClick = { onClick(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route) },
        backgroundColor = Teal200
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add transaction",
            tint = Color.White
        )
    }
}

@Composable
private fun BottomBar(
    routeSelected: String, onClick: (String) -> Unit
) {
    val listItems = generateListBottomNavItem(LocalContext.current)
    BottomAppBar(
        cutoutShape = MaterialTheme.shapes.large.copy(CornerSize(36.dp)),
        contentColor = Color.White,
        backgroundColor = Color.White
    ) {
        BottomNavigation(
            backgroundColor = Color.White, contentColor = Color.Black
        ) {
            listItems.forEach { item ->
                BottomNavItem(item, routeSelected, onClick)
            }
        }
    }
}

@Composable
private fun RowScope.BottomNavItem(
    item: BottomNavItem, routeSelected: String, onClick: (String) -> Unit
) {
    val isSelected = routeSelected == item.route
    BottomNavigationItem(selected = isSelected, onClick = { onClick(item.route) }, icon = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (item.badgeCount > 0) {
                BadgedBox(badge = {
                    Text(text = item.badgeCount.toString())
                }) {
                    if (item.icon != null) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = if (isSelected) Color.Black else Color.Black.copy(
                                alpha = 0.3f
                            )
                        )
                    }
                }
            } else {
                if (item.icon != null) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name,
                        tint = if (isSelected) Color.Black else Color.Black.copy(
                            alpha = 0.3f
                        )
                    )
                }
            }
        }
    }, label = {
        Text(
            text = item.name,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.3f),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }, alwaysShowLabel = true
    )
}