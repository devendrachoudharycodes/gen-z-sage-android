package com.genzsage.core.local.datastore.auth

import androidx.datastore.preferences.core.stringPreferencesKey

object TokenKeys {
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}