package com.zeta.anywhere.data.services

import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.state.ConnectionStatus
import kotlinx.coroutines.flow.StateFlow

interface ConnectionService {
    val connectionState: StateFlow<ConnectionStatus>

    suspend fun connect(credentials: AuthCredentials)
    suspend fun disconnect()
    suspend fun refreshSession()
    suspend fun setZetaOnline(online: Boolean)
}
