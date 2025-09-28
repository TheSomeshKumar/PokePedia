import androidx.compose.ui.window.ComposeUIViewController
import com.thesomeshkumar.pokepedia.app.App
import com.thesomeshkumar.pokepedia.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }) { App() }
