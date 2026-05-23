package com.ohyooo.shared.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Common MVI store base used by all shared screens.
 *
 * UI observes [state] and sends user actions through [dispatch]. Subclasses only
 * implement [reduce], keeping state transitions in one place and out of Composable
 * rendering code.
 */
abstract class MviStore<State, Intent>(initialState: State) {
    private val storeJob = SupervisorJob()

    /**
     * Scope for store-owned background work such as delayed state updates.
     *
     * Use this only inside a store. UI code should not launch work here directly;
     * it should dispatch an intent and let the store decide what work is needed.
     */
    protected val storeScope = CoroutineScope(storeJob + Dispatchers.Default)

    private val _state = MutableStateFlow(initialState)

    /**
     * Read-only UI state stream observed by Compose with collectAsState().
     */
    val state: StateFlow<State> = _state.asStateFlow()

    /**
     * Entry point for UI events.
     *
     * Composables call this method with a screen-specific intent instead of
     * mutating local business state directly.
     */
    fun dispatch(intent: Intent) {
        reduce(intent)
    }

    /**
     * Handles one intent and updates state through [setState].
     */
    protected abstract fun reduce(intent: Intent)

    /**
     * Atomically transforms the current state.
     */
    protected fun setState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    /**
     * Releases store-owned coroutines when the Composable that created the store
     * leaves composition.
     */
    open fun close() {
        storeScope.cancel()
    }
}
