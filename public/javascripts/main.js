$(function() {
  var socketURL = window.socketURL || null;
  if(socketURL === null){
    return;
  }

  var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
  var chatSocket = new WS(socketURL);

  var receiveEvent = function(event) {
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
      var el = $('<div class="log"><span></span><p></p></div>');
      $("span", el).text(data.user);
      $("p", el).text(data.message);
      $(el).addClass(data.kind);
      if(data.user == '@username') $(el).addClass('me');
      $('#logs').append(el);

      // Update the members list
      $("#components").html('');
      $(data.members).each(function() {
        $("#components").append('<li>' + this + '</li>');
      });
    };

  chatSocket.onmessage = receiveEvent;

});
