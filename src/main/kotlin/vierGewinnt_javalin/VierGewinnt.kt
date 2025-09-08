package vierGewinnt_javalin

import kotlin.random.Random.Default.nextInt
import kotlin.system.measureTimeMillis


class VierGewinnt : VierGewinnt_Interface {
    override var bitBoard: LongArray = LongArray(2)
    override var height = intArrayOf(0, 7, 14, 21, 28, 35, 42)
    override var counter = 0
    override var moves = IntArray(42)

    var computerStarts = false
    var playerStarts = false
    var testMode = false
    var activeTest = 0
    var timeLastMove = 0L

    override fun makeMove(col: Int) {
        val move = 1L shl height[col]++
        bitBoard[counter and 1] = bitBoard[counter and 1] xor move
        moves[counter++] = col
    }

    override fun undoMove() {
        val col = moves[--counter]
        val move = 1L shl --height[col]
        bitBoard[counter and 1] = bitBoard[counter and 1] xor move
    }

    override fun undoAll() {
        while (counter > 0) undoMove()
    }

    override fun isWin(bitboard: Long): Boolean {
        val directions = intArrayOf(1, 7, 6, 8)
        var bb: Long
        for (direction in directions) {
            bb = bitboard and (bitboard shr direction)
            if ((bb and (bb shr (2 * direction))) != 0L) return true
        }
        return false
    }

    override fun gameOver(): Boolean {
        if (isWin(bitBoard[0]) || isWin(bitBoard[1])) {
            return true
        }
        return false
    }

    override fun listMoves(): IntArray {
        val movesAR = IntArray(7)
        val TOP = 0b1000000100000010000001000000100000010000001000000L
        for (col in 0..6) {
            if (TOP and (1L shl height[col]) == 0L) movesAR[col] = 1
        }
        return movesAR
    }

    fun isValidMove(col: Int, listMovesAR: IntArray): Boolean {
        if (listMovesAR[col] == 1) return true
        return false
    }

    fun rndMove() {
        timeLastMove = measureTimeMillis {
            val possibleMoves = listMoves()
            if (!isWin(bitBoard[0]) && !isWin(bitBoard[1]) && possibleMoves.contains(1) && counter < 42) {
                var pos = nextInt(7)
                while (possibleMoves.contains(1) && possibleMoves[pos] == 0) {
                    pos = nextInt(7)
                }
                makeMove((pos))
            }
        }
        println("Zeit für Zugberechnung: $timeLastMove")
    }

    override fun toString(): String {
        var str = ""
        for (row in 5 downTo 0) {
            str += "["
            for (col in 0..6) {
                if (bitBoard[0] and (1L shl (row + (7 * col))) != 0L) str += "X "
                else if (bitBoard[1] and (1L shl (row + (7 * col))) != 0L) str += "O "
                else str += ". "
            }
            str += "]\n"
        }
        str += " 1 2 3 4 5 6 7"
        return str
    }

    override fun clone(): VierGewinnt {
        val clonedGame = VierGewinnt()
        clonedGame.bitBoard = bitBoard.clone()
        clonedGame.height = height.clone()
        clonedGame.counter = counter
        clonedGame.height = height.clone()
        clonedGame.moves = moves.clone()
        return clonedGame
    }
}

