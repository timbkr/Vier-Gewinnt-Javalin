const gameContainer = document.querySelector(".gameContainer");

function sendMove(column) {
    send_WS("move",column)
}

function sendRequestGET(path = '', query = '') {
    // console.log("send_WS: "+path + " | "+ query)
    send_WS(path,query)
}

function send_WS(type, param){
    let data = {
        type: type,
        param: param.toString()
    }
    ws.send(JSON.stringify(data))
}

//Websocket Client Code
let wsAddress = "wss://"
if(location.hostname === "localhost") wsAddress = "ws://"
let ws
connectWebsocket()

function connectWebsocket(){
    //Establish the WebSocket connection and set up event handlers
    ws = new WebSocket(wsAddress + location.hostname + ":" + location.port + "/move");
    ws.onmessage = msg => processMSG(msg);
    ws.onclose = () => {
        alert("WebSocket connection closed - try to open new connection..");
        connectWebsocket()
    }
}

function processMSG(msg){
    // console.log(msg)
    gameContainer.innerHTML = msg.data
}