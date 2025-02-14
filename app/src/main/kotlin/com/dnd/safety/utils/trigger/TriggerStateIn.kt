package com.dnd.safety.utils.trigger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.triggerStateIn(
    scope: CoroutineScope,
    started: SharingStarted,
    initialValue: T,
): TriggerStateFlow<T> {
    val sharingRestartable = SharingRestartableImpl(started)
    val stateFlow = stateIn(scope, sharingRestartable, initialValue)
    return object : TriggerStateFlow<T>, StateFlow<T> by stateFlow {
        override fun restart() = sharingRestartable.restart()
    }
}

private data class SharingRestartableImpl(
    private val sharingStarted: SharingStarted,
) : SharingStarted, Trigger {

    private val restartFlow = MutableSharedFlow<SharingCommand>(extraBufferCapacity = 2)

    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> {
        return merge(restartFlow, sharingStarted.command(subscriptionCount))
    }

    override fun restart() {
        restartFlow.tryEmit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE)
        restartFlow.tryEmit(SharingCommand.START)
    }
}