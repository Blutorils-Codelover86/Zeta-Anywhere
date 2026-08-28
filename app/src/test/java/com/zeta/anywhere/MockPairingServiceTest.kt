package com.zeta.anywhere

import com.zeta.anywhere.data.services.mock.MockPairingService
import com.zeta.anywhere.domain.models.PairingOutcome
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MockPairingServiceTest {

    private val service = MockPairingService(pairingTtlSeconds = 600)

    @Test
    fun `valid code pairs successfully`() = runTest {
        val outcome = service.pair("ZETA-7K9P-X4M2", "Android Phone")
        assertTrue(outcome is PairingOutcome.Success)
    }

    @Test
    fun `invalid code fails`() = runTest {
        val outcome = service.pair("INVALID", "Android Phone")
        assertTrue(outcome is PairingOutcome.InvalidCode)
    }

    @Test
    fun `expired code fails`() = runTest {
        val outcome = service.pair("ZETA-EXPIRED-0001", "Android Phone")
        assertTrue(outcome is PairingOutcome.InvalidCode || outcome is PairingOutcome.ExpiredCode)
    }

    @Test
    fun `used code fails second time`() = runTest {
        service.pair("ZETA-AAAA-BBBB", "Android Phone")
        val second = service.pair("ZETA-AAAA-BBBB", "Android Phone")
        assertTrue(second is PairingOutcome.AlreadyUsedCode)
    }

    @Test
    fun `network fail code fails`() = runTest {
        val outcome = service.pair("ZETA-NETF-AIL0", "Android Phone")
        assertTrue(outcome is PairingOutcome.NetworkFailure || outcome is PairingOutcome.InvalidCode)
    }
}
