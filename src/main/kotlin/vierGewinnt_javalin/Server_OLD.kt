package vierGewinnt_javalin

import io.javalin.Javalin
import io.javalin.http.Context

/*

class Server {
    init {
        val game = VierGewinnt()
        /* SpielEngine erzeugen (tiefe,simulationsanzahl) */
        val ai_AlphaBeta_Negamax = AlphaBeta_Negamax(5, 100)

        val app = Javalin.create { config ->
            config.addStaticFiles("/public")
        }.start(getHerokuAssignedPort())

        app.get("/move") { ctx: Context ->
            val input = ctx.queryParam("col")!!.toInt()
            if ((!game.isWin(game.bitBoard[0]) && !game.isWin(game.bitBoard[1]))) {
                val possibleMoves = game.listMoves()
                if (possibleMoves[input] == 1) { //wenn gültiger Zug
                    if (game.counter == 0 && !game.computerStarts) {
                        game.playerStarts = true
                    }
                    game.makeMove(input)
                    if (!game.gameOver() && game.counter < 42 && !game.testMode) // im Testmodus kein automatischer Gegenzug
                        if ((game.computerStarts && game.counter and 1 == 0) || (game.counter and 1 == 1 && game.playerStarts)) {
                            /* antworte mit Gegenzug wenn der Mensch nicht für den Computer gezogen hat (per UndoMove)*/
                            game.makeMove(ai_AlphaBeta_Negamax.bestMove(game))
                        }
                    ctx.result(toHTMLString(game))
                } else {
                    println("unpossible move")
                }
            } else {
                println("GameOver")
            }
        }

        app.get("/aiMove") { ctx: Context ->
            if (!game.gameOver() && game.counter < 42) {
                /* Damit man auch z.B. für den Computer ersten Zug zurücknehmen und selbst bestimmen kann */
                if (game.counter == 0 && !game.playerStarts) {
                    game.computerStarts = true
                }
                game.makeMove(ai_AlphaBeta_Negamax.bestMove(game))
                if (!game.gameOver() && game.counter < 42 && !game.testMode) { // im Testmodus kein automatischer Gegenzug
                    if ((game.computerStarts && game.counter and 1 == 0) || (game.counter and 1 == 1 && game.playerStarts)) {
                        /* antworte mit Gegenzug wenn der Mensch den Computer für sich ziehen lässt */
                        game.makeMove(ai_AlphaBeta_Negamax.bestMove(game))
                    }
                }

                ctx.result(toHTMLString(game))
            } else {
                println("AI: no move possible - restart game!")
            }
        }

        app.get("/newGame") { ctx: Context ->
            game.computerStarts = false
            game.playerStarts = false
            game.timeLastMove = 0
            game.undoAll()
            game.testMode = false
            game.activeTest = 0
            ctx.result(toHTMLString(game))
        }

        app.get("/undoMove") { ctx: Context ->
            if (game.counter > 0) {
                game.undoMove()
                ctx.result(toHTMLString(game))
            }
        }

        app.get("/loadGame") { ctx: Context ->
            ctx.result(toHTMLString(game))
        }

        app.get("/testMode") { ctx: Context ->
            game.testMode = true
            runAllTests(game, ai_AlphaBeta_Negamax)
            game.timeLastMove = 0
            game.undoAll()
            ctx.result(toHTMLString(game))
        }

        app.get("/test/:id") { ctx: Context ->
            val testNR = ctx.pathParam("id").toInt()
            resetForTest(game, testNR)
            initTest(game, testsBoards[testNR-1])
            ctx.result(toHTMLString(game))
        }

        app.get("/startPlayer") { ctx: Context ->
            game.computerStarts = false
            game.playerStarts = true
            ctx.result(toHTMLString(game))
        }
    }

}

fun main(args: Array<String>) {
    Server()
}

fun resetForTest(game: VierGewinnt, testNR: Int) {
    game.undoAll()
    game.testMode = true
    game.activeTest = testNR
}

fun getHerokuAssignedPort(): Int {
    val herokuPort = System.getenv("PORT");
    if (herokuPort != null) {
        return Integer.parseInt(herokuPort);
    }
    return 8080;
}
*/