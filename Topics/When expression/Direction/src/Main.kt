/**
 * Write a program, that reads a direction number: 1 – up, 2 – down, 3 – left, 4 – right, 0 – stay
 * and outputs move up (or move down, or move left, or move right, or do not move depending on the number).
 * If the input does not correspond to any of the listed directions, output error!
 * Output text should match the sample! It is true for the letter case and spaces.
 */

const val UP = 1
const val DOWN = 2

fun main() {



    val direction = readln().toInt()

    val resutl = when(direction) {
        UP -> "move up"
        DOWN -> "move down"
        3 -> "move left"
        4 -> "move right"
        0 -> "do not move"
        else -> "error!"
    }

    println(resutl)
}