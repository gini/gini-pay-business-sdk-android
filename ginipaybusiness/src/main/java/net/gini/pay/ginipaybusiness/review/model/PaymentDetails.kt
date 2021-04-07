package net.gini.pay.ginipaybusiness.review.model

import net.gini.android.models.ExtractionsContainer

data class PaymentDetails(
    val recipient: String,
    val iban: String,
    val amount: String,
    val purpose: String,
)

internal fun ExtractionsContainer.toPaymentDetails() = PaymentDetails(
    recipient = specificExtractions["paymentRecipient"]?.value ?: "",
    iban = specificExtractions["iban"]?.value ?: "",
    amount = specificExtractions["amountToPay"]?.value ?: "",
    purpose = specificExtractions["paymentPurpose"]?.value ?: "",
)