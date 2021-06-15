import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import kotlinx.browser.document
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
                                    display(DisplayStyle.Flex)
                                    justifyContent(JustifyContent.Center)
                                }
                            }
                        ) {
                            Text("💀 Game Over!")
                        }

                        P(
                            attrs = {
                                style {
                                    alignSelf(AlignSelf.End)
                                }
                            }
                        ) {
                            Text("refresh to retry!")
                        }
                    }


                } else {
                    repeat(ComposeBirdGame.ROWS) { rowIndex ->
                        Div {
                            repeat(ComposeBirdGame.COLUMNS) { columnIndex ->
                                Input(
                                    InputType.Radio,
                                    attrs = {
                                        disabled(true) // no external interaction
                                        val tube = gameFrame.tubes.find { it.position == columnIndex }

                                        // Constructing tube
                                        checked(tube?.coordinates?.get(rowIndex) ?: false)

                                        // Checking player position
                                        if (columnIndex == ComposeBirdGame.PLAYER_COLUMN && rowIndex == gameFrame.birdPos) {
                                            // Player
                                            checked(true)
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