define(['lib/socket', 'lib/list.min'], function(socket) {
  'use strict';
  var componentList = {};
  var buttonList = {};
  var logList;

  function newLogs (event, data) {

      for (var i = 0; i < data.length; i++) {
        addLog(data[i]);
      }
      logList.sort('sortDate', {asc: false});
  }

  function addLog (log) {
    var d = new Date(log.created);
    logList.add({
      type: log.logType,
      name: log.sourceName,
      date: d.toLocaleTimeString(),
      sortDate: log.created.toString(),
      message: log.message
    });

    updateComponents(log.sourceName);
    updateButtons(log.logType, log.logType);
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
      $('#components').append(com);
    }
  }

  function updateButtons (name, filter) {
    if (!buttonList[name]) {
      buttonList[name] = name;
      var button = $('<button class="btn btn-inverse">' + name + '</button>');
      button.click(function() {
        logList.filter(function(log) {
          if (log.values().type === filter || filter === '') {return true;}
          return false;
        });
      });
      $('#filter-buttons').append(button);
    }
  }

  function initList () {
    var options = {
      item: '<li class="log">[<span class="type"></span>] <span class="name"></span> (<span class="date"></span>): <span class="message"></span></li>'
    };
    logList = new List('logs', options);

  }

  function run () {
    socket.on('log', newLogs);
    initList();
    updateButtons('ALL', '');
  }

  return {
    run: run
  };
});
