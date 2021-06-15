import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import data.Tube
import kotlin.js.Date

class ComposeBirdGame : Game {

    companion object {
        const val COLUMNS = 15
        const val ROWS = 9
        const val PLAYER_COLUMN = 1
        private const val TOTAL_TUBES = 50
        private const val TUBE_SPACE = 3
        private const val TUBE_WEIGHT = 800
        private const val BIRD_WEIGHT = 300
    }

    private val _gameFrame: MutableState<GameFrame> by lazy {
        mutableStateOf(
            // First frame
            GameFrame(
                birdPos = ROWS / 2,
                tubes = buildLevel(),
                isGameOver = false
            )
        )
    }

    private fun buildLevel(): List<Tube> {
        return mutableListOf<Tube>().apply {
            var tubesAdded = 0
            var tubePosition = 0
            while (tubesAdded < TOTAL_TUBES) {
                if (tubePosition != 0 && tubePosition % TUBE_SPACE == 0) { // To give space to each tube
                    add(
                        Tube(
                            tubePosition,
                            buildRandomTube()
                        )
                    )
                    tubesAdded++
                }
                tubePosition++
            }
        }
    }

    private val tubeGapRange = 2 until ROWS

    private fun buildRandomTube(): List<Boolean> {

        val tube = mutableListOf<Boolean>().apply {
            repeat(ROWS) {
                add(true)
            }
        }

        val gap1 = tubeGapRange.random()
        val gap2 = gap1 - 1

        // Adding gap to the full tube
        tube[gap1] = false
        tube[gap2] = false

        return tube
    }

    override val gameFrame: State<GameFrame> = _gameFrame
    private var tubeLastSteppedAt = 0.0
    private var birdLastSteppedAt = 0.0
    override fun step() {
        update {

            // Stepping tube
            val now = Date().getTime()
            val tubeDiff = now - tubeLastSteppedAt
            val newTubes = if (tubeDiff > TUBE_WEIGHT) {
                tubeLastSteppedAt = now
                tubes.map {
                    it.copy(position = it.position - 1)
                }
            } else {
                tubes
            }

            // Stepping down bird
            val birdDiff = now - birdLastSteppedAt
            val newBirdPos = if (birdDiff > BIRD_WEIGHT) {
                birdLastSteppedAt = now
                birdPos + 1
            } else {
                birdPos
            }

            copy(
                tubes = newTubes,
                birdPos = newBirdPos
            )
        }
    }

    override fun moveBirdUp() {
        update {
            birdLastSteppedAt = Date().getTime()
            copy(
                birdPos = birdPos - 1
            )
        }
    }

    private inline fun update(func: GameFrame.() -> GameFrame) {
        _gameFrame.value = _gameFrame.value.func()
    }
}