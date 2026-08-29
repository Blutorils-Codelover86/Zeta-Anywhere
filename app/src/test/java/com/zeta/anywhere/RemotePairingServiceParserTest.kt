package com.zeta.anywhere

import com.zeta.anywhere.core.config.AppConfig
import com.zeta.anywhere.data.services.remote.RemotePairingService
import com.zeta.anywhere.domain.models.PairingOutcome
import org.junit.Assert.assertTrue
import org.junit.Test

class RemotePairingServiceParserTest {

    private val service = RemotePairingService(
        AppConfig(
            apiBaseUrl = "https://example.invalid",
            mockMode = false,
            pairingTtlSeconds = 600
        )
    )

    @Test
    fun `parses success response`() {
        val response = """
            {
              "success": true,
              "zeta_id": "zeta_1",
              "device_id": "device_1",
              "access_token": "token_1",
              "refresh_token": "refresh_1",
              "expires_in": 3600,
              "endpoint": "https://zeta.endpoint"
            }
        """.trimIndent()

        val outcome = service.parsePairingResponse(response)
        assertTrue(outcome is PairingOutcome.Success)
    }

    @Test
    fun `parses invalid code error`() {
        val response = """{ "success": false, "error": "invalid_pairing_code" }"""
        val outcome = service.parsePairingResponse(response)
        assertTrue(outcome is PairingOutcome.InvalidCode)
    }

    @Test
    fun `parses expired code error`() {
        val response = """{ "success": false, "error": "pairing_code_expired" }"""
        val outcome = service.parsePairingResponse(response)
        assertTrue(outcome is PairingOutcome.ExpiredCode)
    }

    @Test
    fun `handles malformed response`() {
        val outcome = service.parsePairingResponse("not-json")
        assertTrue(outcome is PairingOutcome.UnknownFailure)
    }
}
