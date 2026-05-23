package com.ohyooo.shared.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohyooo.shared.model.twisterList
import com.ohyooo.shared.viewmodel.TwisterIntent
import com.ohyooo.shared.viewmodel.TwisterStore

/**
 * Tongue-twister training screen.
 *
 * The screen observes [TwisterStore.state]. Pressing the primary button dispatches
 * [TwisterIntent.Reset], and each pager reacts to the store's reset signal.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Twister(onMenuClick: () -> Unit = {}, store: TwisterStore = rememberTwisterStore()) {
    val uiState by store.state.collectAsState()

    Column {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "menu",
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
                .clickable {
                    onMenuClick()
                },
            tint = MaterialTheme.colorScheme.inverseSurface
        )

        HorizontalDivider()

        repeat(twisterList.size) { column ->
            val state = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f,
                pageCount = { twisterList[column].size },
            )
            LaunchedEffect(uiState.resetRequest) {
                if (uiState.resetRequest > 0L) {
                    state.animateScrollToPage(0)
                }
            }
            HorizontalPager(
                state = state,
                modifier = Modifier.weight(1F)
            ) { row ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                ) {
                    val s = twisterList[column][row].joinToString("")
                    Box(
                        modifier = Modifier.weight(1F),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoResizeText(
                            text = s,
                            fontSizeRange = FontSizeRange(8.sp, 24.sp),
                            color = MaterialTheme.colorScheme.inverseSurface,
                            textAlign = TextAlign.Justify,
                            maxLines = 2,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }

            HorizontalDivider()
        }
        ClickButton(modifier = Modifier.weight(4F)) {
            store.dispatch(TwisterIntent.Reset)
        }
    }
}

/**
 * Creates the tongue-twister store and closes it when the screen leaves
 * composition.
 */
@Composable
private fun rememberTwisterStore(): TwisterStore {
    val store = remember { TwisterStore() }
    DisposableEffect(store) {
        onDispose { store.close() }
    }
    return store
}
