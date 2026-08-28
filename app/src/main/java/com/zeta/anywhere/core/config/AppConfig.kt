package com.zeta.anywhere.core.config

import com.zeta.anywhere.BuildConfig

data class AppConfig(
    val apiBaseUrl: String,
    val mockMode: Boolean,
    val pairingTtlSeconds: Int
) {
    companion object {
        fun fromBuildConfig(): AppConfig = AppConfig(
            apiBaseUrl = BuildConfig.ZETA_API_BASE_URL,
            mockMode = BuildConfig.ZETA_MOCK_MODE,
            pairingTtlSeconds = BuildConfig.ZETA_PAIRING_TTL_SECONDS
        )
    }
}
