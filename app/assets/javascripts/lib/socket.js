define(function(){
  var componentList = [];
  var logList = $('#logs');

  function connect (socketURL) {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var chatSocket = new WS(socketURL);

    chatSocket.onmessage = function(event) {
        var data = JSON.parse(event.data);

        // Handle errors
        if(data.error) {
          chatSocket.close();
          $("#onError span").text(data.error);
          $("#onError").show();
          return;
        } else {
          $("#onLog").show();
        }

        for (var i = 0; i < data.length; i++) {
          addLog(data[i]);
        }
      };
  }

  function addLog (log) {
    // Create the message element
    var el = $('<div class="log"><span class="type"></span><span class="name"></span><p class="date"></p><p class="message"></p></div>');
    $("span.name", el).text(log.sourceName);
    $("span.type", el).text("["+log.type+"]");
    var d = new Date(log.created);
    $("p.date", el).text("("+d.toLocaleTimeString()+"): ");
    $("p.message", el).text(log.message);
    $(el).addClass(log.type);
    logList.prepend(el);

    // Update the members list
    if ($.inArray(log.sourceName, componentList) === -1) {
      componentList.push(log.sourceName);
      $("#components").append('<li>' + log.sourceName + '</li>');
    }
  }

  return{
    connect: connect
  };
});
