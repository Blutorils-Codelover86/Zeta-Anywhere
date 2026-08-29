package com.zeta.anywhere.data.services.remote

import com.zeta.anywhere.core.config.AppConfig
import com.zeta.anywhere.data.services.ConnectionService
import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.state.ConnectionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RemoteConnectionService(@Suppress("UNUSED_PARAMETER") private val config: AppConfig) : ConnectionService {
    private val _connectionState = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    override val connectionState: StateFlow<ConnectionStatus> = _connectionState.asStateFlow()

    override suspend fun connect(credentials: AuthCredentials) {
        _connectionState.value = ConnectionStatus.CONNECTING
        _connectionState.value = ConnectionStatus.NETWORK_ERROR
    }

    override suspend fun disconnect() {
        _connectionState.value = ConnectionStatus.DISCONNECTED
    }

    override suspend fun refreshSession() {
        _connectionState.value = ConnectionStatus.NETWORK_ERROR
    }

    override suspend fun setZetaOnline(online: Boolean) {
        // no-op in remote mode; server is source of truth.
    }
}
