package com.zeta.anywhere.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.zeta.anywhere.domain.models.AuthCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val KEY_STORE = "zeta_anywhere_secure_store"
private const val KEY_DATA = "auth_credentials"

class SecureCredentialStore(context: Context) : CredentialStore {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        KEY_STORE,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun save(credentials: AuthCredentials) = withContext(Dispatchers.IO) {
        val payload = listOf(
            credentials.zetaId,
            credentials.deviceId,
            credentials.accessToken,
            credentials.refreshToken.orEmpty(),
            credentials.expiresAtEpochSeconds.toString(),
            credentials.endpoint
        ).joinToString("||")

        prefs.edit().putString(KEY_DATA, payload).apply()
    }

    override suspend fun load(): AuthCredentials? = withContext(Dispatchers.IO) {
        val payload = prefs.getString(KEY_DATA, null) ?: return@withContext null
        val parts = payload.split("||")
        if (parts.size < 6) return@withContext null

        AuthCredentials(
            zetaId = parts[0],
            deviceId = parts[1],
            accessToken = parts[2],
            refreshToken = parts[3].ifBlank { null },
            expiresAtEpochSeconds = parts[4].toLongOrNull() ?: return@withContext null,
            endpoint = parts[5]
        )
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit().remove(KEY_DATA).apply()
    }
}
