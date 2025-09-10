package vierGewinnt_javalin

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.javalin.websocket.WsContext
import java.util.concurrent.ConcurrentHashMap
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class Server {
    init {

    }
}


@Serializable
data class WS_msg(
    val type: String,
    val param: String
)
/* SpielEngine erzeugen (tiefe,simulationsanzahl) */
val ai_AlphaBeta_Negamax = AlphaBeta_Negamax(5, 100)

fun main(args: Array<String>) {
    //Server()
    val userList = ConcurrentHashMap<WsContext,VierGewinnt>()

    val app = Javalin.create { config ->
        config.addStaticFiles("/public", Location.CLASSPATH)
    }.apply { ws("move"){
            ws ->
        ws.onConnect { ctx ->
            println("WebSocket Connected - ID: " + ctx.sessionId)
            //Zeit bis zum Disconnect des Websockets ohne Eingabe in Millisec (30min = 1.800.000
            ctx.session.idleTimeout = 1800000
            // initialize User/Session + add session + game to userList
            val game = VierGewinnt()
            newGame(ctx,game)
            userList.put(ctx, game)
        }
        ws.onClose { ctx ->
            println("WebSocket Closed - ID: " + ctx.sessionId)
            //delete User + Game from Userlist
            userList.remove(ctx)
        }
        ws.onMessage{ ctx ->
            //get Zug / Spielfeld from Client
            // process move -> ändere Game in gamelist + send new Spielfeld back to client
            val data = ctx.message()
           // println("Received JSON: $data")
            val obj = Json.decodeFromString<WS_msg>(data)
            //println("obj = "+obj)

            val input = obj.param
            val game = userList.get(ctx)

            if(game!= null)
            when(obj.type){
                "move" -> {
                    println("MOVE")
                    makeMoveAndSendHTMLString(ctx,game,input.toInt())
                }
                "aiMove" -> {
                    aiMoveAndSendHTMLString(ctx,game)
                }
                "undoMove" -> {
                    undoMove(ctx,game)
                }
                "newGame" -> {
                    newGame(ctx,game)
                }
                "startPlayer" -> {
                    startPlayer(ctx, game)
                }
                "testMode" -> {
                    testMode(ctx,game)
                }
                "testID" -> {
                    testID(ctx,game,input.toInt())
                }else -> {
                    println("WHEN: "+obj.type)
                }
            }else println("game = NULL -> ctx(session) not found in userlist")
        }
    } }
        .start(getHerokuAssignedPort())

    //BEISPIEL ---------------------------------------------------------
    /*val data = WS_msg("move", "3")
    val string = Json.encodeToString(data)
    println(string)

    val obj = Json.decodeFromString<WS_msg>(string)
    println(obj) */
//  ------------------------------------------------------
}

fun makeMoveAndSendHTMLString(ctx: WsContext ,game: VierGewinnt, input: Int){
    if ((!game.isWin(game.bitBoard[0]) && !game.isWin(game.bitBoard[1]))) {
        val possibleMoves = game.listMoves()
        if (possibleMoves[input] == 1) { //wenn gültiger Zug
            if (game.counter == 0 && !game.computerStarts) {
                game.playerStarts = true
            }
            game.makeMove(input)
            ctx.send(toHTMLString(game))
            if (!game.gameOver() && game.counter < 42 && !game.testMode) // im Testmodus kein automatischer Gegenzug
                if ((game.computerStarts && game.counter and 1 == 0) || (game.counter and 1 == 1 && game.playerStarts)) {
                    /* antworte mit Gegenzug wenn der Mensch nicht für den Computer gezogen hat (per UndoMove)*/
                    game.makeMove(ai_AlphaBeta_Negamax.bestMove(game))
                }
            ctx.send(toHTMLString(game))
        } else {
            println("unpossible move")
        }
    } else {
        println("GameOver")
    }
}

fun aiMoveAndSendHTMLString(ctx: WsContext ,game: VierGewinnt){
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

        ctx.send(toHTMLString(game))
    } else {
        println("AI: no move possible - restart game!")
    }
}

fun undoMove(ctx: WsContext, game: VierGewinnt){
    if (game.counter > 0) {
        game.undoMove()
        ctx.send(toHTMLString(game))
    }
}
fun newGame(ctx: WsContext, game: VierGewinnt){
    game.computerStarts = false
    game.playerStarts = false
    game.timeLastMove = 0
    game.undoAll()
    game.testMode = false
    game.activeTest = 0
    ctx.send(toHTMLString(game))
}
fun startPlayer(ctx: WsContext,game: VierGewinnt){
    game.computerStarts = false
    game.playerStarts = true
    ctx.send(toHTMLString(game))
}
fun testMode(ctx: WsContext, game: VierGewinnt){
    game.testMode = true
    runAllTests(game, ai_AlphaBeta_Negamax)
    game.timeLastMove = 0
    game.undoAll()
    ctx.send(toHTMLString(game))
}
fun testID(ctx: WsContext,game: VierGewinnt, input: Int){
    val testNR = input
    resetForTest(game, testNR)
    initTest(game, testsBoards[testNR-1])
    ctx.send(toHTMLString(game))
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