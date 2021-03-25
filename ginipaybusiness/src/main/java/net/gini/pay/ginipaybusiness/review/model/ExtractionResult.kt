package net.gini.pay.ginipaybusiness.review.model

sealed class ExtractionResult {
    class Success(val extractions: Extractions) : ExtractionResult()
    class Error(val error: Throwable) : ExtractionResult()
    object Loading : ExtractionResult()
}