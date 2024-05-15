package com.tomorrowit.datacollect.presentation.tutorial_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.tomorrowit.datacollect.databinding.ActivityTutorialBinding
import com.tomorrowit.datacollect.domain.usecases.PreferenceValueUseCase
import com.tomorrowit.datacollect.presentation.main.MainActivity
import com.tomorrowit.datacollect.utils.Constants
import com.tomorrowit.datacollect.utils.extensions.navigateToNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding

    @Inject
    lateinit var preferenceValueUseCase: PreferenceValueUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition { true }

        val preferenceKey = booleanPreferencesKey(Constants.PREF_KEY_FIRST_TIME)
        lifecycleScope.launch {
            val checked = preferenceValueUseCase.getBoolean(preferenceKey).first()
            if (checked) {
                navigateToNewActivity(MainActivity())
            } else {
                binding = ActivityTutorialBinding.inflate(layoutInflater)
                setContentView(binding.root)
                splashScreen.setKeepOnScreenCondition { false }

                binding.tutorialCheck.setOnCheckedChangeListener { _, b ->
                    lifecycleScope.launch {
                        preferenceValueUseCase.invoke(preferenceKey, b)
                    }
                }

                binding.tutorialButton.setOnClickListener {
                    navigateToNewActivity(MainActivity())
                }
            }
        }
    }
}