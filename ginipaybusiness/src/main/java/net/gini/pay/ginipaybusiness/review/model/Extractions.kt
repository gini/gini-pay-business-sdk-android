package net.gini.pay.ginipaybusiness.review.model

import net.gini.android.models.Document
import net.gini.android.models.ExtractionsContainer

data class Extractions(
    val document: Document,
    val documentImageUrls: List<String>,
    val recipient: String,
    val iban: String,
    val amount: String,
    val purpose: String,
)

internal fun getExtractions(document: Document, documentImageUrls: List<String>, extractionsContainer: ExtractionsContainer) =
    Extractions(
        document, documentImageUrls,
        extractionsContainer.specificExtractions["paymentRecipient"]?.value ?: "",
        extractionsContainer.specificExtractions["iban"]?.value ?: "",
        extractionsContainer.specificExtractions["amountToPay"]?.value ?: "",
        extractionsContainer.specificExtractions["paymentPurpose"]?.value ?: "",
    )