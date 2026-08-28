package com.zeta.anywhere.domain.models

sealed interface PairingOutcome {
    data class Success(
        val credentials: AuthCredentials,
        val zetaName: String
    ) : PairingOutcome

    data object InvalidCode : PairingOutcome
    data object ExpiredCode : PairingOutcome
    data object AlreadyUsedCode : PairingOutcome
    data object NetworkFailure : PairingOutcome
    data class UnknownFailure(val message: String) : PairingOutcome
}
