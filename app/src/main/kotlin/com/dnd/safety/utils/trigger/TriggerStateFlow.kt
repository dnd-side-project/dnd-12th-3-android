package com.dnd.safety.utils.trigger

import kotlinx.coroutines.flow.StateFlow

public interface TriggerStateFlow<out T> : StateFlow<T>, Trigger {

    override fun restart()
}