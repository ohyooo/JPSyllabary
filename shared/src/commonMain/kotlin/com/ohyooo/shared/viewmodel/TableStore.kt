package com.ohyooo.shared.viewmodel

import com.ohyooo.shared.model.normalSequence as orderedNormalSequence
import com.ohyooo.shared.model.sonantSequence as orderedSonantSequence
import com.ohyooo.shared.mvi.MviStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Identifies which romaji list the romaji tab is currently showing.
 */
enum class RomajiSource {
    NORMAL,
    SONANT,
}

/**
 * Stable key for a visible table cell.
 *
 * It lets the store reveal a single cell temporarily without holding UI-local
 * mutable state in each grid item.
 */
data class TableCellKey(
    val page: Int,
    val index: Int,
    val source: RomajiSource,
)

/**
 * State rendered by the kana table page.
 *
 * The table page keeps ordering, active romaji source, temporary revealed cells,
 * and floating-action-button state here so UI rendering stays deterministic.
 */
data class TableUiState(
    val selectedPage: Int = 0,
    val romajiSource: RomajiSource = RomajiSource.NORMAL,
    val normalSequence: List<Int> = orderedNormalSequence.toList(),
    val sonantSequence: List<Int> = orderedSonantSequence.toList(),
    val revealedCells: Set<TableCellKey> = emptySet(),
    val isFabExpanded: Boolean = false,
    val fabOffsetX: Float = 0F,
    val fabOffsetY: Float = 0F,
)

/**
 * User and UI actions supported by [TableStore].
 */
sealed interface TableIntent {
    /**
     * Records the current pager page and updates the romaji source when needed.
     */
    data class PageChanged(val page: Int) : TableIntent

    /**
     * Temporarily reveals the hint for a single table cell.
     */
    data class RevealCell(val key: TableCellKey) : TableIntent

    /**
     * Expands or collapses the multi-action floating button.
     */
    data class SetFabExpanded(val expanded: Boolean) : TableIntent

    /**
     * Moves the floating action button by the drag delta emitted by Compose.
     */
    data class DragFab(val deltaX: Float = 0F, val deltaY: Float = 0F) : TableIntent

    /**
     * Randomizes the normal and sonant table ordering.
     */
    data object Shuffle : TableIntent

    /**
     * Restores the normal and sonant table ordering.
     */
    data object Order : TableIntent
}

/**
 * MVI store for the kana table page.
 *
 * All table ordering and transient reveal behavior belongs here. Composables
 * observe [state] and dispatch [TableIntent] values.
 */
class TableStore : MviStore<TableUiState, TableIntent>(TableUiState()) {
    /**
     * Converts table intents into a new [TableUiState].
     */
    override fun reduce(intent: TableIntent) {
        when (intent) {
            is TableIntent.PageChanged -> setState { state ->
                state.copy(
                    selectedPage = intent.page,
                    romajiSource = when (intent.page) {
                        1 -> RomajiSource.NORMAL
                        3 -> RomajiSource.SONANT
                        else -> state.romajiSource
                    },
                )
            }

            is TableIntent.RevealCell -> revealCell(intent.key)
            is TableIntent.SetFabExpanded -> setState { it.copy(isFabExpanded = intent.expanded) }
            is TableIntent.DragFab -> setState {
                it.copy(
                    fabOffsetX = it.fabOffsetX + intent.deltaX,
                    fabOffsetY = it.fabOffsetY + intent.deltaY,
                )
            }

            TableIntent.Shuffle -> setState {
                it.copy(
                    normalSequence = orderedNormalSequence.toList().shuffled(),
                    sonantSequence = orderedSonantSequence.toList().shuffled(),
                    isFabExpanded = false,
                )
            }

            TableIntent.Order -> setState {
                it.copy(
                    normalSequence = orderedNormalSequence.toList(),
                    sonantSequence = orderedSonantSequence.toList(),
                    isFabExpanded = false,
                )
            }
        }
    }

    /**
     * Reveals one cell for one second, then removes it from the revealed set.
     */
    private fun revealCell(key: TableCellKey) {
        setState { it.copy(revealedCells = it.revealedCells + key) }
        storeScope.launch {
            delay(1000)
            setState { it.copy(revealedCells = it.revealedCells - key) }
        }
    }
}
