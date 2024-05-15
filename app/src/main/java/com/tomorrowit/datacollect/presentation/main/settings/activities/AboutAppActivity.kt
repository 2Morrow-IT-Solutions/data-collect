package com.tomorrowit.datacollect.presentation.main.settings.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tomorrowit.datacollect.databinding.ActivityAboutAppBinding
import com.tomorrowit.datacollect.utils.extensions.finishAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityAboutAppToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        finishAnimation()
    }
}