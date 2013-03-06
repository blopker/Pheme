define(['lib/logTable', 'lib/socket', 'datatype/dataTypes'], function(LogTable, socket, DataTypes) {
  'use strict';

  function run () {
    var logTable = new LogTable('.log-holder');
    socket.on(DataTypes.LOG, logTable.addLog);
  }

  return {
    run: run
  };

});
