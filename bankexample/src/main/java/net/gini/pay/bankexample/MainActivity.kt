package net.gini.pay.bankexample

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.gini.pay.bankexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        binding.output.text = "$action :: $data"
    }
}