import java.util.Scanner

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    getX()

}
fun getX() {
    val a = scanner.next().toDoubleOrNull()?: return println("Invalid aguement")
    val b = scanner.next().toDoubleOrNull()?: return println("Invalid aguement")
    val c = scanner.next().toDoubleOrNull()?: return println("Invalid aguement")
    println((c - b)/a)
}