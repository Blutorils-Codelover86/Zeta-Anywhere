package com.zeta.anywhere.domain.state

sealed interface CallState {
    data object Idle : CallState

    data class Incoming(
        val callId: String,
        val title: String = "Zeta is calling",
        val body: String = "Something needs your attention."
    ) : CallState

    data class Connecting(val callId: String) : CallState

    data class Connected(
        val callId: String,
        val assistantState: AssistantActivityState = AssistantActivityState.Listening
    ) : CallState

    data object Declined : CallState
    data object Ended : CallState
    data object Timeout : CallState
    data object ConnectionLost : CallState
    data class Failed(val message: String) : CallState
}

enum class AssistantActivityState {
    Listening,
    Thinking,
    Speaking,
    Working
}
