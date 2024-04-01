package com.kuro.money.navigation.utils

/*Explanation:

Sealed class UiEvent: It is a sealed class that defines a specific set of sealed subclasses
(in this case, Navigate, NavigateUp, and ShowSnackBar). A sealed class is useful when you have a
finite set of subtypes and you want all of these subtypes to be defined in the same file.

data class Navigate(val route: String) : UiEvent(): A data class that represents a Navigate event.
navigation. It contains a route parameter that indicates the route to which you want to navigate.

object NavigateUp : UiEvent(): A single object that represents a navigation back event.
When using object, a single instance of this class is created, since it does not have any parameters.
This object is used to indicate that a back navigation action should be performed.

In short, UiEvent is a sealed class that encapsulates different types of events related to
the user interface. Each subclass within UiEvent represents a specific type of event,
such as navigation to a route, navigation back or showing a snack bar.*/
sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data object NavigateUp : UiEvent()

    data class PreviousBackStackEntry(val data: Any) : UiEvent()
}