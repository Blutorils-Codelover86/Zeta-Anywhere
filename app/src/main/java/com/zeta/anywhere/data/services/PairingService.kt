package com.zeta.anywhere.data.services

import com.zeta.anywhere.domain.models.PairingOutcome

interface PairingService {
    suspend fun pair(
        pairingCode: String,
        deviceName: String,
        deviceType: String = "android"
    ): PairingOutcome
}
