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
import com.kuro.money.presenter.add_transaction.feature.select_category.SelectCategoryViewModel
import com.kuro.money.presenter.add_transaction.feature.wallet.SelectWalletScreen
import com.kuro.money.presenter.utils.horizontalComposable
import com.kuro.money.presenter.utils.verticalComposable

fun NavGraphBuilder.addTransactionNavGraph(navHostController: NavHostController) {
    navigation(
        startDestination = AddTransaction.route,
        route = AddTransactionGraph.route, builder = {
            verticalComposable(route = AddTransaction.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                AddTransactionScreen(navHostController, transactionViewModel)
            }
            verticalComposable(route = AddTransaction.SelectCategory.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                val selectedCategoryViewModel = hiltViewModel<SelectCategoryViewModel>(parentEntry)
                SelectCategoryScreen(
                    navHostController,
                    transactionViewModel,
                    selectedCategoryViewModel
                )
            }
            horizontalComposable(route = AddTransaction.Note.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                NoteScreen(navHostController, transactionViewModel)
            }
            verticalComposable(route = AddTransaction.SelectWallet.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectWalletScreen(navHostController, transactionViewModel)
            }
            horizontalComposable(route = AddTransaction.SelectEvent.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectEventScreen(navHostController, transactionViewModel)
            }
            horizontalComposable(route = AddTransaction.With.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(AddTransactionGraph.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectPeopleScreen(navHostController, transactionViewModel)
            }
            horizontalComposable(route = AddTransaction.SelectCurrency.route) {
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
                        AddEventScreen(
                            navHostController,
                            addEventScreenViewModel,
                            onSelectIcon = {
                                navHostController.navigate(AddTransaction.AddEvent.SelectIcon.route)
                            },
                            onSelectCurrency = {
                                navHostController.navigate(AddTransaction.AddEvent.SelectCurrency.route)
                            },
                            onSelectWallet =  {
                                navHostController.navigate(AddTransaction.AddEvent.SelectWallet.route)
                            }
                        )
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