package com.tomorrowit.datacollect.domain.usecases

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.tomorrowit.datacollect.R

class OpenUrlInBrowserUseCase {

    operator fun invoke(activity: Activity, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            activity.startActivity(browserIntent)
        } catch (e: Exception) {
            Log.e(
                "OpenUrlInBrowserUseCase",
                "Failed to open URL, the user has no browser app installed."
            )
            Toast.makeText(
                activity,
                activity.getString(R.string.no_browser_installed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}