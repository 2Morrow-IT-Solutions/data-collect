package com.tomorrowit.datacollect.utils.extensions

import android.app.Activity
import android.content.Intent
import android.os.Build
import com.tomorrowit.datacollect.R

fun Activity.openActivity(targetActivity: Activity) {
    this.runOnUiThread {
        startActivity(Intent(this, targetActivity::class.java))
        if (Build.VERSION.SDK_INT >= 34) {
            this.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN, R.anim.push_in_right,
                R.anim.push_out_left
            )
        } else {
            this.overridePendingTransition(
                R.anim.push_in_right,
                R.anim.push_out_left
            )
        }
    }
}

fun Activity.navigateToNewActivity(targetActivity: Activity) {
    this.runOnUiThread {
        val intent = Intent(this, targetActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        this.startActivity(intent)
        if (Build.VERSION.SDK_INT >= 34) {
            this.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            this.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}

fun Activity.finishAnimation() {
    if (Build.VERSION.SDK_INT >= 34) {
        this.overrideActivityTransition(
            Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.push_in_left,
            R.anim.push_out_right
        )
    } else {
        this.overridePendingTransition(
            R.anim.push_in_left,
            R.anim.push_out_right
        )
    }
}