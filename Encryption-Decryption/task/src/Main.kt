import java.io.File

const val MODE_ARGS_NAME = "-mode"
const val KEY_ARGS_NAME = "-key"
const val DATA_ARGS_NAME = "-data"
const val IN_ARGS_NAME = "-in"
const val OUT_ARGS_NAME = "-out"
const val ALGORITHM_NAME = "-alg"
const val ENCRYPT = "enc"
const val DECRYPT = "dec"

var outFilename = ""

enum class Algorithm(title: String) {
    SHIFT("shift"),
    UNICODE("unicode")
}

const val ENC = "enc"
const val DEC = "dec"
const val INVALID_OPERATION = "Invalid Operation"

fun main(args: Array<String>) {

    val dataInOut = DataInOut(args)
    dataInOut.getData()
    dataInOut.outData()


}


/**
 * input sample 1 "mode enc -in road_to_treasure.txt -out protected.txt -key 5 -alg unicode"
 * input sample 2 "-mode enc -key 5 -data "Welcome to hyperskill!" -alg unicode"
 * input sample 3 "-key 5 -alg unicode -data "\jqhtrj%yt%m~ujwxpnqq&" -mode dec"
 * input sample 4 "-key 5 -alg shift -data "Welcome to hyperskill!" -mode enc"
 * input sample 5 "-key 5 -alg shift -data "Bjqhtrj yt mdujwxpnqq!" -mode dec"
 */

class DataInOut(val args: Array<String>) {

    var mode = ENCRYPT
    var algorithm = Algorithm.SHIFT
    var key = 0
    var data = ""
    var inFileName = ""

    fun getData() {

        val modeIndex = args.indexOf(MODE_ARGS_NAME)
        if (modeIndex >= 0 && modeIndex < args.size) {
            if (args[modeIndex + 1] == DECRYPT) mode = DECRYPT
        }

        val keyIndex = args.indexOf(KEY_ARGS_NAME)
        if (keyIndex >= 0 && keyIndex < args.size) {
            val value = args[keyIndex + 1].toIntOrNull()
            if (value != null) key = value.toInt()
        }

        var dataIndex = args.indexOf(DATA_ARGS_NAME)
        if (dataIndex >= 0 && dataIndex < args.size) {
            data = args.joinToString(" ").substringAfter("\"").substringBefore("\"")

        } else {
            dataIndex = args.indexOf(IN_ARGS_NAME)
            if (dataIndex >= 0 && dataIndex < args.size) {
                inFileName = args[dataIndex + 1]
                val file = File(inFileName)
                try {
                    data = file.readText()
                } catch (e: Exception) {
                    println("Error: " + e.message)
                    return
                }
            }
        }
    }

    fun outData() {

        var outIndex = args.indexOf(OUT_ARGS_NAME)
        if (outIndex >= 0 && outIndex < args.size) {
            outFilename = args[outIndex + 1]
        }

        var algoIndex = args.indexOf(ALGORITHM_NAME)
        if (algoIndex >= 0 && algoIndex < args.size) {

            algorithm = if (args[algoIndex + 1] == "shift") Algorithm.SHIFT else Algorithm.UNICODE
        }

        if (algorithm == Algorithm.SHIFT) {

            val shiftAlgorithm = ShiftAlgorithm()

            // encrypt or decrypt the file.
            val outPut = shiftAlgorithm.encryptAndDecrypt(message = data, key = key, mode)

            // output the file
            if (outFilename.isEmpty()) println(outPut) else FileOperation().printOnFile(outPut)

        } else if (algorithm == Algorithm.UNICODE) {

            val unicodeAlgorithm = UnicodeAlgorithm()

            // encrypt or decrypt the file.
            val outPut = if (mode == ENCRYPT) unicodeAlgorithm.encryptBasedOnUnicode(message = data, key = key)
            else unicodeAlgorithm.decryptBasedOnUnicode(message = data, key = key)

            // output the file
            if (outFilename.isEmpty()) println(outPut) else FileOperation().printOnFile(outPut)

        }


    }
}


