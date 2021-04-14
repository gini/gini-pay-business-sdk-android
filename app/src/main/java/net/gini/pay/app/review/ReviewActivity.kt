package net.gini.pay.app.review

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import net.gini.pay.app.R
import net.gini.pay.app.databinding.ActivityReviewBinding
import net.gini.pay.app.review.ReviewViewModel.ReviewState
import net.gini.pay.ginipaybusiness.GiniBusiness
import net.gini.pay.ginipaybusiness.review.ReviewFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewActivity : AppCompatActivity() {

    private val viewModel: ReviewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = ReviewFragmentFactory(viewModel.giniBusiness)
        super.onCreate(savedInstanceState)
        val binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            viewModel.uploadDocuments(contentResolver, intent.pageUris)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uploadState.collect { uploadState ->
                updateViews(binding, uploadState)
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.review_fragment, ReviewFragment::class.java, null)
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


class ReviewFragmentFactory(private val giniBusiness: GiniBusiness) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return ReviewFragment(giniBusiness)
    }
}