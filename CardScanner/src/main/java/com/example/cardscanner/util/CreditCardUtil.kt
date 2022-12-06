package com.example.cardscanner.util

import com.example.cardscanner.R
import com.example.cardscanner.scanner.CardType
import java.util.regex.Pattern

internal fun String.getCardType(): CardType {

    listOfPattern().mapIndexed { index, s ->

        val matcher = Pattern.compile(s).matcher(this)

        if (matcher.matches()) {

            return when (index) {

                0 -> VISA
                1 -> MASTERCARD
                2 -> AMEX
                3 -> DINER_CLUB
                4 -> DISCOVER
                5 -> JCB
                6 -> BG_GLOBAL
                7 -> CARTE_BLANCHE
                8 -> INSTA_PAYMENT
                9 -> KOREAN_LOCAL
                10 -> LASER
                11 -> MAESTRO
                12 -> SOLO
                13 -> SWITCH
                14 -> UNION_PAY
                15 -> VISA_MASTERCARD
                else -> UNKNOWN

            }

        }

    }

    return UNKNOWN

}

private val VISA = CardType("Visa", R.drawable.icon_visa, creditCardNumberLength = 16)
private val MASTERCARD = CardType("MasterCard", R.drawable.icon_mastercard, creditCardNumberLength = 16)
private val AMEX = CardType("Amex", R.drawable.icon_american_express, creditCardNumberLength = 16)
private val DINER_CLUB = CardType("DinerClub", R.drawable.icon_diner_club, creditCardNumberLength = 14)
private val DISCOVER = CardType("Discover", R.drawable.icon_discover, creditCardNumberLength = 16)
private val JCB = CardType("Jcb", R.drawable.icon_jcb, creditCardNumberLength = 16)
private val MAESTRO = CardType("Maestro", R.drawable.icon_maestro)
private val UNION_PAY = CardType("China Union Pay", R.drawable.icon_unionpay)
private val CARTE_BLANCHE = CardType("Carte Blanche", R.drawable.ic_card_grey)
private val BG_GLOBAL = CardType("BG Global", R.drawable.ic_card_grey)
private val KOREAN_LOCAL = CardType("Korean Local", R.drawable.ic_card_grey)
private val LASER = CardType("Laser", R.drawable.ic_card_grey)
private val SWITCH = CardType("Switch", R.drawable.ic_card_grey)
private val VISA_MASTERCARD = CardType("Visa Master Card", R.drawable.ic_card_grey)
private val INSTA_PAYMENT = CardType("Insta Payment", R.drawable.ic_card_grey)
private val SOLO = CardType("Solo", R.drawable.ic_card_grey)
private val UNKNOWN = CardType("Unknown", R.drawable.ic_card_grey)

private fun listOfPattern() = ArrayList<String>().apply {

    add("^4[0-9]{6,}$") // visa-0
    add("^5[1-5][0-9]{5,}$")  //MasterCard-1
    add("^3[47][0-9]{5,}$")  //AmeExp-2
    add("^3(?:0[0-5]|[68][0-9])[0-9]{4,}$")  //DinClb-3
    add("^6(?:011|5[0-9]{2})[0-9]{3,}$")  //Discover-4
    add("^(?:2131|1800|35[0-9]{3})[0-9]{3,}$")  //Jcb-5
    // other cards
    add("^(6541|6556)[0-9]{12}$") // BCGlobal-6
    add("^389[0-9]{11}$") //   Carte Blanche-7
    // add("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$") //discover
    add("^63[7-9][0-9]{13}$") // Insta Payment-8
    add("^9[0-9]{15}$") // Korean Local-9
    add("^(6304|6706|6709|6771)[0-9]{12,15}$") // Laser-10
    add("^(5018|5020|5038|5893|6304|6759|6761|6762|6763)[0-9]{8,15}$") // Maestro-11
    add("^(6334|6767)[0-9]{12}|(6334|6767)[0-9]{14}|(6334|6767)[0-9]{15}$") // Solo-12
    add("^(4903|4905|4911|4936|6333|6759)[0-9]{12}|(4903|4905|4911|4936|6333|6759)[0-9]{14}|(4903|4905|4911|4936|6333|6759)[0-9]{15}|564182[0-9]{10}|564182[0-9]{12}|564182[0-9]{13}|633110[0-9]{10}|633110[0-9]{12}|633110[0-9]{13}\$") // Switch-13
    add("^(62[0-9]{14,17})$") // Union pay-14
    add("^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})$") // Visa Master card-15

}





