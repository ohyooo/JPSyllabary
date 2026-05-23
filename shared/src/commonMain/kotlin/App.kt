import androidx.compose.runtime.Composable
import com.ohyooo.shared.compose.Main
import com.ohyooo.shared.theme.AppTheme

/**
 * Shared application entry point for Android, desktop, iOS, and wasm targets.
 *
 * Platform launchers call this Composable once. It applies the app theme and
 * delegates navigation and screen rendering to [Main].
 */
@Composable
fun App() {
    AppTheme {
        Main()
    }
}
