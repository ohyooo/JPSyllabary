package com.ohyooo.shared.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohyooo.shared.common.Text
import com.ohyooo.shared.compose.multifab.MultiFabItem
import com.ohyooo.shared.compose.multifab.MultiFabState
import com.ohyooo.shared.compose.multifab.MultiFloatingActionButton
import com.ohyooo.shared.generated.resources.Res
import com.ohyooo.shared.generated.resources.*
import com.ohyooo.shared.model.hiragana
import com.ohyooo.shared.model.katakana
import com.ohyooo.shared.model.romaji
import com.ohyooo.shared.model.sonant
import com.ohyooo.shared.model.sonantRomaji
import com.ohyooo.shared.viewmodel.RomajiSource
import com.ohyooo.shared.viewmodel.TableCellKey
import com.ohyooo.shared.viewmodel.TableIntent
import com.ohyooo.shared.viewmodel.TableStore
import com.ohyooo.shared.viewmodel.TableUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

/**
 * Kana table screen.
 *
 * It observes [TableStore.state] and dispatches [TableIntent] for tab changes,
 * cell reveals, ordering actions, and floating-action-button movement.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Fragment(onMenuClick: () -> Unit = {}, store: TableStore = rememberTableStore()) {
    val uiState by store.state.collectAsState()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(0, pageCount = { tabList.size })

    LaunchedEffect(pagerState.currentPage) {
        store.dispatch(TableIntent.PageChanged(pagerState.currentPage))
    }

    Box {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 0.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
                        .clickable {
                            onMenuClick()
                        }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "menu", tint = MaterialTheme.colorScheme.inverseSurface)
                }

                Tab(selectedPage = uiState.selectedPage, pagerState = pagerState, scope = scope)
            }

            HorizontalPager(state = pagerState, modifier = Modifier.weight(1F)) { page ->
                Table(page = page, uiState = uiState, dispatch = store::dispatch)
            }
        }

        TableFab(uiState = uiState, dispatch = store::dispatch)
    }
}

/**
 * Creates the table store for this screen instance and closes it when the screen
 * leaves composition.
 */
@Composable
private fun rememberTableStore(): TableStore {
    val store = remember { TableStore() }
    DisposableEffect(store) {
        onDispose { store.close() }
    }
    return store
}

private val tabList = listOf(Res.string.hiragana, Res.string.katakana, Res.string.romaji, Res.string.sonant)

/**
 * Renders the four table tabs and scrolls the pager when a tab is selected.
 *
 * [selectedPage] comes from [TableUiState] so the selected tab reflects store
 * state rather than local UI state.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Tab(selectedPage: Int, pagerState: PagerState, scope: CoroutineScope) {
    TabRow(
        selectedTabIndex = selectedPage,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        tabList.forEachIndexed { index, stringRes ->
            Tab(
                selected = selectedPage == index,
                onClick = { scope.launch { pagerState.scrollToPage(index) } },
                text = {
                    AutoResizeText(
                        text = stringResource(stringRes),
                        fontSizeRange = FontSizeRange(8.sp, 12.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        maxLines = 1,
                    )
                },
            )
        }
    }
}

/**
 * Chooses which grid data to render for one pager page.
 *
 * The romaji page reuses [TableUiState.romajiSource] so it follows the last
 * normal or sonant table selected by the user.
 */
@Composable
private fun Table(page: Int, uiState: TableUiState, dispatch: (TableIntent) -> Unit) {
    when (page) {
        0 -> TableGrid(
            page = page,
            source = RomajiSource.NORMAL,
            chars = hiragana.asList(),
            hints = romaji.asList(),
            sequence = uiState.normalSequence,
            revealedCells = uiState.revealedCells,
            dispatch = dispatch,
        )

        1 -> TableGrid(
            page = page,
            source = RomajiSource.NORMAL,
            chars = katakana.asList(),
            hints = romaji.asList(),
            sequence = uiState.normalSequence,
            revealedCells = uiState.revealedCells,
            dispatch = dispatch,
        )

        3 -> TableGrid(
            page = page,
            source = RomajiSource.SONANT,
            chars = sonant.asList(),
            hints = sonantRomaji.asList(),
            sequence = uiState.sonantSequence,
            revealedCells = uiState.revealedCells,
            dispatch = dispatch,
        )

        2 -> {
            val isSonant = uiState.romajiSource == RomajiSource.SONANT
            val chars = if (isSonant) sonantRomaji else romaji
            TableGrid(
                page = page,
                source = uiState.romajiSource,
                chars = chars.asList(),
                hints = chars.asList(),
                sequence = if (isSonant) uiState.sonantSequence else uiState.normalSequence,
                revealedCells = uiState.revealedCells,
                dispatch = dispatch,
            )
        }
    }
}

/**
 * Renders a grid of kana or romaji cells.
 *
 * Cell click events dispatch [TableIntent.RevealCell]. The store decides how long
 * the hint remains visible.
 */
@Composable
private fun TableGrid(
    page: Int,
    source: RomajiSource,
    chars: List<String>,
    hints: List<String>,
    sequence: List<Int>,
    revealedCells: Set<TableCellKey>,
    dispatch: (TableIntent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {
        sequence.chunked(5).forEachIndexed { _, fragment ->
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    fragment.forEachIndexed { _, index ->
                        val alphabet = chars[index]
                        val key = TableCellKey(page = page, index = index, source = source)
                        val char = if (key in revealedCells) hints[index] else alphabet
                        Box(
                            modifier = Modifier
                                .weight(1F)
                                .aspectRatio(1F)
                                .border(width = 0.25.dp, color = Color.DarkGray)
                                .clickable {
                                    dispatch(TableIntent.RevealCell(key))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                color = MaterialTheme.colorScheme.inverseSurface,
                                textAlign = TextAlign.Center,
                                fontSize = 32.sp,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Floating action button for ordering controls.
 *
 * Expansion, drag offset, and order actions are stored in [TableUiState] through
 * [TableIntent] instead of local mutable state.
 */
@Composable
private fun BoxScope.TableFab(uiState: TableUiState, dispatch: (TableIntent) -> Unit) {
    val toState = if (uiState.isFabExpanded) {
        MultiFabState.EXPANDED
    } else {
        MultiFabState.COLLAPSED
    }

    val draggableStateX = rememberDraggableState {
        dispatch(TableIntent.DragFab(deltaX = it))
    }
    val draggableStateY = rememberDraggableState {
        dispatch(TableIntent.DragFab(deltaY = it))
    }

    MultiFloatingActionButton(
        fabIcon = Icons.Rounded.Add,
        items = if (uiState.isFabExpanded) {
            listOf(
                MultiFabItem(Icons.Rounded.Refresh, "Shuffle"),
                MultiFabItem(Icons.AutoMirrored.Rounded.List, "Order")
            )
        } else emptyList(),
        toState = toState,
        showLabels = false,
        stateChanged = { dispatch(TableIntent.SetFabExpanded(it == MultiFabState.EXPANDED)) },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 64.dp)
            .offset {
                IntOffset(uiState.fabOffsetX.roundToInt(), uiState.fabOffsetY.roundToInt())
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = draggableStateX
            )
            .draggable(
                orientation = Orientation.Vertical,
                state = draggableStateY
            ),
    ) { item ->
        if (item.label == "Shuffle") {
            dispatch(TableIntent.Shuffle)
        }
        if (item.label == "Order") {
            dispatch(TableIntent.Order)
        }
    }
}