class UnicodeAlgorithm {


    fun decryptBasedOnUnicode(message: String, key: Int): String {
        var result = ""
        for (i in message.indices) {
            result += (message[i].code - key).toChar()
        }

        return result
    }

    fun encryptBasedOnUnicode(message: String, key: Int): String {

        var result = ""
        for (i in message.indices) {
            result += (message[i].code + key).toChar()
        }

        return result

    }
}


class FileOperation {

    fun printOnFile(message: String) {
        try {
            File(outFilename).writeText(message)
        } catch (e: Exception) {
            println("Error: " + e.message)
        }
    }
}


class ShiftAlgorithm {

    private val capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz"

    fun encryptAndDecrypt(message: String, key: Int, mode: String): String {

        val encryptedMessage = message.map {

            when (it) {
                in capitalLetters -> {
                    capitalLetters[getShiftedIndex(key, capitalLetters.indexOf(it), reverse = mode == DECRYPT)]
                }

                in lowerCaseLetters -> {
                    lowerCaseLetters[getShiftedIndex(key, lowerCaseLetters.indexOf(it), reverse = mode == DECRYPT)]
                }

                else -> {
                    it
                }
            }
        }

        return encryptedMessage.joinToString("")

    }


    fun encrypt(message: String, key: Int): String {

        val encryptedMessage = message.map {

            when (it) {
                in capitalLetters -> {
                    capitalLetters[getShiftedIndex(key, capitalLetters.indexOf(it))]
                }

                in lowerCaseLetters -> {
                    lowerCaseLetters[getShiftedIndex(key, lowerCaseLetters.indexOf(it))]
                }

                else -> {
                    it
                }
            }
        }

        return encryptedMessage.joinToString("")

    }

    fun decrypt(message: String, key: Int): String {

        val dec = true

        val decryptedMessage = message.map {

            when (it) {
                in capitalLetters -> {
                    capitalLetters[getShiftedIndex(key, capitalLetters.indexOf(it), reverse = true)]
                }

                in lowerCaseLetters -> {
                    lowerCaseLetters[getShiftedIndex(key, lowerCaseLetters.indexOf(it), reverse = true)]
                }

                else -> {
                    it
                }
            }
        }

        return decryptedMessage.joinToString("")
    }


    private fun getShiftedIndex(key: Int, currentIndex: Int, reverse: Boolean = false): Int {

        var index = currentIndex // 25
        val lastIndex = 25
        val startIndex = 0

        if (reverse) {
            repeat(key) {
                if (index == startIndex) index = lastIndex else index--
            }
        } else {
            repeat(key) {
                if (index >= lastIndex) index = startIndex else index++
            }
        }


        return index
    }
}


fun lambdaFunctions() {
    val items = arrayListOf("Guard", "It", "Well")

    for (index in items.indices) {
        println("Argument ${index + 1}: ${items[index]}. It has ${items[index].length} characters")
    }

    val sumObject = ::sum
    val sumObject1: (Int, Int) -> Int = ::sum

    fun identity(num: Int) = num
    fun half(num: Int) = num / 2
    fun zero(num: Int) = 0

    val lambda: (Long, Long) -> Long = { leftBorder, rightBorder ->
        var result = 1L
        for (i in leftBorder..rightBorder) {
            result *= i
        }
        result

    }

    fun compose(g: (Int) -> Int, h: (Int) -> Int): (Int) -> Int {

        return { number ->
            g(h(number))
        }
    }
}

fun fizzbuzz(from: Int, to: Int, transformation: (Int) -> String) {
    for (number in from..to) {
        println(transformation(number))
    }
}


fun sum(a: Int, b: Int) = a + b

fun swapArray() {

    val numbers = readLine()!!.split(' ').map { it.toInt() }.toIntArray()
    // Do not touch lines above
    // Write only exchange actions here.
    val swapNumbers: IntArray = intArrayOf()

    val firstElement = numbers.first()
    numbers[0] = numbers.last()
    numbers[numbers.size - 1] = firstElement


    // Do not touch lines below
    println(numbers.joinToString(separator = " "))
}

