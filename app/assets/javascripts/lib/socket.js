define(function(){
  function connect (socketURL) {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var chatSocket = new WS(socketURL);

    var componentList = [];

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

        // Create the message element
        var el = $('<div class="log"><span class="type"></span><span class="name"></span><p class="date"></p><p class="message"></p></div>');
        $("span.name", el).text(data.sourceName);
        $("span.type", el).text("["+data.type+"]");
        var d = new Date(data.created);
        $("p.date", el).text("("+d.toLocaleTimeString()+"): ");
        $("p.message", el).text(data.message);
        $(el).addClass(data.type);
        $('#logs').prepend(el);

        // Update the members list
        if ($.inArray(data.sourceName, componentList) === -1) {
          componentList.push(data.sourceName);
          $("#components").append('<li>' + data.sourceName + '</li>');
        }
      };
  }

  return{
    connect: connect
  };
});
