import java.util.Scanner

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    getX()

}
fun getX() {
    val a = scanner.next().toDoubleOrNull() ?: return println(warning)
    val b = scanner.next().toDoubleOrNull() ?: return println(warning)
    val c = scanner.next().toDoubleOrNull() ?: return println(warning)
    println((c - b) / a)
}
val warning = "Invalid arguement"
