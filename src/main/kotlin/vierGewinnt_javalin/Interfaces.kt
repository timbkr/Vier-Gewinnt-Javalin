package vierGewinnt_javalin

interface VierGewinnt_Interface {
    var bitBoard: LongArray
    var height: IntArray
    var counter: Int
    var moves: IntArray

    fun makeMove(col: Int)
    fun undoMove()
    fun undoAll()
    fun isWin(bitboard: Long): Boolean
    fun gameOver(): Boolean
    fun listMoves(): IntArray
    override fun toString(): String
    fun clone(): VierGewinnt_Interface
}

interface AB_negamax_Interface {
    var gespeicherterZug: Int
    var spielerAmZug: Int
    var mcts: MonteCarlo

    fun bestMove(game: VierGewinnt): Int
    fun negamax_AB(game: VierGewinnt, tiefe: Int, alpha: Int, beta: Int, player: Int): Int
    fun bewerten(game: VierGewinnt, tiefe: Int, spielerAmZug: Int): Int
}

interface MonteCarlo_Interface {
    fun play_randomly(game: VierGewinnt)
    fun simulate(game: VierGewinnt, simulations: Int, player: Int): Double
}