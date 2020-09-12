package minesweeper

import java.util.*
import kotlin.random.Random



object MineSweeper {
    var mineCount = 0
    var columns = 0
    var rows = 0
    val mines = mutableSetOf<Pair<Int, Int>>()
    val marked = mutableSetOf<Pair<Int, Int>>()
    val unexplored = mutableSetOf<Pair<Int, Int>>()
    var grid = Array(0) { CharArray(0) { '.' } }

}
val input = Scanner(System.`in`)
var exit = false

fun main() {
    println("How many mines do you want on the field?")
    val mine = input.nextInt()
    createGrid(mine, 9, 9)
    while (!exit) {
        printGrid()
        requestAction()
        checkWin()
    }
}

fun createGrid(m: Int, c: Int, r: Int = c) {
    MineSweeper.mineCount = m
    MineSweeper.columns = c
    MineSweeper.rows = r
    if (m * 11 > c * r * 10) return println("Too many mines")
    MineSweeper.grid = Array(r + 3) { CharArray(c + 3) { '.' } }
    for (i in 0..r + 2) {
        for (j in 0..c + 2) {
            MineSweeper.grid[i][j] = when {
                j == 1 || j == c + 2 -> '|'
                i == 1 || i == r + 2 -> '-'
                i == 0 && j == 0 -> ' '
                i == 0 -> (j - 1).toString().last()
                j == 0 -> (i - 1).toString().last()
                else -> {MineSweeper.unexplored.add(i - 1 to j - 1); '.' }
            }
        }
    }
}

fun firstExplore(i: Int, j: Int) {
    MineSweeper.mines.add(i to j)
    for (mine in 1 .. MineSweeper.mineCount) {
        var pair = newMine(MineSweeper.columns, MineSweeper.rows)
        while (pair in MineSweeper.mines) {
            pair = newMine(MineSweeper.columns, MineSweeper.rows)
        }
        MineSweeper.mines.add( pair)
    }
    MineSweeper.mines.remove(i to j)
    return explore(i, j)
}

fun newMine(c: Int, r: Int): Pair<Int, Int> {
    return Random.nextInt(c) + 1 to Random.nextInt(r) + 1
}

fun requestAction() {
    println("Set/unset mine marks or claim a cell as free:")
    val x = input.nextInt()
    val y = input.nextInt()
    val action = input.next()
    if (x !in 1..9 || y !in 1..9) {
        println("Out of bounds")
        return requestAction()
    }
    if (action in listOf("mine", "mark", "set")) {
        markMine(x, y)
    } else if (action in listOf("claim", "free", "explore")) {
        explore(x, y)
    } else {
        println("Unknown Action")
        return requestAction()
    }

}
fun printGrid() {
    for (j in 0 .. MineSweeper.rows + 2) {
        for (i in 0 .. MineSweeper.columns + 2) {
            print(MineSweeper.grid[j][i])
        }
         println()
    }
}
fun gameOver() {
    for (mine in MineSweeper.mines) {
        MineSweeper.grid[mine.second + 1][mine.first + 1] = 'X'
    }
    printGrid()
    exit = true
    println("You stepped on a mine and failed!")
}

fun checkWin() {
    if (exit) return
    if (MineSweeper.mines == MineSweeper.marked || MineSweeper.mines == MineSweeper.unexplored) {
        printGrid()
        println("Congratulations! You found all the mines.")
        exit = true
    }

}

fun explore(i: Int, j: Int, action: Boolean = true) {
    when {
        MineSweeper.grid[j + 1][i + 1] == '*' -> {
            if (action) {
                println("Cell set as mine, you must mark it again before exploring:")
                return requestAction()
            }
            MineSweeper.grid[j + 1][i + 1] = '.'
            explore(i, j)
        }
        MineSweeper.mines.isEmpty() -> return firstExplore(i, j)
        MineSweeper.grid[j + 1][i + 1] == '.' -> {
            if (i to j  in MineSweeper.mines) return gameOver()
            if (checkNeighbours(i, j) == 0) {
                MineSweeper.grid[j + 1][i + 1] = '/'
                MineSweeper.unexplored.remove(i to j)
                getNeighbours(i, j).map { explore(it.first, it.second, false) }
                return
            }
            MineSweeper.unexplored.remove(i to j)
            MineSweeper.grid[j + 1][i + 1] = checkNeighbours(i, j).toString()[0]
        }
        action ->  { println("Cell already explored."); return requestAction() }
    }
}

fun checkNeighbours(i:Int, j: Int): Int {
    if (i to j in MineSweeper.mines) return 0
    val neighbours = getNeighbours(i, j)
    return neighbours.count { it in MineSweeper.mines}
}

fun getNeighbours(i:Int, j: Int): List<Pair<Int, Int>> {
    return listOf(i-1 to j-1, i-1 to j, i-1 to j+1,
            i to j-1, i to j+1, i+1 to j-1, i+1 to j,
            i+1 to j+1).filter {
        it.first in 1 .. MineSweeper.columns &&
                it.second in 1 .. MineSweeper.rows
    }
}

fun markMine(x: Int, y: Int) {
    when (MineSweeper.grid[y+1][x+1]) {
        '*' -> {
            MineSweeper.marked.remove(x to y )
            MineSweeper.grid[y+1][x+1] = '.'
            return
        }
        '.' -> {
            MineSweeper.marked.add(x to y )
            MineSweeper.grid[y+1][x+1] = '*'
            return
        }
        '/' -> println {"This cell has already been cleared!"}
        else ->  println {"There is a number here!"}
    }
    return requestAction()
}

