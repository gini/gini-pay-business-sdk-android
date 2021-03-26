package net.gini.pay.app

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.gini.pay.app.databinding.ActivityMainBinding
import net.gini.pay.app.pager.PagerAdapter
import net.gini.pay.app.review.ReviewActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture(), ::photoResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.takePhoto.setOnClickListener {
            takePhoto()
        }

        binding.pager.adapter = PagerAdapter().apply {
            lifecycleScope.launch {
                viewModel.pages.collect { pages ->
                    submitList(pages)
                }
            }
        }

        TabLayoutMediator(binding.indicator, binding.pager) { _, _ -> }.attach()

        binding.payment.setOnClickListener {
            startActivity(ReviewActivity.getStartIntent(this, viewModel.pages.value.map { it.uri }))
        }
    }

    private fun takePhoto() {
        takePictureLauncher.launch(viewModel.getNextPageUri(this@MainActivity))
    }

    private fun photoResult(saved: Boolean) {
        if (saved) {
            viewModel.onPhotoSaved()
        }
    }
}