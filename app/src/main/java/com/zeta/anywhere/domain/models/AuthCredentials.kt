package com.zeta.anywhere.domain.models

data class AuthCredentials(
    val zetaId: String,
    val deviceId: String,
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresAtEpochSeconds: Long,
    val endpoint: String
)
