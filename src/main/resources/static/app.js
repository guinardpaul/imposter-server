const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/room', (room) => {
        console.log("on subscribe /topic/room")
            let body = JSON.parse(room.body);
            console.log(body)
            showMessage(body.roomId);
        });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function createRoom() {
    stompClient.publish({
          destination: "/app/room.create"
    });
}

function join() {
    stompClient.subscribe('/topic/room/' + $("#roomId").val() + '/**', (roomUpdateMessage) => {
        console.log("on subscribe /topic/room/roomId")
        console.log(roomUpdateMessage.body)
        showMessage(JSON.stringify(JSON.parse(roomUpdateMessage.body), null, 2))
    });
    stompClient.publish({
        destination: "/app/room.join",
        body: JSON.stringify({'roomId': $("#roomId").val(), 'playerName': $("#pseudo").val()})
    });
}

function showMessage(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
    $( "#createRoom" ).click(() => createRoom());
    $( "#join" ).click(() => join());
});