package net.gini.pay.ginipaybusiness.review.model

import net.gini.android.models.ExtractionsContainer

data class PaymentDetails(
    val recipient: String,
    val iban: String,
    val amount: String,
    val purpose: String,
    internal val extractions: ExtractionsContainer? = null
)

internal fun ExtractionsContainer.toPaymentDetails() = PaymentDetails(
    recipient = specificExtractions["paymentRecipient"]?.value ?: "",
    iban = specificExtractions["iban"]?.value ?: "",
    amount = specificExtractions["amountToPay"]?.value?.toAmount() ?: "",
    purpose = specificExtractions["paymentPurpose"]?.value ?: "",
    extractions = this
)

internal fun String.toAmount(): String {
    val delimiterIndex = this.indexOf(":")
    return if (delimiterIndex != -1) {
        this.substring(0, delimiterIndex)
    } else {
        this
    }
}
