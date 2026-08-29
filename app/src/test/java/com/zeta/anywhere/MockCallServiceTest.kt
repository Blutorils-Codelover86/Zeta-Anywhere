package com.zeta.anywhere

import com.zeta.anywhere.data.services.mock.MockCallService
import com.zeta.anywhere.domain.state.CallState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MockCallServiceTest {

    private val service = MockCallService()

    @Test
    fun `incoming call can be answered`() = runTest {
        service.simulateIncomingCall()
        assertTrue(service.callState.value is CallState.Incoming)
        service.answerCall()
        assertTrue(service.callState.value is CallState.Connected)
    }

    @Test
    fun `incoming call can be declined`() = runTest {
        service.simulateIncomingCall()
        service.declineCall()
        assertTrue(service.callState.value is CallState.Idle)
    }

    @Test
    fun `call timeout scenario`() = runTest {
        service.simulateTimeout()
        assertTrue(service.callState.value is CallState.Idle)
    }

    @Test
    fun `call failure scenario`() = runTest {
        service.simulateFailure()
        assertTrue(service.callState.value is CallState.Failed)
    }

    @Test
    fun `outgoing call can end`() = runTest {
        service.startOutgoingCall()
        assertTrue(service.callState.value is CallState.Connected)
        service.endCall()
        assertTrue(service.callState.value is CallState.Idle)
    }
}
