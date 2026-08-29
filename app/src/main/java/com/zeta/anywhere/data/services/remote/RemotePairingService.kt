package com.zeta.anywhere.data.services.remote

import com.zeta.anywhere.core.config.AppConfig
import com.zeta.anywhere.core.network.ApiClient
import com.zeta.anywhere.data.services.PairingService
import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.models.PairingOutcome
import org.json.JSONObject

class RemotePairingService(private val config: AppConfig) : PairingService {
    override suspend fun pair(pairingCode: String, deviceName: String, deviceType: String): PairingOutcome {
        val payload = JSONObject()
            .put("pairing_code", pairingCode)
            .put("device_name", deviceName)
            .put("device_type", deviceType)
            .toString()

        val result = ApiClient.postJson("${config.apiBaseUrl}/api/v1/pair", payload)
        if (result.isFailure) return PairingOutcome.NetworkFailure

        return parsePairingResponse(result.getOrNull().orEmpty())
    }

    internal fun parsePairingResponse(body: String): PairingOutcome {
        return runCatching {
            val json = JSONObject(body)
            val success = json.optBoolean("success", false)
            if (!success) {
                when (json.optString("error")) {
                    "invalid_pairing_code" -> PairingOutcome.InvalidCode
                    "pairing_code_expired" -> PairingOutcome.ExpiredCode
                    "pairing_code_used" -> PairingOutcome.AlreadyUsedCode
                    "network_error" -> PairingOutcome.NetworkFailure
                    else -> PairingOutcome.UnknownFailure("Pairing failed.")
                }
            } else {
                val expiresIn = json.optLong("expires_in", 3600)
                val expiresAt = System.currentTimeMillis() / 1000 + expiresIn
                PairingOutcome.Success(
                    credentials = AuthCredentials(
                        zetaId = json.getString("zeta_id"),
                        deviceId = json.getString("device_id"),
                        accessToken = json.getString("access_token"),
                        refreshToken = json.optString("refresh_token", null),
                        expiresAtEpochSeconds = expiresAt,
                        endpoint = json.getString("endpoint")
                    ),
                    zetaName = json.optString("zeta_name", "Your Desktop")
                )
            }
        }.getOrElse {
            PairingOutcome.UnknownFailure("Pairing response format is invalid.")
        }
    }
}