fun getEncryptionInput() {
    val operation = readln()
    val message = readln()
    val key = readln().toInt()



    when (operation) {
        ENC -> encryptBasedOnUnicode(message, key)
        DEC -> decryptBasedOnUnicode(message, key)
        else -> INVALID_OPERATION
    }
}


fun decryptBasedOnUnicode(message: String, key: Int) {
    val encryptedMessage = message.map {
        (it.code - key).toChar()
    }
    encryptedMessage.forEach { c ->
        print(c)
    }
}

fun encryptBasedOnUnicode(message: String, key: Int) {

    val encryptedMessage = message.map {
        (it.code + key).toChar()
    }
    encryptedMessage.forEach { c ->
        print(c)
    }

}


fun knowledgeIsKey() {

    val alphabets = "abcdefghijklmnopqrstuvwxyz"

    val message = "we found a treasure!"
    val key = 5

    val encryptedMessage = message.map {

        if (it in alphabets) {
            alphabets[getIndexByKey(key, alphabets.indexOf(it))]
        } else {
            it
        }
    }

    encryptedMessage.forEach { c ->
        print(c)
    }

}

fun getIndexByKey(key: Int, currentIndex: Int): Int {

    var index = currentIndex // 25
    val lastIndex = 25
    val startIndex = 0
    // key 3

    repeat(key) {
        if (index >= lastIndex) {
            index = startIndex
        } else {
            index++
        }

    }

    return index
}


fun parseUrl() {
//    val url = readln()
    val url = "https://target.com/index.html?pass=12345&port=8080&cookie=&host=localhost"

    val passExist = url.contains("pass")

    val params = url.split("?").last().split("&")
        .map { it.split("=").first() + " : " + it.split("=").last().ifEmpty { "not found" } } // listOf<String>()

    for (p in params) {
        println(p)
    }

    if (passExist) {
        val password = url.substringAfter("pass=").substringBefore("&")
        println("password : $password")
    }


    println(params)


}

fun cgPercentage() {
    val input = readln()
    var count = 0
    for (c in input) {
        if (c.lowercase() == "c" || c.lowercase() == "g") {
            count++
        }
    }
    println(count.toDouble() / input.length.toDouble() * 100)
}

fun doubleCharacters() {
    val word = readln()

    for (i in word) {
        print("$i$i")
    }
}

fun example5() {

    val string = " my last sting upper case"
    string.lastIndexOf('u')

    println(
        string.substring(0, string.lastIndexOf('u') + 1) +
                string.substringAfterLast('u').uppercase()
    )
}


fun example4() {
    val a = "GooD"
    println(a.lowercase())
    println(a.replace("o", "O"))
    println(a.replaceFirst('o', 'O'))
    println(a.substringBefore('o') + "OO" + a.substringAfter('o'))
    println(a.uppercase())

    println("result".substring(1, 4).substring(1, 2)) // esu //

}


fun example3() {
    val line1 = readln()
    val line2 = readln()
    println(line1.equals(line2, ignoreCase = true))
}


fun example2() {
    val example = "Kotlin is a language"
    example.replace(" language", "n island")
    println(example)
}


fun example1() {
    val firstList = readLine()!!.split(' ').map { it }.toMutableList()
    val secondList = readLine()!!.split(' ').map { it }.toMutableList()

    val cap = mutableListOf<String>("Tokyo", "Moscow", "Paris", "Washington", "Beijing")

    val result = firstList + secondList
    println(result.joinToString(separator = ", "))
}

fun reversePrint() {
    val numbers = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    for (index in numbers.lastIndex downTo 0 step 2) {
        println("$index: ${numbers[index]}")
    }
}

fun numberInASet() {
    val size = readln().toInt()
    val numbers: MutableList<Int> = mutableListOf()

    for (i in 0 until size) {

        numbers.add(readln().toInt())

    }

    val numberToCheck = readln().toInt()

    if (numberToCheck in numbers) {
        println("YES")
    } else {
        println("NO")
    }


}

