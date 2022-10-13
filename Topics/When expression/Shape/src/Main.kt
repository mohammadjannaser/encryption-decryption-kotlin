
const val Square = 1
const val Circle = 2
const val Triangle = 3
const val Rhombus = 4
const val Message = "You have chosen a "
const val ERROR_MESSAGE = "There is no such shape!"


fun main(args: Array<String>) {
    // write your code here
    val choice = readln().toInt()

    println(
        when (choice) {
            Square -> Message + "square"
            Circle -> Message + "circle"
            Triangle -> Message + "triangle"
            Rhombus -> Message + "rhombus"
            else -> ERROR_MESSAGE
        }
    )
}