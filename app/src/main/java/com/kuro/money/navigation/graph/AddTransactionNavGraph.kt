package com.kuro.money.navigation.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute.AddTransactionGraph
import com.kuro.money.navigation.routes.NavigationRoute.AddTransaction
import com.kuro.money.presenter.add_transaction.AddTransactionScreen
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.event.SelectEventScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.add_event.AddEventScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.add_event.AddEventScreenViewModel
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_currency.SelectCurrencyScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_icon.SelectIconScreen
import com.kuro.money.presenter.add_transaction.feature.note.NoteScreen
import com.kuro.money.presenter.add_transaction.feature.people.SelectPeopleScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.SelectCategoryScreen
import com.kuro.money.presenter.add_transaction.feature.wallet.SelectWalletScreen

fun NavGraphBuilder.addTransactionNavGraph(navHostController: NavHostController) {
    navigation(
        startDestination = AddTransaction.route,
        route = AddTransactionGraph.route, builder = {
            composable(route = AddTransaction.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                AddTransactionScreen(navHostController, transactionViewModel)
            }
            composable(route = AddTransaction.SelectCategory.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectCategoryScreen(navHostController, transactionViewModel)
            }
            composable(route = AddTransaction.Note.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                NoteScreen(navHostController, transactionViewModel)
            }
            composable(route = AddTransaction.SelectWallet.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectWalletScreen(navHostController, transactionViewModel)
            }
            composable(route = AddTransaction.SelectEvent.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectEventScreen(navHostController, transactionViewModel)
            }
            composable(route = AddTransaction.With.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectPeopleScreen(navHostController, transactionViewModel)
            }
            composable(route = AddTransaction.SelectCurrency.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectCurrencyScreen(navHostController, transactionViewModel)
            }
            navigation(
                startDestination = AddTransaction.AddEvent.route,
                route = AddTransactionGraph.AddEvent.route, builder = {
                    composable(route = AddTransaction.AddEvent.route) {
                        val parentEntry = remember(it) {
                            navHostController.getBackStackEntry(AddTransactionGraph.AddEvent.route)
                        }
                        val addEventScreenViewModel =
                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                        AddEventScreen(navHostController, addEventScreenViewModel)
                    }
                    composable(route = AddTransaction.AddEvent.SelectIcon.route) {
                        val parentEntry = remember(it) {
                            navHostController.getBackStackEntry(AddTransactionGraph.AddEvent.route)
                        }
                        val addEventScreenViewModel =
                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                        SelectIconScreen(navHostController, addEventScreenViewModel)
                    }
                    composable(route = AddTransaction.AddEvent.SelectCurrency.route) {
                        val parentEntry = remember(it) {
                            navHostController.getBackStackEntry(AddTransactionGraph.AddEvent.route)
                        }
                        val addEventScreenViewModel =
                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                        SelectCurrencyScreen(navHostController, addEventScreenViewModel)
                    }
                    composable(route = AddTransaction.AddEvent.SelectWallet.route) {
                        val parentEntry = remember(it) {
                            navHostController.getBackStackEntry(AddTransactionGraph.AddEvent.route)
                        }
                        val addEventScreenViewModel =
                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                        SelectWalletScreen(navHostController, addEventScreenViewModel)
                    }
                }
            )
        }
    )
}