package com.zeta.anywhere.domain.state

enum class ConnectionStatus {
    CONNECTING,
    CONNECTED,
    ZETA_ONLINE,
    ZETA_OFFLINE,
    AUTHENTICATION_FAILED,
    NETWORK_ERROR,
    DISCONNECTED
}
