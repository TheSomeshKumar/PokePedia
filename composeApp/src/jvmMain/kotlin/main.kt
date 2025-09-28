import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.thesomeshkumar.pokepedia.app.App
import com.thesomeshkumar.pokepedia.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "PokePedia",
        ) {
            App()
        }
    }
}

