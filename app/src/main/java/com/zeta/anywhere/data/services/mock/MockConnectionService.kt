package com.zeta.anywhere.data.services.mock

import com.zeta.anywhere.data.services.ConnectionService
import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.state.ConnectionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockConnectionService : ConnectionService {
    private val _connectionState = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    override val connectionState: StateFlow<ConnectionStatus> = _connectionState.asStateFlow()

    override suspend fun connect(credentials: AuthCredentials) {
        _connectionState.value = ConnectionStatus.CONNECTING
        delay(900)
        _connectionState.value = ConnectionStatus.CONNECTED
        delay(300)
        _connectionState.value = ConnectionStatus.ZETA_ONLINE
    }

    override suspend fun disconnect() {
        _connectionState.value = ConnectionStatus.DISCONNECTED
    }

    override suspend fun refreshSession() {
        if (_connectionState.value == ConnectionStatus.ZETA_OFFLINE) return
        _connectionState.value = ConnectionStatus.CONNECTING
        delay(400)
        _connectionState.value = ConnectionStatus.ZETA_ONLINE
    }

    override suspend fun setZetaOnline(online: Boolean) {
        _connectionState.value = if (online) ConnectionStatus.ZETA_ONLINE else ConnectionStatus.ZETA_OFFLINE
    }
}
