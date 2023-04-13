package com.ohyooo.jpsyllabary.ui.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.ohyooo.jpsyllabary.R
import com.ohyooo.jpsyllabary.model.*
import com.ohyooo.jpsyllabary.ui.compose.multifab.MultiFabItem
import com.ohyooo.jpsyllabary.ui.compose.multifab.MultiFabState
import com.ohyooo.jpsyllabary.ui.compose.multifab.MultiFloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Preview
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Fragment(onMenuClick: () -> Unit = {}) {
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(0)

    var nSequence by remember { mutableStateOf(normalSequence) }
    var sSequence by remember { mutableStateOf(sonantSequence) }

    Box {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(modifier = Modifier
                    .padding(start = 0.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
                    .clickable {
                        onMenuClick()
                    }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "menu", tint = MaterialTheme.colorScheme.inverseSurface)
                }

                Tab(pagerState, scope)
            }

            HorizontalPager(count = tabList.size, state = pagerState, modifier = Modifier.weight(1F)) { page ->
                Table(page = page, currentPage = currentPage, scope = scope, nSequence = nSequence, sSequence = sSequence)
            }
        }

        Button {
            nSequence = normalSequence.copyOf()
            sSequence = sonantSequence.copyOf()
        }
    }
}

private val tabList = listOf(R.string.hiragana, R.string.katakana, R.string.romaji, R.string.sonant)

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Tab(pagerState: PagerState, scope: CoroutineScope) {
    val tabIndex = pagerState.currentPage

    TabRow(
        selectedTabIndex = tabIndex,
        indicator = { tabPositions -> TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)) },
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        tabList.forEachIndexed { index, stringRes ->
            Tab(
                selected = tabIndex == index,
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

@Composable
private fun Table(page: Int, currentPage: Int, scope: CoroutineScope, nSequence: IntArray, sSequence: IntArray) {
    var isSnoat by remember { mutableStateOf(false) }

    when (page) {
        0 -> Table(chars = hiragana.asList(), hints = romaji.asList(), nSequence, scope)
        1 -> Table(chars = katakana.asList(), hints = romaji.asList(), nSequence, scope)
        3 -> Table(chars = sonant.asList(), hints = sonantRomaji.asList(), sSequence, scope)
        2 -> {
            when (currentPage) {
                1 -> {
                    isSnoat = false
                    Table(romaji.asList(), romaji.asList(), nSequence, scope)
                }

                3 -> {
                    isSnoat = true
                    Table(sonantRomaji.asList(), sonantRomaji.asList(), nSequence, scope)
                }

                2 -> {
                    val chars = if (isSnoat) sonantRomaji else romaji
                    Table(chars.asList(), chars.asList(), if (isSnoat) sSequence else nSequence, scope)
                }
            }
        }
    }
}

@Composable
private fun Table(chars: List<String>, hints: List<String>, sequence: IntArray, scope: CoroutineScope) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {
        sequence.asList().chunked(5).forEachIndexed { _, fragment ->
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    fragment.forEachIndexed { _, index ->
                        val alphabet = chars[index]
                        var char by remember(alphabet) { mutableStateOf(alphabet) }
                        Box(
                            modifier = Modifier
                                .weight(1F)
                                .aspectRatio(1F)
                                .border(width = 0.25.dp, color = Color.DarkGray)
                                .clickable {
                                    scope.launch {
                                        char = hints[index]
                                        delay(1000)
                                        char = alphabet
                                    }
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

@Composable
fun BoxScope.Button(onClick: (Long) -> Unit) {
    var toState by remember { mutableStateOf(MultiFabState.COLLAPSED) }

    var offsetX by remember { mutableStateOf(0F) }
    var offsetY by remember { mutableStateOf(0F) }

    val draggableStateX = rememberDraggableState {
        offsetX += it
    }
    val draggableStateY = rememberDraggableState {
        offsetY += it
    }

    MultiFloatingActionButton(
        fabIcon = Icons.Rounded.Add,
        items = if (toState == MultiFabState.EXPANDED) {
            listOf(
                MultiFabItem("Shuffle", Icons.Rounded.Refresh, "Shuffle"),
                MultiFabItem("Order", Icons.Rounded.List, "Order")
            )
        } else emptyList(),
        toState = toState,
        showLabels = false,
        stateChanged = { toState = it },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 64.dp)
            .offset {
                IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
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
            shuffle()
            onClick(1)
        }
        if (item.label == "Order") {
            order()
            onClick(0)
        }
        toState = MultiFabState.COLLAPSED
    }
}
