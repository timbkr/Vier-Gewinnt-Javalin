package vierGewinnt_javalin

/* HTML Code (mit aktuellem Spielboard) für GUI erzeugen */
fun toHTMLString(game: VierGewinnt): String {
    var win = false
    var winPOS: ArrayList<Pair<Int, Int>> = ArrayList()
    // Check ob ein Spieler gewonnen hat
    if (game.isWin(game.bitBoard[0])) {
        win = true
        winPOS = isWinHTML(game.bitBoard[0])
    } else if (game.isWin(game.bitBoard[1])) {
        win = true
        winPOS = isWinHTML(game.bitBoard[1])
    }
    var str = "<div class=\"game\">  \n" +
            "       <table class=\"board\">"
    // Board zeichnen
    for (row in 5 downTo 0) {
        str += "<tr class=\"row" + row + "\"> \n"
        for (col in 0..6) {
            if (win && winPOS.contains(Pair(col, row))) { //Färbe Gewinnpositionen grün
                str += "<td class=\"col" + col + "\" style=\"background-color:green\" onclick=\"sendMove(" + col + ")\">"
            } else
                str += "<td class=\"col" + col + "\" onclick=\"sendMove(" + col + ")\">"
            //Wenn an dieser Stelle Stein dann adde
            if (game.bitBoard[0] and (1L shl (row + (7 * col))) != 0L) {
                str += "<div class=\"playerX\"></div>"
            } else if (game.bitBoard[1] and (1L shl (row + (7 * col))) != 0L) {
                str += "<div class=\"playerO\"></div>"
            }
            str += "</td>\n"
        }
        str += "</tr>"
    }
    str += "</table>  \n" +
            "</div>\n"
    // Tool- bzw Infoboxen zeichnen
    if (!game.testMode && !game.computerStarts && !game.playerStarts) {
        str += "<div class=\"infoBox startBox \" style=\"background-color:darkblue\">\n<h1>Start:</h1>" +
                "<button class=\"start\" onclick=\"sendRequestGET('startPlayer')\">Player</button>" +
                "<button class=\"start\" style=\"background-color:crimson\" onclick=\"sendRequestGET('aiMove')\">Computer</button>" +
                "<button class=\"start\" style=\"background-color:darkgoldenrod \" onclick=\"sendRequestGET('testMode')\">TestMode</button>" +
                "</div>"
    }// Test 1-5 Stylen wenn aktiv
    else if (game.testMode) {
        str += "<div class=\"infoBox startBox \" style=\"background-color:darkblue\">\n<h1></h1>"
        if (game.activeTest == 1) str += "<button class=\"start testBTN\" style=\"background-color:darkgoldenrod\" onclick=\"sendRequestGET('test1')\">Test1</button>"
        else str += "<button class=\"start testBTN\" onclick=\"sendRequestGET('testID','1')\">Test1</button>"
        if (game.activeTest == 2) str += "<button class=\"start testBTN\" style=\"background-color:darkgoldenrod\" onclick=\"sendRequestGET('test2')\">Test2</button>"
        else str += "<button class=\"start testBTN\" onclick=\"sendRequestGET('testID','2')\">Test2</button>"
        if (game.activeTest == 3) str += "<button class=\"start testBTN\" style=\"background-color:darkgoldenrod\" onclick=\"sendRequestGET('test3')\">Test3</button>"
        else str += "<button class=\"start testBTN\" onclick=\"sendRequestGET('testID','3')\">Test3</button>"
        if (game.activeTest == 4) str += "<button class=\"start testBTN\" style=\"background-color:darkgoldenrod\" onclick=\"sendRequestGET('test4')\">Test4</button>"
        else str += "<button class=\"start testBTN\" onclick=\"sendRequestGET('testID','4')\">Test4</button>"
        if (game.activeTest == 5) str += "<button class=\"start testBTN\" style=\"background-color:darkgoldenrod\" onclick=\"sendRequestGET('test5')\">Test5</button></div>"
        else str += "<button class=\"start testBTN\" onclick=\"sendRequestGET('testID','5')\">Test5</button></div>"
    } else { // Wenn testmode nicht aktiv
        str += "<div class=\"infoBox\" style=\"background-color:darkblue\">\n<h1 class=\"zeit\">Zugberechnung: " + game.timeLastMove + " Millisek</h1></div>"
    }
    if (win && !game.testMode) {
        if (game.isWin(game.bitBoard[0])) {
            str += "<div class=\"infoBox\" style=\"background-color: green\">\n<h1>GameOver - Winner:</h1><h1 class=\"nextMoveX\">X</h1>"
        } else if (game.isWin(game.bitBoard[1])) {
            str += "<div class=\"infoBox\" style=\"background-color: green\">\n<h1>GameOver - Winner:</h1><h1 class=\"nextMoveO\">O</h1>"
        }
    }// Unterste infoBOX:
    else if (!game.listMoves().contains(1)) { // Falls kein Zug mehr möglich
        str += "<div class=\"infoBox\" style=\"background-color: navy\">\n<h1>-> Spiel Unentschieden <-</h1>"
    } else if (game.testMode) {
        if (game.counter and 1 == 1) {
            str += "<div class=\"infoBox\">\n<h1>Next:</h1><h1 class=\"nextMoveO\"> </h1> <h1 style=\"padding-left:15px\">LastMove: " + game.timeLastMove + "ms</h1>"
        } else {
            str += "<div class=\"infoBox\">\n<h1>Next:</h1><h1 class=\"nextMoveX\"> </h1> <h1 style=\"padding-left:15px\">LastMove: " + game.timeLastMove + "ms</h1>"
        }
    } else if (game.computerStarts) {
        str += "<div class=\"infoBox\">\n<h1>Spielerfarbe:</h1><h1 class=\"nextMoveO\"> </h1>"
    } else if (game.playerStarts) {
        str += "<div class=\"infoBox\">\n<h1>Spielerfarbe:</h1><h1 class=\"nextMoveX\"> </h1>"
    } else str += "<div class=\"infoBox\">\n<h1>Let's Go!</h1>"
    return str
}

/* Speichere Gewinnpositionen in Liste um sie in GUI grafisch hervorheben zu können */
fun isWinHTML(bitboard: Long): ArrayList<Pair<Int, Int>> {
    val winPOS: ArrayList<Pair<Int, Int>> = ArrayList() // <POS,COL>
    val directions = intArrayOf(1, 7, 6, 8)
    var bb: Long
    val dir: Int
    for (direction in directions) {
        bb = bitboard and (bitboard shr direction);
        if ((bb and (bb shr (2 * direction))) != 0L) {
            dir = direction
            var col1: Int = 0
            var row1: Int = 0
            for (row in 5 downTo 0) {
                for (col in 0..6) {
                    if (bb and (bb shr (2 * direction)) and (1L shl (row + (7 * col))) != 0L) {
                        col1 = col
                        row1 = row
                        winPOS.add(Pair(col1, row1))
                    }
                }
            }
            for (i in 1..3) {
                if (dir == 1) {
                    winPOS.add(Pair(col1, row1 + i))
                } else if (dir == 6) {
                    winPOS.add(Pair(col1 + i, row1 - i))
                } else if (dir == 7) {
                    winPOS.add(Pair(col1 + i, row1))
                } else if (dir == 8) {
                    winPOS.add(Pair(col1 + i, row1 + i))
                }
            }
            break
        }
    }
    return winPOS
}

