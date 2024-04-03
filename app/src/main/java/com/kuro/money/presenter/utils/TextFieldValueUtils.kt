package com.kuro.money.presenter.utils

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object TextFieldValueUtils {
    fun clear(textField: MutableState<TextFieldValue>) {
        textField.value = TextFieldValue(
            text = "",
            selection = TextRange(0)
        )
    }

    fun deleteAt(textField: MutableState<TextFieldValue>) {
        if (textField.value.text.isEmpty() ||
            textField.value.selection.start == 0
        ) return
        val newText = textField.value.text.removeRange(
            textField.value.selection.start - 1,
            textField.value.selection.start
        )
        val newSelection = textField.value.selection.start - 1
        textField.value = TextFieldValue(
            text = newText,
            selection = TextRange(newSelection)
        )
    }

    fun add(textField: MutableState<TextFieldValue>, str: String) {
        val currentSelection = textField.value.selection.start
        val newText = if (textField.value.text.length > 1) {
            textField.value.text.substring(
                0,
                currentSelection
            ) + str + textField.value.text.substring(currentSelection)
        } else textField.value.text + str
        textField.value = TextFieldValue(
            text = newText,
            selection = TextRange(currentSelection + str.length)
        )
    }

    fun set(textField: MutableState<TextFieldValue>, text : String) {
        textField.value = TextFieldValue(
            text = text,
            selection = TextRange(text.length)
        )
    }


}