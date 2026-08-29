package com.zeta.anywhere.core.network

import com.zeta.anywhere.domain.models.IncomingCallNotification

object IncomingCallPayloadParser {
    fun parse(data: Map<String, String>): IncomingCallNotification? {
        if (data["type"] != "incoming_call") return null
        val callId = data["call_id"]?.trim().orEmpty()
        if (callId.isBlank()) return null
        return IncomingCallNotification(callId = callId)
    }
}
