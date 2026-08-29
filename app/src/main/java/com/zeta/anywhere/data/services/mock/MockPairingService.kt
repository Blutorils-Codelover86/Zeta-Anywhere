package com.zeta.anywhere.data.services.mock

import com.zeta.anywhere.data.services.PairingService
import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.models.PairingOutcome
import kotlinx.coroutines.delay

class MockPairingService(private val pairingTtlSeconds: Int) : PairingService {
    private val consumedCodes = mutableSetOf<String>()

    override suspend fun pair(pairingCode: String, deviceName: String, deviceType: String): PairingOutcome {
        delay(1200)

        val normalized = pairingCode.trim().uppercase()
        if (!PAIRING_REGEX.matches(normalized)) {
            return PairingOutcome.InvalidCode
        }

        val scenario = normalized.substringAfter("ZETA-").substringBefore("-")
        if (scenario == "EXPR") return PairingOutcome.ExpiredCode
        if (scenario == "NETF") return PairingOutcome.NetworkFailure
        if (scenario == "USED") return PairingOutcome.AlreadyUsedCode

        if (normalized in consumedCodes) {
            return PairingOutcome.AlreadyUsedCode
        }

        consumedCodes += normalized
        val now = System.currentTimeMillis() / 1000

        return PairingOutcome.Success(
            credentials = AuthCredentials(
                zetaId = "zeta_abc123",
                deviceId = "android_device_${deviceName.hashCode()}",
                accessToken = "mock_access_${normalized.takeLast(4)}",
                refreshToken = "mock_refresh_${normalized.takeLast(4)}",
                expiresAtEpochSeconds = now + pairingTtlSeconds,
                endpoint = "https://mock.zeta.anywhere"
            ),
            zetaName = "Your Desktop"
        )
    }

    companion object {
        private val PAIRING_REGEX = Regex("^ZETA-[A-Z0-9]{4}-[A-Z0-9]{4}$")
    }
}
