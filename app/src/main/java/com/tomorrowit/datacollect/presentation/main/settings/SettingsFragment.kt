package com.tomorrowit.datacollect.presentation.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tomorrowit.datacollect.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)
        .apply {
            binding = this
            addPreferenceFragment()
        }.root

    private fun addPreferenceFragment() {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.apply {
            replace(
                binding.settingsFragmentContainer.id,
                PreferenceFragment()
            )
            addToBackStack(null)
            commit()
        }
    }
}