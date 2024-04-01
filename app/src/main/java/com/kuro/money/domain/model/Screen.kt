package com.kuro.money.domain.model

enum class SelectionUI(val route: String) {
    HOME("home"),
    TRANSACTION("transaction"),
    BUDGETS("budgets"),
    ACCOUNT("account"),
    MY_WALLET("my_wallet"),
    ADD_WALLET("add_wallet"),
    MY_CATEGORY("my_category"),
    MY_EVENT("my_event"),
    MY_RECURRING_TRANSACTIONS("my_recurring_transactions"),
    MY_BILLS("my_bills"),
    MY_DEBTS("my_debts"),
    MY_TOOLS("my_tools"),
    MY_TRAVEL_MODE("my_travel_mode"),
    MY_SETTINGS("my_setting"),
    MY_ABOUT("my_about"),
    SUB_GRAPH_HOME("sub_graph_home"),
    SUB_GRAPH_TRANSACTION("sub_graph_transaction"),
    SUB_GRAPH_ADD_TRANSACTION("sub_graph_add_transaction"),
    SUB_GRAPH_ACCOUNT("sub_graph_account"),
    ADD_TRANSACTION("add_transaction"),
    SELECT_CATEGORY("select_category"),
    NOTE("note"),
    WALLET("wallet"),
    WALLET_FROM_EVENT("wallet_from_event"),
    WITH("with"),
    EVENT("event"),
    ADD_EVENT("add_event"),
    SELECT_CURRENCY("select_currency"),
    SELECT_ICON("select_icon"),
    EDIT_WALLET("edit_wallet")
}

fun screenRoute(parentRoute: String, childRoute : String) = "$parentRoute/$childRoute"