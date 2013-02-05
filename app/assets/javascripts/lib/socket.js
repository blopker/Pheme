define(['lib/pubsub'], function(pubsub){
  // var event_prefix = "pheme.";
  var event_prefix = "";
  var callbacks = {};
  connect();

  function connect () {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var connection = new WS("ws://localhost:9000/socket");
    connection.onmessage = onMessage;
  }

  function on (type, callback) {
    return pubsub.subscribe(event_prefix + type, callback);
  }

  function onMessage (message){
    var eventDict = {};
    var data = JSON.parse(message.data);

    for (var i = 0; i < data.length; i++) {
      var evnt = event_prefix.concat(data[i].dataType);
      var dataList = eventDict[evnt] || [];
      dataList.push(data[i]);
      eventDict[evnt] = dataList;
    }

    for (var event in eventDict){
      pubsub.publishSync(event, eventDict[event]);
    }
  }

  function send (dataType, data) {
    // TODO
  }

  return{
    on: on,
    send: send
  };
});
