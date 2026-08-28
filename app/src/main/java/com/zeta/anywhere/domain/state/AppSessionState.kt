package com.zeta.anywhere.domain.state

sealed interface AppSessionState {
    data object Starting : AppSessionState
    data object Unpaired : AppSessionState
    data object Pairing : AppSessionState
    data class Paired(
        val zetaName: String,
        val connectionStatus: ConnectionStatus
    ) : AppSessionState
}
