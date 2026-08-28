package com.zeta.anywhere.data.services.remote

import com.zeta.anywhere.core.config.AppConfig
import com.zeta.anywhere.core.network.ApiClient
import com.zeta.anywhere.data.services.PairingService
import com.zeta.anywhere.domain.models.PairingOutcome

class RemotePairingService(private val config: AppConfig) : PairingService {
    override suspend fun pair(pairingCode: String, deviceName: String, deviceType: String): PairingOutcome {
        val payload = """
            {
              "pairing_code": "$pairingCode",
              "device_name": "$deviceName",
              "device_type": "$deviceType"
            }
        """.trimIndent()

        val result = ApiClient.postJson("${config.apiBaseUrl}/api/v1/pair", payload)
        if (result.isFailure) return PairingOutcome.NetworkFailure

        return PairingOutcome.UnknownFailure(
            "Remote pairing parser is pending backend contract finalization."
        )
    }
}
