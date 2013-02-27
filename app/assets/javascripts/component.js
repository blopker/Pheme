define(['lib/logTable', 'lib/dataGraph', 'lib/socket', 'lib/stat', 'datatype/datatypes'], function(LogTable, DataGraph, socket, Stat, DataTypes) {
  'use strict';

  var stats = {};
  var componentStatsId = $('.stats').data('component');

  function processCount (event, data) {
    if (componentStatsId !== data.component.id) {return;}
    if(!stats[data.id]){
      stats[data.id] = new Stat('.stats', data.counterName, data.count);
    } else {
      stats[data.id].setStat(data.count);
    }
    DataGraph.setValue(data.count);
  }

  function run () {
    socket.on(DataTypes.COUNT, processCount);
    DataGraph.run();
    // Set up log table
    var logTable = new LogTable('.log-table', $('.log-table').data('component'));
  }

  return {
    run: run
  };
});
