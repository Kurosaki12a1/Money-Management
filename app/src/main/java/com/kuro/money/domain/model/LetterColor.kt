package com.kuro.money.domain.model

import androidx.compose.ui.graphics.Color

// Enum defining a color for each letter of the alphabet.
enum class LetterColor(val letter: Char, val color: Color) {
    A('A', Color(0xFFE6194B)), // Red
    B('B', Color(0xFF3CB44B)), // Green
    C('C', Color(0xFFFFE119)), // Yellow
    D('D', Color(0xFF4363D8)), // Blue
    E('E', Color(0xFFF58231)), // Orange
    F('F', Color(0xFF911EB4)), // Purple
    G('G', Color(0xFF46F0F0)), // Cyan
    H('H', Color(0xFFF032E6)), // Magenta
    I('I', Color(0xFFBCF60C)), // Lime
    J('J', Color(0xFFFABEBE)), // Pink pastel
    K('K', Color(0xFF008080)), // Teal
    L('L', Color(0xFFE6BEFF)), // Lavender
    M('M', Color(0xFF9A6324)), // Brown
    N('N', Color(0xFFFFFAC8)), // Cream
    O('O', Color(0xFF800000)), // Maroon
    P('P', Color(0xFFAAFFC3)), // Mint
    Q('Q', Color(0xFF808000)), // Olive
    R('R', Color(0xFFFFD8B1)), // Peach
    S('S', Color(0xFF000075)), // Navy
    T('T', Color(0xFF808080)), // Grey
    U('U', Color(0xFFFFFFFF)), // White
    V('V', Color(0xFF000000)), // Black
    W('W', Color(0xFFA9A9A9)), // Dark grey
    X('X', Color(0xFFFABED4)), // Cute pink
    Y('Y', Color(0xFFFFD700)), // Gold
    Z('Z', Color(0xFFFF5733)); // Orange-red

    companion object {
        fun getColor(index : Int) : Color = entries[index].color
        fun getColor(letter: Char): Color {
            return entries.firstOrNull { it.letter == letter.uppercaseChar() }?.color ?: Color.Black // Default is Black color
        }
    }
}