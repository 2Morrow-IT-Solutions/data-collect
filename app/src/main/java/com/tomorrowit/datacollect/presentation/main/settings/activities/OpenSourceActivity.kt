package com.tomorrowit.datacollect.presentation.main.settings.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.tomorrowit.datacollect.R
import com.tomorrowit.datacollect.databinding.ActivityOpenSourceBinding
import com.tomorrowit.datacollect.utils.extensions.finishAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenSourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenSourceBinding
    private lateinit var libraryNames: Array<String>
    private lateinit var libraryLinks: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        libraryNames = this@OpenSourceActivity.resources.getStringArray(R.array.library_names)
        libraryLinks = this@OpenSourceActivity.resources.getStringArray(R.array.library_links)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this@OpenSourceActivity, R.layout.simple_list_item_1, libraryNames)
        binding.activityOpenSourceListview.adapter = adapter

        binding.activityOpenSourceListview.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, p2, _ -> openUrl(libraryLinks[p2]) }
        binding.activityOpenSourceToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun openUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun finish() {
        super.finish()
        finishAnimation()
    }
}