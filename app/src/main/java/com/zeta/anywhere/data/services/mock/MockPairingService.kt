package com.zeta.anywhere.data.services.mock

import com.zeta.anywhere.data.services.PairingService
import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.models.PairingOutcome
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class MockPairingService(private val pairingTtlSeconds: Int) : PairingService {
    private val consumedCodes = mutableSetOf<String>()

    override suspend fun pair(pairingCode: String, deviceName: String, deviceType: String): PairingOutcome {
        delay(1200)

        if (!PAIRING_REGEX.matches(pairingCode)) {
            return PairingOutcome.InvalidCode
        }
        if ("EXPIRED" in pairingCode) {
            return PairingOutcome.ExpiredCode
        }
        if ("NETFAIL" in pairingCode) {
            return PairingOutcome.NetworkFailure
        }
        if (pairingCode in consumedCodes || "USED" in pairingCode) {
            return PairingOutcome.AlreadyUsedCode
        }

        consumedCodes += pairingCode
        val now = System.currentTimeMillis() / 1000

        return PairingOutcome.Success(
            credentials = AuthCredentials(
                zetaId = "zeta_abc123",
                deviceId = "android_device_${deviceName.hashCode()}",
                accessToken = "mock_access_${pairingCode.takeLast(4)}",
                refreshToken = "mock_refresh_${pairingCode.takeLast(4)}",
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
