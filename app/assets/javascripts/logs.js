define(['lib/socket', 'lib/list.min'], function(socket) {
  var componentList = {};
  var buttonList = {};
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

    updateComponents(log.sourceName);
    updateButtons(log.type, log.type);
  }

  function updateComponents (name) {
    // Update the members list
    if (!componentList[name]) {
      componentList[name] = name;
      var com = $('<li>' + name + '</li>');
      com.click(function() {
        logList.filter(function(item) {
          if (item.values().name === name) {return true;}
          return false;
        });
      });
      $("#components").append(com);
    }
  }

  function updateButtons (name, filter) {
    if (!buttonList[name]) {
      buttonList[name] = name;
      var button = $("<button class='btn btn-inverse'>" + name + "</button>");
      button.click(function() {
        logList.filter(function(log) {
          if (log.values().type === filter || filter === "") {return true;}
          return false;
        });
      });
      $("#filter-buttons").append(button);
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
    initList();
    updateButtons("ALL", "");
  }

  return {
    run: run
  };
});
