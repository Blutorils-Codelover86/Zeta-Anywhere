package com.zeta.anywhere

import com.zeta.anywhere.data.services.mock.MockConnectionService
import com.zeta.anywhere.domain.models.AuthCredentials
import com.zeta.anywhere.domain.state.ConnectionStatus
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MockConnectionServiceTest {

    private val service = MockConnectionService()

    @Test
    fun `successful connection reaches online`() = runTest {
        service.connect(dummyCredentials())
        assertEquals(ConnectionStatus.ZETA_ONLINE, service.connectionState.value)
    }

    @Test
    fun `offline simulation works`() = runTest {
        service.setZetaOnline(false)
        assertEquals(ConnectionStatus.ZETA_OFFLINE, service.connectionState.value)
    }

    @Test
    fun `disconnect resets state`() = runTest {
        service.connect(dummyCredentials())
        service.disconnect()
        assertEquals(ConnectionStatus.DISCONNECTED, service.connectionState.value)
    }

    private fun dummyCredentials() = AuthCredentials(
        zetaId = "zeta",
        deviceId = "device",
        accessToken = "token",
        expiresAtEpochSeconds = 999999999,
        endpoint = "https://mock"
    )
}
