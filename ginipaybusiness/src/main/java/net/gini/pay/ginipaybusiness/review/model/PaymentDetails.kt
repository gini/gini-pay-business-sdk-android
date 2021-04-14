package net.gini.pay.ginipaybusiness.review.model

import net.gini.android.models.ExtractionsContainer
import net.gini.android.models.SpecificExtraction

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


fun MutableMap<String, SpecificExtraction>.withFeedback(paymentDetails: PaymentDetails): Map<String, SpecificExtraction> {
    this["paymentRecipient"] = this["paymentRecipient"].let { extraction ->
        SpecificExtraction(
            extraction?.name ?: "paymentRecipient",
            paymentDetails.recipient,
            extraction?.entity,
            extraction?.box,
            extraction?.candidate
        )
    }
    this["iban"] = this["iban"].let { exrtaction ->
        SpecificExtraction(
            exrtaction?.name ?: "iban",
            paymentDetails.iban,
            exrtaction?.entity,
            exrtaction?.box,
            exrtaction?.candidate
        )
    }
    this["amountToPay"] = this["amountToPay"].let { exrtaction ->
        SpecificExtraction(
            exrtaction?.name ?: "amountToPay",
            paymentDetails.amount,
            exrtaction?.entity,
            exrtaction?.box,
            exrtaction?.candidate
        )
    }
    this["paymentPurpose"] = this["paymentPurpose"].let { exrtaction ->
        SpecificExtraction(
            exrtaction?.name ?: "paymentPurpose",
            paymentDetails.purpose,
            exrtaction?.entity,
            exrtaction?.box,
            exrtaction?.candidate
        )
    }
    return this
}