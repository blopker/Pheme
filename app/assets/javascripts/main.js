require(["lib/socket"], function(socket) {
  $(function() {
    var socketURL = window.socketURL || null;
    if(socketURL){
      socket.connect(socketURL);
    }
  });
});

