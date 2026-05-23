package com.ohyooo.shared.viewmodel

import com.ohyooo.shared.model.Route
import com.ohyooo.shared.mvi.MviStore

/**
 * State for the application shell.
 *
 * Currently it only stores the selected drawer route. Add cross-screen state here
 * when the app shell needs to coordinate multiple screens.
 */
data class AppUiState(
    val route: Route = Route.SINGLE,
)

/**
 * User or system actions handled by [AppStore].
 */
sealed interface AppIntent {
    /**
     * Selects a drawer destination and causes the navigation UI to render the
     * matching page.
     */
    data class SelectRoute(val route: Route) : AppIntent
}

/**
 * MVI store for navigation-level state.
 *
 * Compose screens call [dispatch] with [AppIntent.SelectRoute] instead of storing
 * route state in the UI tree.
 */
class AppStore : MviStore<AppUiState, AppIntent>(AppUiState()) {
    /**
     * Reduces app-level intents into a new [AppUiState].
     */
    override fun reduce(intent: AppIntent) {
        when (intent) {
            is AppIntent.SelectRoute -> setState { it.copy(route = intent.route) }
        }
    }
}
