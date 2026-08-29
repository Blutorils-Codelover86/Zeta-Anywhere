package com.zeta.anywhere

import com.zeta.anywhere.core.network.IncomingCallPayloadParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class IncomingCallPayloadParserTest {

    @Test
    fun `parses incoming call payload`() {
        val payload = mapOf(
            "type" to "incoming_call",
            "call_id" to "call_123"
        )

        val result = IncomingCallPayloadParser.parse(payload)

        assertEquals("call_123", result?.callId)
    }

    @Test
    fun `rejects non incoming payload`() {
        val payload = mapOf(
            "type" to "presence",
            "call_id" to "call_123"
        )

        val result = IncomingCallPayloadParser.parse(payload)

        assertNull(result)
    }

    @Test
    fun `rejects missing call id`() {
        val payload = mapOf("type" to "incoming_call")

        val result = IncomingCallPayloadParser.parse(payload)

        assertNull(result)
    }
}
