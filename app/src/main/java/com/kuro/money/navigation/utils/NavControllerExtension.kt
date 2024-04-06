package com.kuro.money.navigation.utils

import androidx.navigation.NavController
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle

fun NavController.navigate(event: UiEvent) {
    when (event) {
        is UiEvent.Navigate -> {
            this.navigate(event.route) {}
        }

        UiEvent.NavigateUp -> {
            this.navigateUp()
        }

        is UiEvent.PreviousBackStackEntry -> {
            when (event.data) {

                is String -> {

                }

                is Boolean -> {

                }

                else -> {

                }
            }

            this.popBackStackWithLifeCycle()
        }
    }
}
