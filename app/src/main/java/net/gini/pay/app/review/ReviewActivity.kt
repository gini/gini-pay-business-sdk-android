package net.gini.pay.app.review

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.gini.pay.app.databinding.ActivityReviewBinding
import net.gini.pay.app.review.ReviewViewModel.ReviewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewActivity : AppCompatActivity() {

    private val viewModel: ReviewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.uploadDocuments(contentResolver, intent.pageUris)

        lifecycleScope.launch {
            viewModel.uploadState.collect { uploadState ->
                if (uploadState is ReviewState.Success) {
                     viewModel.setDocumentForReview(uploadState.documentId)
                }
                updateViews(binding, uploadState)
            }
        }
    }

    private fun updateViews(binding: ActivityReviewBinding, uploadState: ReviewState) {
        binding.progress.isVisible = uploadState is ReviewState.Loading
        binding.reviewFragment.isVisible = uploadState is ReviewState.Success
        binding.errorMessage.isVisible = uploadState is ReviewState.Failure
    }

    companion object {
        private const val EXTRA_URIS = "EXTRA_URIS"
        fun getStartIntent(context: Context, pages: List<Uri>): Intent = Intent(context, ReviewActivity::class.java).apply {
            putParcelableArrayListExtra(EXTRA_URIS, if (pages is ArrayList<Uri>) pages else ArrayList<Uri>().apply { addAll(pages) })
        }

        private val Intent.pageUris: List<Uri>
            get() = getParcelableArrayListExtra<Uri>(EXTRA_URIS)?.toList() ?: emptyList()
    }
}