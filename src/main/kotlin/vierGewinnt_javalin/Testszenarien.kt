package vierGewinnt_javalin

val testsBoards  = arrayOf(
    /* Test 1 */
    /* Die Spiel-Engine kann im nächsten Zug gewinnen (Sichttiefe 1) */
    "[3, 0, 4, 1, 5, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],6",

    /* Test 2 */
    /* Die Spiel-Engine kann im übernächsten Zug gewinnen (Sichttiefe 3) */
    "[3, 0, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],4",

    /* Test 3 */
    /* Die Spiel-Engine kann im überübernächsten Zug gewinnen (Sichttiefe 5)   -> Muss zuerst COL0 dann COL1 setzen */
    "[2, 3, 3, 4, 5, 6, 4, 2, 3, 4, 2, 2, 6, 5, 3, 3, 0, 0, 0, 4, 1, 2, 1, 6, 6, 6, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],18",

    /* Test 4 */
    /* Die Spiel-Engine vereitelt eine unmittelbare Gewinnbedrohung des Gegners (Sichttiefe 2) */
    "[3, 1, 4, 1, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],6",

    /* Test 5 */
    /* Die Spiel-Engine vereitelt eine Drohung, die den Gegner im übernächsten Zug ansonsten einen Gewinn umsetzen lässt (Sichttiefe 4) */
    "[1, 2, 3, 5, 4, 3, 6, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],8"
    )

fun initTest(game: VierGewinnt, board: String){
    game.undoAll()
    createBoard(game,board)
}

/* showBoard() in Browser Konsole eingeben um das aktuelle Board in Zügen zu erhalten (auf Konsole)
* -> den String dann wie oben speichern um neue Testboards zu speichern / erzeugen */
fun createBoard(game: VierGewinnt, board: String) {
    var i = 0
    val counter: Int
    if (board.get(board.length - 2).isDigit()) {
        counter = board.substring(board.length - 2).toInt()
    } else {
        counter = Character.getNumericValue(board.last())
    }
    for (char in board) {
        if (i == counter) break
        if (char.isDigit()) {
            game.makeMove(Character.getNumericValue(char))
            i++
        }
    }


}