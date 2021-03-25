package net.gini.pay.ginipaybusiness

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.gini.android.DocumentManager
import net.gini.android.models.Document
import net.gini.pay.ginipaybusiness.review.model.ExtractionResult
import net.gini.pay.ginipaybusiness.review.model.getExtractions

class GiniBusiness(
    private val documentManager: DocumentManager
) {

    private val _documentFlow = MutableStateFlow<ExtractionResult>(ExtractionResult.Loading)
    val documentFlow: StateFlow<ExtractionResult> = _documentFlow

    suspend fun setDocumentForReview(document: Document) {
        _documentFlow.value = ExtractionResult.Loading
        try {
            val extractions = documentManager.getExtractions(document)
            // TODO get image url for pages and include it in result
            _documentFlow.value = ExtractionResult.Success(getExtractions(document, emptyList(), extractions))
        } catch (throwable: Throwable) {
            _documentFlow.value = ExtractionResult.Error(throwable)
        }
    }
}