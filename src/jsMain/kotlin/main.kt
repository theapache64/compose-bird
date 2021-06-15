import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.selectors.attr
import org.jetbrains.compose.web.css.selectors.id
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.renderComposableInBody
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.FocusEventInit
import org.w3c.dom.get

const val COLUMNS = 10
const val ROWS = 9
const val PLAYER_COLUMN = 1

fun main() {

    var score by mutableStateOf(0)
    val playerPos = mutableStateOf(ROWS / 2) // Start at middle

    val body = document.getElementsByTagName("body")[0] as HTMLElement
    body.addEventListener("keyup", {
        score = 2
    })

    renderComposable(rootElementId = "root") {

    }
}