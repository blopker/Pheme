define(['lib/socket', 'datatypes', 'lib/dataTable'], function(socket, datatypes, DataTable) {
  'use strict';
  var buttonList = {};
  var logList;

  function newLogs (event, data) {
      for (var i = 0; i < data.length; i++) {
        addLog(data[i]);
      }
  }

  function addLog (log) {
    var d = new Date(log.created);
    var level = '[' + log.logType + ']';
    logList.addData([level, log.message, log.sourceName, d.getTime()]);
    updateButtons(log.logType, log.logType);
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
    logList = new DataTable('.data-table');
    logList.sortByColumn(3);
  }

  function run () {
    socket.on(datatypes.LOG, newLogs);
    initList();
    updateButtons('ALL', '');
  }

  return {
    run: run
  };

});
