import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.checked
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.get


fun main() {

    val game: Game = ComposeBirdGame()

    val body = document.getElementsByTagName("body")[0] as HTMLElement
    body.addEventListener("keyup", {
        when ((it as KeyboardEvent).keyCode) {
            38 -> { // Arrow up
                game.moveBirdUp()
            }
        }
    })

    renderComposable(rootElementId = "root") {

        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.Center)
                }
            }
        ) {


            val gameFrame by game.gameFrame

            LaunchedEffect(Unit) {
                while (!gameFrame.isGameOver) {
                    delay(60)
                    game.step()
                }
            }

            println("Rendering...")

            // Game area
            Div {

                H1(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            justifyContent(JustifyContent.Center)
                        }
                    }
                ) {
                    Text("🐦 Compose Bird!")
                }

                if (gameFrame.isGameOver) {
                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                flexDirection(FlexDirection.Column)
                                justifyContent(JustifyContent.Center)
                            }
                        }
                    ) {
                        H2(
                            attrs = {
                                style {
                                    alignSelf(AlignSelf.Center)
                                }
                            }
                        ) {
                            Text("💀 Game Over!")
                        }

                        Button(
                            attrs = {
                                onClick {
                                    window.location.reload()
                                }
                            }
                        ) {
                            Text("RETRY!")
                        }
                    }


                } else {
                    repeat(ComposeBirdGame.ROWS) { rowIndex ->
                        Div {
                            repeat(ComposeBirdGame.COLUMNS) { columnIndex ->
                                Input(
                                    InputType.Radio,

                                    attrs = {

                                        val tube = gameFrame.tubes.find { it.position == columnIndex }
                                        val isTube = tube?.coordinates?.get(rowIndex) ?: false
                                        val isBird =
                                            !isTube && columnIndex == ComposeBirdGame.BIRD_COLUMN && rowIndex == gameFrame.birdPos

                                        // Constructing tube
                                        if (isTube || isBird) {
                                            checked(true)
                                        } else {
                                            checked(false)
                                        }

                                        if (isBird) {
                                            disabled(false)
                                        } else {
                                            disabled(true)
                                        }

                                        val radioId = when {
                                            isBird -> "rBird"
                                            isTube -> "rTube"
                                            else -> null
                                        }

                                        if (radioId != null) {
                                            id(radioId)
                                        }

                                        style {
                                            width(25.px)
                                            height(25.px)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}