package com.example.cardscanner.util

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.example.cardscanner.scanner.CardType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

internal fun TextInputEditText.isValidString(): Boolean =
    text.toString().isValidString()

internal fun String?.isValidString(): Boolean =

    if (isNullOrEmpty()) false
    else trim { it <= ' ' }.isNotEmpty() &&

            !trim { it <= ' ' }.equals("null", ignoreCase = true) &&

            !trim { it <= ' ' }.equals("", ignoreCase = true)


internal fun String?.validateString() = if (isNullOrEmpty()) "" else trim()

internal fun String?.setErrorMessage() = if (isNullOrEmpty()) "Unknown error" else trim()

internal fun TextInputEditText.getTextFromTextView(): String =
    if (text.toString().isValidString()) text.toString().trim() else ""


internal fun TextInputEditText.setMaxLength(maxLength: Int) {
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}

internal fun TextInputEditText.setDrawableStart(drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
        0,
        0,
        drawable,
        0
    )
}

internal fun TextInputEditText.setCreditCard(cardType: (cardType: CardType) -> Unit) {

    with(getTextFromTextView().getCardType()) {

        setDrawableStart(cardIcon)

        setMaxLength(creditCardNumberLength)

        cardType(this)
    }

}

internal fun TextInputEditText.setExpiryDate() {

    // TODO: simplify code here + test

    var isClearPressed = false

    var selectionIndex = 0

    var maxLength = 5

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

            setOnKeyListener { _, keyCode, _ ->

                if (keyCode == KeyEvent.KEYCODE_CLEAR || keyCode == KeyEvent.KEYCODE_DEL) {

                    isClearPressed = true

                    if (getTextFromTextView() == "/" || !getTextFromTextView().isValidString()) {
                        setText("")
                        isClearPressed = false
                    }
                }

                return@setOnKeyListener false
            }


            var targetString = getTextFromTextView()


            if (targetString.isValidString()) {

                if (!targetString.contains("/")) targetString = "$targetString/"

                if ("01".contains(targetString.first())) {

                    maxLength = 5

                    if (targetString.length == 2) {

                        selectionIndex = 1

                    } else if (targetString.length == 3 && targetString[0].toString() == "1" && "3456789".contains(
                            targetString[1].toString())
                    ) {

                        selectionIndex = targetString.length

                        targetString = "${targetString.first()}/${targetString[1]}"

                        maxLength = 4

                    } else if (isClearPressed && targetString.last().toString() == "/") {

                        selectionIndex = targetString.length - 1

                        isClearPressed = false
                    } else {

                        selectionIndex = targetString.length

                    }


                } else if ("23456789".contains(targetString.first())) {

                    if (isClearPressed && targetString.last().toString() == "/") {

                        selectionIndex = targetString.length - 1

                        isClearPressed = false

                    } else {

                        selectionIndex = targetString.length

                    }

                    maxLength = 4

                }

                if (targetString == "/") {

                    targetString = ""

                    isClearPressed = false

                }


                removeTextChangedListener(this)

                setText(targetString)

                if (targetString.isValidString() && selectionIndex <= targetString.length) setSelection(
                    selectionIndex)

                setMaxLength(maxLength)

                addTextChangedListener(this)


            }

        }

        override fun afterTextChanged(p0: Editable?) {


        }

    }

    addTextChangedListener(textWatcher)

}

fun TextInputLayout?.clearTextInputLayout() {

    this?.apply {

        if (this.isErrorEnabled) {

            this.error = null

            this.isErrorEnabled = false

        }

    }

}

fun TextInputLayout.setErrorMessage(errorMessage: String) {

    isErrorEnabled = true
    error = errorMessage
    setErrorIconDrawable(0)
}


fun TextInputEditText.clearError(textInputLayout: TextInputLayout) {

    doOnTextChanged { _, _, _, _ ->
        textInputLayout.clearTextInputLayout()
    }

    onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->

        if (hasFocus) {

            textInputLayout.clearTextInputLayout()

        }
    }

}

fun TextInputEditText.isValidExpiryDate(): Boolean {

    if (!isValidString() || !getTextFromTextView().contains("/") || getTextFromTextView().length < 4) return false

    val dateStringArr = getTextFromTextView().split("/")

    if ("20${dateStringArr[1]}".toInt() < getCurrentYear() || (dateStringArr[0].toInt() < getCurrentMonth() && "20${dateStringArr[1]}".toInt() == getCurrentYear())) return false

    return true
}

private fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

private fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH)




