package com.kuro.money.presenter.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

object PermissionUtils {
    @Composable
    fun requestPermission(permission: String, onRequest: (Boolean) -> Unit) {
        val context = LocalContext.current
        if (hasPermission(context, permission)) return
        val launcherRequestPermission =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                onRequest(it)
            }
        launcherRequestPermission.launch(permission)
    }

    @Composable
    fun requestMultiPermissions(
        vararg permission: String,
        onRequest: (Map<String, Boolean>) -> Unit
    ) {
        val context = LocalContext.current
        if (hasPermissions(context, *permission)) return
        val launcherRequestPermission =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
                onRequest(it)
            }
        launcherRequestPermission.launch(arrayOf(*permission))
    }

    fun hasPermission(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}