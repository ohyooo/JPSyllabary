import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ohyooo.shared.App


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
