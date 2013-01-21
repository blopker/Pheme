define(function(){
  var componentList = [];
  var logList = $('#logs');

  function connect (socketURL) {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var connection = new WS(socketURL);
    return connection;
  }

  return{
    connect: connect
  };
});
