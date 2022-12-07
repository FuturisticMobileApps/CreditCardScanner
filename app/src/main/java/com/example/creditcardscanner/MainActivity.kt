package com.example.creditcardscanner

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.cardscanner.CardScanner
import com.example.cardscanner.scanner.CardDetails
import com.example.cardscanner.scanner.CreditCardDetails
import com.example.creditcardscanner.databinding.ActivityMainBinding
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)

        setContentView(bind.root)

        getCardDetails()

    }

    private fun getCardDetails() {

        with(bind) {

            tvScanner.setOnClickListener {

                CardScanner(object : CreditCardDetails {

                    override fun cardDetails(cardDetails: CardDetails) {

                        val expiryDate = cardDetails.expiryDate.split("/")

                        etCreditCard.setText(cardDetails.cardNumber)

                        acsMonth.setText(expiryDate[0])

                        acsYear.setText(expiryDate[1])

                        if (cardDetails.cardType != "Unknown") {
                            tvCardtypeLabel.visibility = View.VISIBLE
                            tvCardType.visibility = View.VISIBLE
                            tvCardType.text = cardDetails.cardType
                        }
                    }

                }).show(supportFragmentManager, "Scan_Card")

            }
        }
    }


}


