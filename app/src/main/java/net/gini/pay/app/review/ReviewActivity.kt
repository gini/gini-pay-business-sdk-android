package net.gini.pay.app.review

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class ReviewActivity : AppCompatActivity() {

    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uploadDocuments(intent.pageUris)
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