define(['lib/pubsub'], function(pubsub){
  'use strict';
  var eventPrefix = 'pheme.';
  // var eventPrefix = '';


  (function connect () {
    var WS;
    if (window['MozWebSocket']) {
      WS = MozWebSocket;
    } else if (window['WebSocket']) {
      WS = WebSocket;
    } else{
      // Running and old browser or RhinoJS
      print('Sockets not supported!');
      return;
    }
    var connection = new WS(WS_URL);
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
