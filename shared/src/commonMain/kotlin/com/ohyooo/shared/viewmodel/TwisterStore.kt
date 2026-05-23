package com.ohyooo.shared.viewmodel

import com.ohyooo.shared.mvi.MviStore

/**
 * State for the tongue-twister page.
 *
 * [resetRequest] is a monotonically increasing signal. The UI watches it with
 * LaunchedEffect and scrolls every pager back to the first page when it changes.
 */
data class TwisterUiState(
    val resetRequest: Long = 0L,
)

/**
 * User actions supported by [TwisterStore].
 */
sealed interface TwisterIntent {
    /**
     * Requests all tongue-twister pagers to return to their first page.
     */
    data object Reset : TwisterIntent
}

/**
 * MVI store for the tongue-twister page.
 */
class TwisterStore : MviStore<TwisterUiState, TwisterIntent>(TwisterUiState()) {
    /**
     * Converts page actions into state changes consumed by the Compose UI.
     */
    override fun reduce(intent: TwisterIntent) {
        when (intent) {
            TwisterIntent.Reset -> setState { it.copy(resetRequest = it.resetRequest + 1) }
        }
    }
}
