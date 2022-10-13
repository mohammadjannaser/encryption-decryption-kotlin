fun main() {
    val choice = readln().toInt()

    println(when (choice) {
        1 -> "No!"
        2 -> "Yes!"
        3 -> "No!"
        4 -> "No!"
        else -> "Unknown number"
    }) 
}
