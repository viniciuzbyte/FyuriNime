package com.fyurinime.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "fyurinime_session")

@Singleton
class SessionManager @Inject constructor(
    private val context: Context
) {
    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val HAS_ONBOARDED = booleanPreferencesKey("has_onboarded")
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val SOURCE = stringPreferencesKey("source")
        val BIO = stringPreferencesKey("bio")
        val AVATAR_PATH = stringPreferencesKey("avatar_path")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[Keys.IS_LOGGED_IN] ?: false }
    val hasOnboarded: Flow<Boolean> = context.dataStore.data.map { it[Keys.HAS_ONBOARDED] ?: false }
    val username: Flow<String?> = context.dataStore.data.map { it[Keys.USERNAME] }
    val source: Flow<String?> = context.dataStore.data.map { it[Keys.SOURCE] }
    val bio: Flow<String?> = context.dataStore.data.map { it[Keys.BIO] }
    val avatarPath: Flow<String?> = context.dataStore.data.map { it[Keys.AVATAR_PATH] }

    suspend fun login(username: String, email: String) {
        context.dataStore.edit {
            it[Keys.IS_LOGGED_IN] = true
            it[Keys.USERNAME] = username
            it[Keys.EMAIL] = email
        }
    }

    suspend fun completeOnboarding(source: String) {
        context.dataStore.edit {
            it[Keys.HAS_ONBOARDED] = true
            it[Keys.SOURCE] = source
        }
    }

    suspend fun updateProfile(username: String, bio: String) {
        context.dataStore.edit {
            it[Keys.USERNAME] = username
            it[Keys.BIO] = bio
        }
    }

    suspend fun setAvatarPath(path: String?) {
        context.dataStore.edit {
            if (path == null) {
                it.remove(Keys.AVATAR_PATH)
            } else {
                it[Keys.AVATAR_PATH] = path
            }
        }
    }

    suspend fun logout() {
        context.dataStore.edit {
            it[Keys.IS_LOGGED_IN] = false
        }
    }
}
