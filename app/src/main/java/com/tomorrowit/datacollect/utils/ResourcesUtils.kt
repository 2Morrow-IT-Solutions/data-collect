package com.tomorrowit.datacollect.utils

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.provider.Settings
import java.net.URLDecoder
import java.text.DecimalFormat

object ResourcesUtils {
    @JvmStatic
    fun getOrientationString(): String {
        val orientation = Resources.getSystem().configuration.orientation
        return when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
            Configuration.ORIENTATION_PORTRAIT -> "Portrait"
            else -> "Undefined"
        }
    }

    @JvmStatic
    fun getNavigationMethod(): String {
        return when (Resources.getSystem().configuration.navigation) {
            Configuration.NAVIGATION_NONAV -> "No Navigation"
            Configuration.NAVIGATION_DPAD -> "DPad"
            Configuration.NAVIGATION_TRACKBALL -> "Trackball"
            Configuration.NAVIGATION_WHEEL -> "Wheel"
            else -> "Undefined"
        }
    }

    @JvmStatic
    fun getTouchscreenType(): String {
        return when (Resources.getSystem().configuration.touchscreen) {
            Configuration.TOUCHSCREEN_NOTOUCH -> "No Touchscreen"
            Configuration.TOUCHSCREEN_FINGER -> "Finger Touch"
            Configuration.TOUCHSCREEN_STYLUS -> "Stylus Touch"
            else -> "Undefined"
        }
    }

    @JvmStatic
    fun getKeyboardAvailability(): String {
        return when (Resources.getSystem().configuration.keyboard) {
            Configuration.KEYBOARD_NOKEYS -> "No Keyboard"
            Configuration.KEYBOARD_QWERTY -> "QWERTY Keyboard"
            Configuration.KEYBOARD_12KEY -> "12-Key Keyboard"
            else -> "Undefined"
        }
    }

    @JvmStatic
    fun getUiMode(): String {
        return Resources.getSystem().configuration.uiMode.toString()
    }

    @JvmStatic
    fun getFontScale(): String {
        return Resources.getSystem().configuration.fontScale.toString()
    }

    fun isNightMode(configuration: Configuration): Boolean {
        return (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    @JvmStatic
    fun getScreenLayout(): String {
        return Resources.getSystem().configuration.screenLayout.toString()
    }

    fun hasPermission(permissionType: String, vararg args: String): Boolean {
        return try {
            val securityManager = System.getSecurityManager()
            if (securityManager != null) {
                val permission = when (permissionType) {
                    "java.io.FilePermission" -> {
                        if (args.size != 2) {
                            throw IllegalArgumentException("Invalid arguments for FilePermission")
                        }
                        java.io.FilePermission(args[0], args[1])
                    }

                    "java.net.SocketPermission" -> {
                        if (args.size != 2) {
                            throw IllegalArgumentException("Invalid arguments for SocketPermission")
                        }
                        java.net.SocketPermission(args[0], args[1])
                    }

                    "java.security.SecurityPermission" -> {
                        if (args.size != 1) {
                            throw IllegalArgumentException("Invalid arguments for SecurityPermission")
                        }
                        java.security.SecurityPermission(args[0])
                    }

                    else -> throw IllegalArgumentException("Unsupported permission type: $permissionType")
                }
                securityManager.checkPermission(permission)
                true
            } else {
                true
            }
        } catch (e: SecurityException) {
            false
        }
    }

    fun getClipBoardText(context: Context): String {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val clipItem = clipData.getItemAt(0)
            if (clipItem.text != null) {
                return clipItem.text.toString()
            }
        }
        return ""
    }

    fun getRingerModeString(context: Context): String {
        val contentResolver: ContentResolver = context.contentResolver
        val ringerMode = Settings.System.getInt(contentResolver, Settings.System.MODE_RINGER, -1)
        return when (ringerMode) {
            android.media.AudioManager.RINGER_MODE_NORMAL -> "Normal"
            android.media.AudioManager.RINGER_MODE_SILENT -> "Silent"
            android.media.AudioManager.RINGER_MODE_VIBRATE -> "Vibrate"
            else -> "Unknown"
        }
    }

    fun extractTitleFromUrl(url: String): String? {
        val titleIndex = url.indexOf("title=")
        if (titleIndex != -1) {
            val titleSubstring = url.substring(titleIndex + 6)

            val ampersandIndex = titleSubstring.indexOf("&")

            if (ampersandIndex != -1) {
                val titleWithEncodedSpaces = titleSubstring.substring(0, ampersandIndex)
                return URLDecoder.decode(titleWithEncodedSpaces, "UTF-8")
            }
        }
        return null
    }

    fun getDeviceSecurityType(context: Context): String {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (keyguardManager.isDeviceSecure) {
            run {
                when {
                    keyguardManager.isKeyguardSecure -> {
                        when {
                            keyguardManager.isDeviceSecure && keyguardManager.isKeyguardSecure -> {
                                when {
                                    keyguardManager.isKeyguardSecure -> {
                                        "yes"
                                    }

                                    else -> "Unknown"
                                }
                            }

                            else -> "Unknown"
                        }
                    }

                    else -> "Unknown"
                }
            }
        } else {
            "Not Secure"
        }
    }

    fun formatMemorySize(memorySizeBytes: Long): String {
        val memorySizeGB = memorySizeBytes.toDouble() / (1024 * 1024 * 1024)
        val df = DecimalFormat("#.##")
        return df.format(memorySizeGB)
    }
}