package com.kuro.money.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationGraphRoute.HomeGraph
import com.kuro.money.navigation.routes.NavigationRoute.Home
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails.Edit.AddEvent
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails.Edit.Note
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails.Edit.SelectCategory
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails.Edit.SelectCurrency
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails.Edit.SelectEvent
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails.Edit.SelectWallet
import com.kuro.money.navigation.routes.NavigationRoute.Home.TransactionDetails.Edit.With
import com.kuro.money.presenter.account.feature.wallets.WalletScreen
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
import com.kuro.money.presenter.home.HomeScreen
import com.kuro.money.presenter.home.HomeViewModel
import com.kuro.money.presenter.home.MyWalletViewModel
import com.kuro.money.presenter.home.RecentTransactionViewModel
import com.kuro.money.presenter.home.SpendingReportViewModel
import com.kuro.money.presenter.home.feature.EditTransactionDetailViewModel
import com.kuro.money.presenter.home.feature.EditTransactionsDetailsScreen
import com.kuro.money.presenter.home.feature.TransactionDetailsScreen
import com.kuro.money.presenter.home.feature.TransactionDetailsViewModel
import com.kuro.money.presenter.utils.horizontalComposable

fun NavGraphBuilder.homeNavGraph(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    navigation(
        startDestination = Home.route,
        route = HomeGraph.route,
        builder = {
            /** Bottom Navigation Account Screen*/
            composable(route = Home.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(NavigationGraphRoute.RootGraph.route)
                }
                val homeViewModel = hiltViewModel<HomeViewModel>(parentEntry)
                val myWalletViewModel = hiltViewModel<MyWalletViewModel>(parentEntry)
                val spendingReportViewModel = hiltViewModel<SpendingReportViewModel>(parentEntry)
                val recentTransactionViewModel =
                    hiltViewModel<RecentTransactionViewModel>(parentEntry)
                HomeScreen(
                    navHostController,
                    paddingValues,
                    homeViewModel,
                    myWalletViewModel,
                    spendingReportViewModel,
                    recentTransactionViewModel
                )
            }
            horizontalComposable(route = Home.Wallet.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(Home.Wallet.route)
                }
                val myWalletViewModel = hiltViewModel<MyWalletViewModel>(parentEntry)
                WalletScreen(
                    navController = navHostController,
                    myWalletViewModel = myWalletViewModel
                )
            }
            navigation(
                startDestination = TransactionDetails.route,
                route = HomeGraph.TransactionDetails.route,
                builder = {
                    composable(
                        route = "${TransactionDetails.route}/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) {
                        val subEntry =
                            remember(it) { navHostController.getBackStackEntry(HomeGraph.TransactionDetails.route) }
                        val transactionDetailsViewModel =
                            hiltViewModel<TransactionDetailsViewModel>(subEntry)
                        TransactionDetailsScreen(
                            navController = navHostController,
                            it.arguments?.getLong("id") ?: 0L,
                            transactionDetailsViewModel
                        )
                    }
                    navigation(
                        startDestination = TransactionDetails.Edit.route,
                        route = HomeGraph.EditTransactions.route, builder = {
                            composable(
                                route = "${TransactionDetails.Edit.route}/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.LongType })
                            ) {
                                val subEntry = remember(it) {
                                    navHostController.getBackStackEntry(HomeGraph.EditTransactions.route)
                                }
                                val editTransactionDetailViewModel =
                                    hiltViewModel<EditTransactionDetailViewModel>(subEntry)
                                EditTransactionsDetailsScreen(
                                    navController = navHostController,
                                    it.arguments?.getLong("id") ?: 0L,
                                    editTransactionDetailViewModel
                                )
                            }
                            composable(route = SelectCategory.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(HomeGraph.EditTransactions.route)
                                }
                                val transactionViewModel =
                                    hiltViewModel<EditTransactionDetailViewModel>(parentEntry)
                                val selectedCategoryViewModel =
                                    hiltViewModel<SelectCategoryViewModel>(parentEntry)
                                SelectCategoryScreen(
                                    navHostController,
                                    transactionViewModel,
                                    selectedCategoryViewModel
                                )
                            }
                            composable(route = Note.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(HomeGraph.EditTransactions.route)
                                }
                                val transactionViewModel =
                                    hiltViewModel<EditTransactionDetailViewModel>(parentEntry)
                                NoteScreen(navHostController, transactionViewModel)
                            }
                            composable(route = SelectWallet.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(HomeGraph.EditTransactions.route)
                                }
                                val transactionViewModel =
                                    hiltViewModel<EditTransactionDetailViewModel>(parentEntry)
                                SelectWalletScreen(navHostController, transactionViewModel)
                            }
                            composable(route = SelectEvent.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(HomeGraph.EditTransactions.route)
                                }
                                val transactionViewModel =
                                    hiltViewModel<EditTransactionDetailViewModel>(parentEntry)
                                SelectEventScreen(navHostController, transactionViewModel)
                            }
                            composable(route = With.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(HomeGraph.EditTransactions.route)
                                }
                                val transactionViewModel =
                                    hiltViewModel<EditTransactionDetailViewModel>(parentEntry)
                                SelectPeopleScreen(navHostController, transactionViewModel)
                            }
                            composable(route = SelectCurrency.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(HomeGraph.EditTransactions.route)
                                }
                                val transactionViewModel =
                                    hiltViewModel<EditTransactionDetailViewModel>(parentEntry)
                                SelectCurrencyScreen(navHostController, transactionViewModel)
                            }
                            navigation(
                                startDestination = AddEvent.route,
                                route = HomeGraph.EditTransactionEvent.route,
                                builder = {
                                    composable(route = AddEvent.route) {
                                        val parentEntry = remember(it) {
                                            navHostController.getBackStackEntry(HomeGraph.EditTransactionEvent.route)
                                        }
                                        val addEventScreenViewModel =
                                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                                        AddEventScreen(
                                            navHostController,
                                            addEventScreenViewModel,
                                            onSelectIcon = {
                                                navHostController.navigate(AddEvent.SelectIcon.route)
                                            },
                                            onSelectWallet = {
                                                navHostController.navigate(AddEvent.SelectWallet.route)
                                            },
                                            onSelectCurrency = {
                                                navHostController.navigate(AddEvent.SelectCurrency.route)
                                            }
                                        )
                                    }
                                    composable(route = AddEvent.SelectIcon.route) {
                                        val parentEntry = remember(it) {
                                            navHostController.getBackStackEntry(HomeGraph.EditTransactionEvent.route)
                                        }
                                        val addEventScreenViewModel =
                                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                                        SelectIconScreen(navHostController, addEventScreenViewModel)
                                    }
                                    composable(route = AddEvent.SelectCurrency.route) {
                                        val parentEntry = remember(it) {
                                            navHostController.getBackStackEntry(HomeGraph.EditTransactionEvent.route)
                                        }
                                        val addEventScreenViewModel =
                                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                                        SelectCurrencyScreen(
                                            navHostController,
                                            addEventScreenViewModel
                                        )
                                    }
                                    composable(route = AddEvent.SelectWallet.route) {
                                        val parentEntry = remember(it) {
                                            navHostController.getBackStackEntry(HomeGraph.EditTransactionEvent.route)
                                        }
                                        val addEventScreenViewModel =
                                            hiltViewModel<AddEventScreenViewModel>(parentEntry)
                                        SelectWalletScreen(
                                            navHostController,
                                            addEventScreenViewModel
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}
