package com.example.cardscanner.scanner

data class CardDetails(
     val cardNumber: String,
     val expiryDate: String,
     val cardType: String = "Unknown",
)
