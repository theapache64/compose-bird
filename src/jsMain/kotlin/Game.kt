import androidx.compose.runtime.State

interface Game {
    val gameFrame: State<GameFrame>
    fun step()
    fun moveBirdUp()
}