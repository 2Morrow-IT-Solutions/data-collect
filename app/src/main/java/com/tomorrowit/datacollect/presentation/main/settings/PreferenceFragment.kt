package com.tomorrowit.datacollect.presentation.main.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tomorrowit.datacollect.BuildConfig
import com.tomorrowit.datacollect.R
import com.tomorrowit.datacollect.domain.usecases.OpenUrlInBrowserUseCase
import com.tomorrowit.datacollect.presentation.main.settings.activities.AboutAppActivity
import com.tomorrowit.datacollect.presentation.main.settings.activities.OpenSourceActivity
import com.tomorrowit.datacollect.utils.extensions.openActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var openUrlInBrowserUseCase: OpenUrlInBrowserUseCase

    private lateinit var aboutApp: Preference
    private lateinit var github: Preference
    private lateinit var linkedIn: Preference
    private lateinit var website: Preference
    private lateinit var openSource: Preference
    private lateinit var versionNumber: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        aboutApp =
            preferenceScreen.findPreference(getString(R.string.key_about_the_app))!!

        github =
            preferenceScreen.findPreference(getString(R.string.key_github))!!

        linkedIn =
            preferenceScreen.findPreference(getString(R.string.key_linked_in))!!

        website =
            preferenceScreen.findPreference(getString(R.string.key_website))!!

        openSource =
            preferenceScreen.findPreference(getString(R.string.key_open_source))!!

        versionNumber =
            preferenceScreen.findPreference(getString(R.string.key_version_number))!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutApp.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            requireActivity().openActivity(AboutAppActivity())
            true
        }

        github.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            openUrlInBrowserUseCase.invoke(requireActivity(), github.summary.toString())
            true
        }

        linkedIn.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            openUrlInBrowserUseCase.invoke(requireActivity(), linkedIn.summary.toString())
            true
        }

        website.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            openUrlInBrowserUseCase.invoke(requireActivity(), website.summary.toString())
            true
        }

        openSource.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            requireActivity().openActivity(OpenSourceActivity())
            true
        }
        versionNumber.summary = "Version ${BuildConfig.VERSION_NAME}"
    }
}