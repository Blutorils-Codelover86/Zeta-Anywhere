package com.zeta.anywhere.core.security

import com.zeta.anywhere.domain.models.AuthCredentials

interface CredentialStore {
    suspend fun save(credentials: AuthCredentials)
    suspend fun load(): AuthCredentials?
    suspend fun clear()
}
