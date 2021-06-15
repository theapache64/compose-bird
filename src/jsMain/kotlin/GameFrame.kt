import data.Tube

data class GameFrame(
    val birdPos: Int,
    val tubes: List<Tube>,
    val isGameOver: Boolean
)
