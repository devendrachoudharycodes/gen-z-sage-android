package com.genzsage.core.local.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.genzsage.core.local.di.AuthDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @param:AuthDataStore
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveTokens(
        refreshToken: String
    ) {
        dataStore.edit { prefs ->
            prefs[TokenKeys.REFRESH_TOKEN] = refreshToken
        }
    }



    val refreshToken: Flow<String?> =
        dataStore.data.map { prefs ->
            prefs[TokenKeys.REFRESH_TOKEN]
        }

    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(TokenKeys.REFRESH_TOKEN)
        }
    }
}