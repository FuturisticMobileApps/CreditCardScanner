package com.example.cardscanner.scanner

import com.example.cardscanner.R

data class CardDetails(
     val cardNumber: String,
     val expiryDate: String,
     val cardType: String = "Unknown",
     val cardIcon : Int = R.drawable.unknown
)
