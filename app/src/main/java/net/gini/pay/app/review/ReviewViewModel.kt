package net.gini.pay.app.review

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.gini.android.Gini
import net.gini.android.MediaTypes
import net.gini.pay.app.util.getBytes
import net.gini.pay.ginipaybusiness.GiniBusiness

class ReviewViewModel(
    private val giniApi: Gini,
    private val giniBusiness: GiniBusiness,
) : ViewModel() {

    private val _uploadState: MutableStateFlow<ReviewState> = MutableStateFlow(ReviewState.Loading)
    val uploadState: StateFlow<ReviewState> = _uploadState

    fun uploadDocuments(contentResolver: ContentResolver, pageUris: List<Uri>) {
        viewModelScope.launch {
            _uploadState.value = ReviewState.Loading
            try {
                val documentPages = pageUris.map { pageUri ->
                    val stream = contentResolver.openInputStream(pageUri)
                    check(stream != null) { "ContentResolver failed" }
                    giniApi.documentManager.createPartialDocument(stream.getBytes(), MediaTypes.IMAGE_JPEG)
                }
                val document = giniApi.documentManager.createCompositeDocument(documentPages)
                _uploadState.value = ReviewState.Success(document.id)
            } catch (throwable: Throwable) {
                _uploadState.value = ReviewState.Failure(throwable)
            }
        }
    }

    fun setDocumentForReview(documentId: String) {
        viewModelScope.launch {
            giniBusiness.setDocumentForReview(documentId)
        }
    }

    sealed class ReviewState {
        object Loading: ReviewState()
        class Success(val documentId: String): ReviewState()
        class Failure(val throwable: Throwable): ReviewState()
    }
}