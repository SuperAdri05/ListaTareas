import androidx.compose.ui.graphics.Color

data class Task(
    val id: Int,
    val title: String,
    var isCompleted: Boolean = false,
    var priority: Priority = Priority.MEDIUM
)

enum class Priority(val color: Color) {
    HIGH(Color.Red),
    MEDIUM(Color.Yellow),
    LOW(Color.Green)
}
