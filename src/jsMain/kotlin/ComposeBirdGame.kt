import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import data.Tube
import kotlin.js.Date

class ComposeBirdGame : Game {

    companion object {
        const val COLUMNS = 15
        const val ROWS = 9
        const val BIRD_COLUMN = 1
        private const val TUBES_START_FROM = (COLUMNS * 0.75).toInt()
        private const val TOTAL_TUBES = 50
        private const val TUBE_SPACE = 3
        private const val TUBE_WEIGHT = 500
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
                if (tubePosition > TUBES_START_FROM && tubePosition % TUBE_SPACE == 0) { // To give space to each tube
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

        // Adding gap to the full tube
        tube[gap1] = false
        tube[gap1 - 1] = false
        tube[gap1 - 2] = false

        return tube
    }

    override val gameFrame: State<GameFrame> = _gameFrame
    private var tubeLastSteppedAt = 0.0
    private var birdLastSteppedAt = 0.0
    private var shouldMoveBirdUp = false
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
            val newBirdPos = when {
                shouldMoveBirdUp -> {
                    birdLastSteppedAt = now
                    shouldMoveBirdUp = false
                    birdPos - 1 // move up
                }
                birdDiff > BIRD_WEIGHT -> {
                    birdLastSteppedAt = now
                    birdPos + 1 // move down
                }
                else -> {
                    birdPos
                }
            }

            // Checking if bird gone out
            val newIsGameOver = if (newBirdPos < 0 || newBirdPos >= ROWS || isCollidedWithTube(newBirdPos, tubes)) {
                true
            } else {
                isGameOver
            }

            copy(
                isGameOver = newIsGameOver,
                tubes = newTubes,
                birdPos = newBirdPos
            )
        }
    }

    private fun isCollidedWithTube(newBirdPos: Int, tubes: List<Tube>): Boolean {
        val birdTube = tubes.find { it.position == BIRD_COLUMN }
        return birdTube?.coordinates?.get(newBirdPos) ?: false
    }

    override fun moveBirdUp() {
        shouldMoveBirdUp = true
    }

    private inline fun update(func: GameFrame.() -> GameFrame) {
        _gameFrame.value = _gameFrame.value.func()
    }
}