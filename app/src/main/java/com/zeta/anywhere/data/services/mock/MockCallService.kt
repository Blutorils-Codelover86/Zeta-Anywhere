package com.zeta.anywhere.data.services.mock

import com.zeta.anywhere.data.services.CallService
import com.zeta.anywhere.domain.state.AssistantActivityState
import com.zeta.anywhere.domain.state.CallState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class MockCallService : CallService {
    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    override val callState: StateFlow<CallState> = _callState.asStateFlow()

    override suspend fun startOutgoingCall() {
        val callId = newCallId()
        _callState.value = CallState.Connecting(callId)
        delay(1000)
        _callState.value = CallState.Connected(callId, AssistantActivityState.Listening)
    }

    override suspend fun simulateIncomingCall() {
        _callState.value = CallState.Incoming(newCallId())
    }

    override suspend fun answerCall() {
        val current = _callState.value
        if (current is CallState.Incoming) {
            _callState.value = CallState.Connecting(current.callId)
            delay(800)
            _callState.value = CallState.Connected(
                callId = current.callId,
                assistantState = AssistantActivityState.entries.random(Random(System.currentTimeMillis()))
            )
        }
    }

    override suspend fun declineCall() {
        _callState.value = CallState.Declined
        delay(300)
        _callState.value = CallState.Idle
    }

    override suspend fun endCall() {
        _callState.value = CallState.Ended
        delay(400)
        _callState.value = CallState.Idle
    }

    private fun newCallId(): String = "call_${System.currentTimeMillis()}"
}
