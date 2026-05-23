package com.ohyooo.shared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.ohyooo.shared.common.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohyooo.shared.generated.resources.Res
import com.ohyooo.shared.generated.resources.round
import com.ohyooo.shared.viewmodel.SingleIntent
import com.ohyooo.shared.viewmodel.SingleStore
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Single-character training screen.
 *
 * It observes [SingleStore.state] and dispatches [SingleIntent] from user
 * actions. Business state such as the current kana and hint visibility stays in
 * the store.
 */
@Composable
fun Single(onMenuClick: () -> Unit = {}, store: SingleStore = rememberSingleStore()) {
    val uiState by store.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                tint = MaterialTheme.colorScheme.inverseSurface,
                contentDescription = "menu",
                modifier = Modifier.clickable {
                    onMenuClick()
                },
            )
        }

        Type(modifier = Modifier.weight(1F), stringResource(uiState.type))

        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

        Character(modifier = Modifier.weight(2F), uiState.character)

        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

        Hint(
            modifier = Modifier.weight(2F),
            text = uiState.hint,
            visible = uiState.isHintVisible,
            onClick = { store.dispatch(SingleIntent.ToggleHint) },
        )

        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

        ClickButton(modifier = Modifier.weight(2F)) {
            store.dispatch(SingleIntent.Next)
        }
    }
}

/**
 * Creates the screen-level store and closes its coroutine scope when the screen
 * leaves composition.
 */
@Composable
private fun rememberSingleStore(): SingleStore {
    val store = remember { SingleStore() }
    DisposableEffect(store) {
        onDispose { store.close() }
    }
    return store
}

/**
 * Displays the localized kana category for the current card.
 */
@Composable
fun Type(modifier: Modifier, text: String) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = MaterialTheme.colorScheme.inverseSurface,
            fontSize = 18.sp,
            textAlign = TextAlign.End,
        )
    }
}

/**
 * Displays the current kana character.
 */
@Composable
fun Character(modifier: Modifier, text: String) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 80.sp, color = MaterialTheme.colorScheme.inverseSurface, modifier = modifier)
    }
}

/**
 * Displays or hides the current romaji hint.
 *
 * Clicking this region dispatches [SingleIntent.ToggleHint] through the caller's
 * [onClick] callback.
 */
@Composable
fun Hint(modifier: Modifier, text: String, visible: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (visible) {
            Text(text = text, fontSize = 80.sp, color = MaterialTheme.colorScheme.inverseSurface, modifier = modifier)
        }
    }
}

/**
 * Reusable image button used by training screens for the primary action.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun ClickButton(modifier: Modifier, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(Res.drawable.round),
            contentDescription = "Next",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .fillMaxHeight(2 / 3F)
                .aspectRatio(1F)
        )
    }
}
