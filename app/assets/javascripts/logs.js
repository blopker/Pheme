define(['lib/socket', 'lib/list.min'], function(socket) {
  var componentList = {};
  var logList;

  function newLogs (event) {
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

      if (!logList) {
        initList();
      }

      for (var i = 0; i < data.length; i++) {
        addLog(data[i]);
      }
      logList.sort('sort_date', {asc: false});
  }

  function addLog (log) {
    var d = new Date(log.created);
    logList.add({
      type: log.type,
      name: log.sourceName,
      date: d.toLocaleTimeString(),
      sort_date: log.created.toString(),
      message: log.message
    });

    // Update the members list
    if (!componentList[log.sourceName]) {
      componentList[log.sourceName] = log.sourceName;
      var com = $('<li>' + log.sourceName + '</li>');
      com.click(function() {
        logList.filter(function(item) {
          if (item.values().name === log.sourceName) {return true;}
          return false;
        });
      });
      $("#components").append(com);
    }
  }

  function initList () {
    var options = {
      item: '<li class="log">[<span class="type"></span>] <span class="name"></span> (<span class="date"></span>): <span class="message"></span></li>'
    };
    logList = new List('logs', options);

  }

  function run () {
    var connection = socket.connect(window.socketURL);
    connection.onmessage = newLogs;
  }

  return {
    run: run
  };
});
