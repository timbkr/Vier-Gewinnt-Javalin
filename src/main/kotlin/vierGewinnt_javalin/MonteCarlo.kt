package vierGewinnt_javalin

import kotlin.random.Random

class MonteCarlo : MonteCarlo_Interface {
    /* Spiele zufällig bis zum Ende spielen lassen */
    override fun play_randomly(game: VierGewinnt) {
        while (!game.gameOver() && game.counter < 42) {
            val possibleMoves = game.listMoves()
            var index = Random.nextInt(7)
            while (possibleMoves[index] == 0) {
                index = Random.nextInt(7)
            }
            game.makeMove(index)
        }
    }

    /* führe X Simulationen durch und gib den Prozentsatz an gewonnenen Spielen zurück */
    override fun simulate(game: VierGewinnt, simulations: Int, player: Int): Double {
        val ratio: Double
        var callingPlayer = 0
        if (player == 1) callingPlayer = 1
        else if (player == -1) callingPlayer = 0

        var wins = 0
        var losses = 0
        for (x in 0..simulations - 1) {
            val clonedGame = game.clone()
            play_randomly(clonedGame)

            if (callingPlayer == 0) {
                if (clonedGame.isWin(clonedGame.bitBoard[0])) wins++
                else if (clonedGame.isWin(clonedGame.bitBoard[1])) losses++
            } else {
                if (clonedGame.isWin(clonedGame.bitBoard[0])) losses++
                else if (clonedGame.isWin(clonedGame.bitBoard[1])) wins++
            }
        }
        if (losses > 0) ratio = wins.toDouble() / simulations
        else ratio = 1.0
        return ratio
    }

}