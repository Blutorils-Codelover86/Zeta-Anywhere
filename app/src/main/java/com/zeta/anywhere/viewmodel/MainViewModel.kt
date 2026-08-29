package com.zeta.anywhere.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeta.anywhere.core.security.CredentialStore
import com.zeta.anywhere.data.services.CallService
import com.zeta.anywhere.data.services.ConnectionService
import com.zeta.anywhere.data.services.PairingService
import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.models.PairingOutcome
import com.zeta.anywhere.domain.state.AppSessionState
import com.zeta.anywhere.domain.state.CallState
import com.zeta.anywhere.domain.state.ConnectionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val pairingService: PairingService,
    private val connectionService: ConnectionService,
    private val callService: CallService,
    private val credentialStore: CredentialStore,
    val mockMode: Boolean
) : ViewModel() {

    private val pairingFlow = MutableStateFlow(false)
    private val pairedZetaName = MutableStateFlow("Your Desktop")

    val pairingInProgress = MutableStateFlow(false)
    val lastPairingMessage = MutableStateFlow<String?>(null)

    val appState: StateFlow<AppSessionState> = combine(
        pairingFlow,
        connectionService.connectionState,
        pairedZetaName
    ) { isPairing, connectionState, zetaName ->
        when {
            isPairing -> AppSessionState.Pairing
            connectionState == ConnectionStatus.DISCONNECTED -> AppSessionState.Unpaired
            else -> AppSessionState.Paired(
                zetaName = zetaName,
                connectionStatus = connectionState
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSessionState.Starting)

    val callState: StateFlow<CallState> = callService.callState

    init {
        viewModelScope.launch {
            val credentials = credentialStore.load()
            if (credentials != null) {
                connectionService.connect(credentials)
            }
        }
    }

    fun startPairing() {
        pairingFlow.value = true
        lastPairingMessage.value = null
    }

    fun cancelPairing() {
        pairingFlow.value = false
        lastPairingMessage.value = null
    }

    fun submitPairingCode(code: String) {
        viewModelScope.launch {
            pairingInProgress.value = true
            when (val result = pairingService.pair(code.trim().uppercase(), "Android Phone")) {
                is PairingOutcome.Success -> {
                    onPairingSuccess(result.credentials, result.zetaName)
                }

                PairingOutcome.InvalidCode -> {
                    lastPairingMessage.value = "That pairing code isn't valid. Try generating a new one from Zeta."
                }

                PairingOutcome.ExpiredCode -> {
                    lastPairingMessage.value = "This pairing code has expired. Generate a new one and try again."
                }

                PairingOutcome.AlreadyUsedCode -> {
                    lastPairingMessage.value = "This pairing code has already been used. Generate a new one from Zeta."
                }

                PairingOutcome.NetworkFailure -> {
                    lastPairingMessage.value = "Connection lost. Trying again may help."
                }

                is PairingOutcome.UnknownFailure -> {
                    lastPairingMessage.value = result.message
                }
            }
            pairingInProgress.value = false
        }
    }

    fun disconnectZeta() {
        viewModelScope.launch {
            callService.endCall()
            connectionService.disconnect()
            credentialStore.clear()
            pairedZetaName.value = "Your Desktop"
            pairingFlow.value = false
        }
    }

    fun startOutgoingCall() {
        viewModelScope.launch { callService.startOutgoingCall() }
    }

    fun simulateIncomingCall() {
        viewModelScope.launch { callService.simulateIncomingCall() }
    }

    fun answerIncomingCall() {
        viewModelScope.launch { callService.answerCall() }
    }

    fun declineIncomingCall() {
        viewModelScope.launch { callService.declineCall() }
    }

    fun endCall() {
        viewModelScope.launch { callService.endCall() }
    }

    fun setZetaOnline(online: Boolean) {
        viewModelScope.launch { connectionService.setZetaOnline(online) }
    }

    private suspend fun onPairingSuccess(credentials: AuthCredentials, zetaName: String) {
        credentialStore.save(credentials)
        pairedZetaName.value = zetaName
        pairingFlow.value = false
        lastPairingMessage.value = "✓ Connected. Your Zeta is ready."
        connectionService.connect(credentials)
    }
}
