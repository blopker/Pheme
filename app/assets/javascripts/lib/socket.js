define(['lib/pubsub'], function(pubsub){
  'use strict';
  var eventPrefix = 'pheme.';
  // var eventPrefix = '';


  (function connect () {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var connection = new WS('ws://localhost:9000/socket');
    connection.onmessage = onMessage;
  })();


  function on (type, callback) {
    return pubsub.subscribe(eventPrefix + type, callback);
  }

  function onMessage (message){
    var eventDict = {};
    var data = JSON.parse(message.data);

    for (var i = 0; i < data.length; i++) {
      var evnt = eventPrefix.concat(data[i].dataType);
      var dataList = eventDict[evnt] || [];
      dataList.push(data[i]);
      eventDict[evnt] = dataList;
    }

    for (var event in eventDict){
      if (eventDict.hasOwnProperty(event)) {
        pubsub.publishSync(event, eventDict[event]);
      }
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
