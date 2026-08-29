package com.zeta.anywhere.data.services.remote

import com.zeta.anywhere.core.config.AppConfig
import com.zeta.anywhere.data.services.CallService
import com.zeta.anywhere.domain.state.CallState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RemoteCallService(@Suppress("UNUSED_PARAMETER") private val config: AppConfig) : CallService {
    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    override val callState: StateFlow<CallState> = _callState.asStateFlow()

    override suspend fun startOutgoingCall() {
        _callState.value = CallState.Failed("Remote call setup is pending backend signaling endpoints.")
    }

    override suspend fun simulateIncomingCall() {
        // no-op in remote mode; this is for development only.
    }

    override suspend fun receiveIncomingCall(callId: String) {
        _callState.value = CallState.Incoming(callId = callId)
    }

    override suspend fun answerCall() {
        _callState.value = CallState.Failed("Remote call answering requires backend implementation.")
    }

    override suspend fun declineCall() {
        _callState.value = CallState.Idle
    }

    override suspend fun endCall() {
        _callState.value = CallState.Idle
    }
}
