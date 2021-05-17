package net.gini.pay.ginipaybusiness.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormatSymbols

internal fun TextInputEditText.setTextIfDifferent(text: String) {
    if (this.text.toString() != text) {
        this.setText(text)
    }
}

internal fun String.isNumber(): Boolean {
    val separator = DecimalFormatSymbols.getInstance().decimalSeparator
    return try {
        this.filter { it.isDigit() || it == separator }
            .map { if (it == separator) "." else it.toString() }
            .joinToString(separator = "") { it }
            .toDouble()
        true
    } catch (_: Throwable) {
        false
    }
}

internal fun String.toBackendFormat(): String {
    val separator = DecimalFormatSymbols.getInstance().decimalSeparator
    return this.filter { it.isDigit() || it == separator }
        .map { if (it == separator) "." else it.toString() }
        .joinToString(separator = "") { it }
        .toDouble()
        .toString()
}

internal val amountWatcher = object : TextWatcher {
    private var fractionPosition = -1

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        fractionPosition = if (s != null && start < s.length && (s[start] == ',' || s[start] == '.')) {
            start
        } else -1
    }

    override fun afterTextChanged(s: Editable?) {
        val formatSymbols = DecimalFormatSymbols.getInstance()
        try {
            if (s != null && s.toString().trim().isNotEmpty()) {
                val oldString = s.toString().updateFractionPosition(fractionPosition, formatSymbols.decimalSeparator)
                    .removeLeadingZero()
                    .takeNumberCharacters(formatSymbols.decimalSeparator)
                val fractionIndex = oldString.getDecimalSeparatorIndex(formatSymbols.decimalSeparator)
                val newString = oldString.mapIndexedNotNull { index, c ->
                    when {
                        index == 0 -> c.toString()
                        index < fractionIndex && (fractionIndex - index) % 3 == 0 -> "${formatSymbols.groupingSeparator}$c"
                        index > fractionIndex && index - fractionIndex > 2 -> null
                        index > 11 -> null
                        else -> c.toString()
                    }
                }.joinToString(separator = "") { it }
                if (newString != s.toString()) {
                    s.replace(0, s.length, newString)
                }
            }
        } catch (_: Throwable) {

        }
    }

}

private fun String.updateFractionPosition(fractionPosition: Int, decimalSeparator: Char): String {
    return if (fractionPosition > -1) {
        this.mapIndexedNotNull { index, c ->
            when {
                index == fractionPosition -> decimalSeparator.toString()
                c == ',' || c == '.' -> null
                else -> c.toString()
            }
        }.joinToString(separator = "") { it }
    } else {
        this
    }
}

private fun String.getDecimalSeparatorIndex(decimalSeparator: Char): Int {
    val index = this.indexOf(decimalSeparator)
    return if (index != -1) {
        index
    } else {
        this.length
    }
}

private fun String.takeNumberCharacters(decimalSeparator: Char): String {
    val fractionIndex = this.indexOf(decimalSeparator)
    return this.filterIndexed { index, c -> c.isDigit() || index == fractionIndex }
}

private fun String.removeLeadingZero(): String {
    return if (length > 1 && first() == '0' && this[1] != ',' && this[1] != '.') {
        val firstNonZero = indexOfFirst { it != '0' }
        if (firstNonZero == -1) {
            "0"
        } else {
            this.slice(firstNonZero until length)
        }
    } else this
}