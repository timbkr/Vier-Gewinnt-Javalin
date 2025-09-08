package vierGewinnt_javalin

import kotlin.random.Random
import kotlin.system.measureTimeMillis

/* Alpha Beta - Negamax Variante incl verwendung von MonteCarloTreeSearch zur Stellungsbewertung */
class AlphaBeta_Negamax(var depthWanted: Int, var simulations: Int) : AB_negamax_Interface {

    override var gespeicherterZug: Int = -1
    override var spielerAmZug = 0
    override var mcts = MonteCarlo()

    override fun bestMove(game: VierGewinnt): Int {
        game.timeLastMove = measureTimeMillis {
            spielerAmZug = game.counter and 1
            if (spielerAmZug == 0) spielerAmZug = 1
            else spielerAmZug = -1
            val clonedGame = game.clone()
            negamax_AB(clonedGame, depthWanted, Int.MIN_VALUE + 1, Int.MAX_VALUE - 1, spielerAmZug) //Min_Value+1 Da sonst beim umkehren kein Positiver Wert entsteht
            if (gespeicherterZug == -1) println("Something went wrong! check for valid gamestate!")
        }
        return gespeicherterZug
    }

    override fun negamax_AB(game: VierGewinnt, tiefe: Int, alpha: Int, beta: Int, player: Int): Int {
        if (tiefe == 0 || game.counter >= 42 || game.gameOver()) {
            return bewerten(game, tiefe, player)
        }
        var maxWert = alpha
        val possibleMoves = movesToARList(game)
        while (possibleMoves.isNotEmpty()) {
            val zug = possibleMoves.get(0)
            game.makeMove(zug)
            possibleMoves.removeAt(0)
            val wert = -negamax_AB(game, tiefe - 1, -beta, -maxWert, -player)
            game.undoMove()
            if (wert > maxWert) {
                maxWert = wert
                if (tiefe == depthWanted) {
                    gespeicherterZug = zug
                }
                if (maxWert >= beta) break
            }
        }
        return maxWert
    }

    override fun bewerten(game: VierGewinnt, tiefe: Int, spielerAmZug: Int): Int {
        if (game.isWin(game.bitBoard[0])) {
            if (spielerAmZug == 1)
                return 20000 + (tiefe * 50) + Random.nextInt(50)
            else
                return -20000 + (tiefe * -50) + Random.nextInt(50)

        } else if (game.isWin(game.bitBoard[1])) {
            if (spielerAmZug == 1)
                return -20000 + (tiefe * -50) + Random.nextInt(50)
            else
                return 20000 + (tiefe * 50) + Random.nextInt(50)
        }
        return -(mcts.simulate(game, simulations, spielerAmZug) * 10000).toInt()
    }

    /* mögliche Züge in Arraylist packen um sie nacheinander entfernen zu können */
    fun movesToARList(game: VierGewinnt): ArrayList<Int> {
        val possibleMoves = ArrayList<Int>()
        val ar = game.listMoves()
        for (i in 0..ar.size - 1) {
            if (ar[i] == 1) possibleMoves.add(i)
        }
        return possibleMoves
    }
}