package net.gini.pay.ginipaybusiness.util

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.setTextIfDifferent(text: String) {
    if (this.text.toString() != text) {
        this.setText(text)
    }
}