package com.example.cardscanner.scanner

internal data class CardType(
    val cardType: String = "Unknown",
    val cardIcon: Int,
    val creditCardNumberLength: Int = 19,
)

