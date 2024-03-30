package com.kuro.money.presenter.account.feature.wallets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.money.R
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.SelectionUI
import com.kuro.money.domain.model.screenRoute
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditWalletScreen(
    navController: NavController,
    viewModel: EditWalletViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BackHandler(
        navBackStackEntry?.destination?.route == screenRoute(
            SelectionUI.ACCOUNT.route,
            SelectionUI.EDIT_WALLET.route
        )
    ) {
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        viewModel.deleteWallet.collectLatest {
            if (it is Resource.Success){
                viewModel.clearData()
                navController.popBackStack()
            }
        }

        viewModel.updateWallet.collectLatest {
            if (it is Resource.Success){
                viewModel.clearData()
                navController.popBackStack()
            }
        }
    }

    val nameWallet = viewModel.name.collectAsState().value
    val iconSelected = viewModel.iconSelected.collectAsState().value
    val currencySelected = viewModel.currencySelected.collectAsState().value
    val isTickEnableNotification = remember { mutableStateOf(false) }
    val isTickExcludeFromTotal = remember { mutableStateOf(false) }
    val isTickArchive = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        /* ToolBar */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.noRippleClickable {
                    navController.popBackStack()
                }
            )
            Text(
                text = stringResource(id = R.string.add_wallet),
                style = MaterialTheme.typography.h6,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.noRippleClickable { viewModel.save() }
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            /* Wallet name and icon */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(painter = iconSelected.toPainterResource(), contentDescription = "Icon",
                    modifier = Modifier.noRippleClickable {
                        navController.navigate(
                            screenRoute(
                                SelectionUI.EDIT_WALLET.route,
                                SelectionUI.SELECT_ICON.route
                            )
                        )
                    })
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (nameWallet.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.name),
                            style = MaterialTheme.typography.h6,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                    BasicTextField(
                        value = nameWallet,
                        onValueChange = { viewModel.setName(it) },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                }
            }
            /* Currency Selection */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        navController.navigate(
                            screenRoute(
                                SelectionUI.EDIT_WALLET.route,
                                SelectionUI.SELECT_CURRENCY.route
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (currencySelected == null) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_w_credit),
                        contentDescription = "Currency",
                        colorFilter = ColorFilter.tint(Color.Black),
                    )
                    Text(
                        text = stringResource(id = R.string.currency),
                        color = Color.Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.body1
                    )
                } else {
                    Image(
                        painter = currencySelected.icon.toPainterResource(),
                        contentDescription = currencySelected.name
                    )
                    Text(
                        text = currencySelected.name,
                        color = Color.Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 20.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.enable_notifications),
                        color = Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = stringResource(id = R.string.enable_notifications_hint),
                        color = Color.Black.copy(0.5f),
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.width(20.dp),
                    checked = isTickEnableNotification.value,
                    onCheckedChange = {
                        isTickEnableNotification.value = it
                    })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.exclude_from_total),
                        color = Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = stringResource(id = R.string.exclude_from_total_hint),
                        color = Color.Black.copy(0.5f),
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.width(20.dp),
                    checked = isTickExcludeFromTotal.value,
                    onCheckedChange = { isTickExcludeFromTotal.value = it })
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 20.dp, horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(end = 5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.archive),
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )
                Text(
                    text = stringResource(id = R.string.archive_hint),
                    style = MaterialTheme.typography.body2,
                    color = Color.Black.copy(0.5f),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                modifier = Modifier.width(20.dp),
                checked = isTickArchive.value,
                onCheckedChange = { isTickArchive.value = it })
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { viewModel.delete() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                Text(
                    text = stringResource(id = R.string.delete_wallet),
                    color = Color.Red,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}