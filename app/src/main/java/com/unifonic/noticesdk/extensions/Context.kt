package com.unifonic.noticesdk.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.annotation.StringRes
import com.unifonic.noticesdk.R

const val IDENTIFIER = "Identifier"
const val APP_ID = "app_id"
const val USER_PREFERENCES = "user_preferences"
const val SDK_TOKEN = "sdk_token"
const val FIREBASE_TOKEN = "firebase_token"
const val OLD_FIREBASE_TOKEN = "old_firebase_token"
const val FIREBASE_TOKEN_REGISTERED = "FIREBASE_TOKEN_REGISTERED"
const val FIREBASE_NOTIFICATION_DISABLED = "FIREBASE_NOTIFICATION_DISABLED"

fun Context.userPreferences(): SharedPreferences {
    return getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
}

fun Context.saveIdentifier(identifier: String) {
    userPreferences().edit().putString(IDENTIFIER, identifier).apply()
}

fun Context.getIdentifier(): String? {
    return userPreferences().getString(IDENTIFIER, null)
}


fun Context.saveAppId(appId: String) {
    userPreferences().edit().putString(APP_ID, appId).apply()
}

fun Context.getAppId(): String? {
    return userPreferences().getString(APP_ID, null)
}

fun Context.saveSdkToken(sdkToken: String) {
    userPreferences().edit().putString(SDK_TOKEN, sdkToken).apply()
}

fun Context.getSdkToken(): String? {
    return userPreferences().getString(SDK_TOKEN, null)
}

fun Context.saveFirebaseToken(token: String) {
    getFirebaseToken()?.let {
        saveOldFirebaseToken(it)
    }
    userPreferences().edit().putString(FIREBASE_TOKEN, token).apply()
}

fun Context.getFirebaseToken(): String? {
    return userPreferences().getString(FIREBASE_TOKEN, null)
}

fun Context.saveOldFirebaseToken(token: String) {
    userPreferences().edit().putString(OLD_FIREBASE_TOKEN, token).apply()
}

fun Context.getOldFirebaseToken(): String? {
    return userPreferences().getString(OLD_FIREBASE_TOKEN, null)
}

fun Context.userRegisteredWithFirebaseToken() {
    userPreferences().edit().putString(
        FIREBASE_TOKEN_REGISTERED,
        getAppId().plus("-").plus(getIdentifier()).plus("-").plus(getFirebaseToken())
    ).apply()
}

fun Context.isUserRegisteredWithFirebaseToken(): Boolean {
    return userPreferences().getString(FIREBASE_TOKEN_REGISTERED, "") == getAppId().plus("-")
        .plus(getIdentifier()).plus("-").plus(getFirebaseToken())
}

fun Context.notificationsDisableStatusNotified(boolean: Boolean) {
    userPreferences().edit().putBoolean(FIREBASE_NOTIFICATION_DISABLED, boolean).apply()
}

fun Context.isNotificationsDisableStatusNotified() : Boolean{
    return userPreferences().getBoolean(FIREBASE_NOTIFICATION_DISABLED, false)
}


fun Context.showDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    positiveButtonText: String,
    onPositive: (() -> Unit)? = null,
    negativeButtonText: String? = null,
    onNegative: (() -> Unit)? = null, cancelable: Boolean = true
) {
    val dialog = AlertDialog.Builder(this).apply {
        setIcon(R.mipmap.ic_launcher_round)
        setMessage(message)
            .setPositiveButton(
                positiveButtonText
            ) { _, _ ->
                onPositive?.invoke()
            }
        if (negativeButtonText != null)
            setNegativeButton(
                negativeButtonText
            ) { _, _ ->
                onNegative?.invoke()
            }
        setTitle(title)
        setCancelable(cancelable)
    }.show()
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
}


fun Context.showDialog(
    @StringRes title: Int,
    message: String,
    positiveButtonText: String,
    onPositive: (() -> Unit)? = null,
    negativeButtonText: String? = null,
    onNegative: (() -> Unit)? = null, cancelable: Boolean = true
) {
    val dialog = AlertDialog.Builder(this).apply {
        setIcon(R.mipmap.ic_launcher_round)
        setMessage(message)
            .setPositiveButton(
                positiveButtonText
            ) { _, _ ->
                onPositive?.invoke()
            }
        if (negativeButtonText != null)
            setNegativeButton(
                negativeButtonText
            ) { _, _ ->
                onNegative?.invoke()
            }
        setTitle(title)
        setCancelable(cancelable)
    }.show()
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
}
