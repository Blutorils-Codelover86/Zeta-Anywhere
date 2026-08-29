package com.zeta.anywhere.core.network

import com.zeta.anywhere.data.services.CallService

class IncomingCallDispatcher(
    private val callService: CallService
) {
    suspend fun dispatch(payload: Map<String, String>): Boolean {
        val notification = IncomingCallPayloadParser.parse(payload) ?: return false
        callService.receiveIncomingCall(notification.callId)
        return true
    }
}
