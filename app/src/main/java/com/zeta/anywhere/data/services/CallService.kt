package com.zeta.anywhere.data.services

import com.zeta.anywhere.domain.state.CallState
import kotlinx.coroutines.flow.StateFlow

interface CallService {
    val callState: StateFlow<CallState>

    suspend fun startOutgoingCall()
    suspend fun simulateIncomingCall()
    suspend fun receiveIncomingCall(callId: String)
    suspend fun answerCall()
    suspend fun declineCall()
    suspend fun endCall()
}
