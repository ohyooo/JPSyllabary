package com.ohyooo.shared.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ohyooo.shared.theme.AppTheme

@Preview(
    name = "Main",
    group = "Screens",
    widthDp = 360,
    heightDp = 720,
    showBackground = true,
)
@Composable
private fun MainPreview() {
    PreviewContent {
        Main()
    }
}

@Preview(
    name = "Single",
    group = "Screens",
    widthDp = 360,
    heightDp = 720,
    showBackground = true,
)
@Composable
private fun SinglePreview() {
    PreviewContent {
        Single()
    }
}

@Preview(
    name = "Table",
    group = "Screens",
    widthDp = 360,
    heightDp = 720,
    showBackground = true,
)
@Composable
private fun TablePreview() {
    PreviewContent {
        Fragment()
    }
}

@Preview(
    name = "Twister",
    group = "Screens",
    widthDp = 360,
    heightDp = 720,
    showBackground = true,
)
@Composable
private fun TwisterPreview() {
    PreviewContent {
        Twister()
    }
}

@Preview(
    name = "GitHub",
    group = "Screens",
    widthDp = 360,
    heightDp = 720,
    showBackground = true,
)
@Composable
private fun GithubPreview() {
    PreviewContent {
        Github()
    }
}

@Composable
private fun PreviewContent(content: @Composable () -> Unit) {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            content()
        }
    }
}
