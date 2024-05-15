package com.tomorrowit.datacollect.utils

import android.view.View

object ViewHelper {
    @JvmStatic
    fun showView(view: View) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    fun hideView(view: View) {
        if (view.visibility != View.GONE) {
            view.visibility = View.GONE
        }
    }
}