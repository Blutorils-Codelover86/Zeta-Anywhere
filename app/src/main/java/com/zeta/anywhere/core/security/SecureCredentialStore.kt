package com.zeta.anywhere.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.zeta.anywhere.domain.models.AuthCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

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
        val payload = JSONObject()
            .put("zeta_id", credentials.zetaId)
            .put("device_id", credentials.deviceId)
            .put("access_token", credentials.accessToken)
            .put("refresh_token", credentials.refreshToken)
            .put("expires_at", credentials.expiresAtEpochSeconds)
            .put("endpoint", credentials.endpoint)
            .toString()

        prefs.edit().putString(KEY_DATA, payload).apply()
    }

    override suspend fun load(): AuthCredentials? = withContext(Dispatchers.IO) {
        val payload = prefs.getString(KEY_DATA, null) ?: return@withContext null
        runCatching {
            val obj = JSONObject(payload)
            AuthCredentials(
                zetaId = obj.getString("zeta_id"),
                deviceId = obj.getString("device_id"),
                accessToken = obj.getString("access_token"),
                refreshToken = obj.optString("refresh_token", null),
                expiresAtEpochSeconds = obj.getLong("expires_at"),
                endpoint = obj.getString("endpoint")
            )
        }.getOrNull()
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit().remove(KEY_DATA).apply()
    }
}
