package com.example.creditcardscanner

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cardscanner.CardScanner
import com.example.cardscanner.scanner.CardDetails
import com.example.cardscanner.scanner.CreditCardDetails
import com.example.creditcardscanner.databinding.ActivityMainBinding


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

                        tilCreditCard.setEndIconDrawable(cardDetails.cardIcon)

                        acsMonth.setText(expiryDate[0])

                        acsYear.setText(expiryDate[1])

                        val drawable: Drawable? = ContextCompat.getDrawable(this@MainActivity, cardDetails.cardIcon)

                        etCreditCard.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)

                        if (cardDetails.cardType != "Unknown") {
                            tvCardtypeLabel.visibility = View.VISIBLE
                            tvCardType.visibility = View.VISIBLE
                            tvCardType.text = cardDetails.cardType
                        }
                    }

                }).show(supportFragmentManager, "CreditCardScannerDialog")

            }
        }
    }


}


