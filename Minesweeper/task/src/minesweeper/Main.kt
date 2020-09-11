package minesweeper

import java.sql.Array
import kotlin.random.Random

object MineSweeper {
    var mineCount = 0
    var columns = 0
    var rows = 0
    val mines = mutableListOf<Pair<Int, Int>>()
    var grid = Array(0) { CharArray(0) { '.' } }
}


fun main() {
    createGrid(10, 9, 9)
    printGrid()


}

fun createGrid(m: Int, c: Int, r: Int = c) {
    MineSweeper.mineCount = m
    MineSweeper.columns = c
    MineSweeper.rows = r
    if (m * 2 > c * r) return println("Too many mines")
    for (i in 0 until m) {
        var pair = newMine(c, r)
        while (pair in MineSweeper.mines) {
            pair = newMine(c, r)
        }
        MineSweeper.mines.add(i, pair)
    }
    MineSweeper.grid = Array(r) { CharArray(c) { '.' } }
    for (mine in MineSweeper.mines) {
        MineSweeper.grid[mine.second][mine.first] = 'X'
    }
}
fun newMine(c: Int, r: Int): Pair<Int, Int> {
    return Random.nextInt(c) to Random.nextInt(r)
}

fun printGrid() {
    for (i in 0 until MineSweeper.rows) {
        for (j in 0 until MineSweeper.columns) {
            print(MineSweeper.grid[i][j])
        }
         println()
    }
}