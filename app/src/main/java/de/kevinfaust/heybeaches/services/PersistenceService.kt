package de.kevinfaust.heybeaches.services

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PersistenceService(context: Context) {
    private val PREFS_FILENAME = context.packageName + ".prefs"
    private val JWT_TOKEN = "jwt_token"
    private val USER_NAME = "user_name"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    var jwtToken: String
        get() = prefs.getString(JWT_TOKEN, "")
        set(value) = prefs.edit().putString(JWT_TOKEN, value).apply()

    var userName: String
        get() = prefs.getString(USER_NAME, "")
        set(value) = prefs.edit().putString(USER_NAME, value).apply()
}
