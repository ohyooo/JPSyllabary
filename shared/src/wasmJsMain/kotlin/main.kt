import androidx.compose.ui.ExperimentalComposeUiApi
import com.ohyooo.shared.compose.*
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        App()
    }
}