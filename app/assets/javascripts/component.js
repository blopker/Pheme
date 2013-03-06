define(['lib/logTable', 'lib/stats',  'lib/socket', 'datatype/datatypes'], function(LogTable, Stats, socket, DataTypes) {
  'use strict';

  // function processStat (event, data) {
  //   if (componentStatsId !== data.component.id) {return;}
  //   stats.updateStat(data.counterName, data.count);
  //   // if(!stats[data.id]){
  //   //   stats[data.id] = new Stat('.stats', data.counterName, data.count);
  //   // } else {
  //   //   stats[data.id].setStat(data.count);
  //   // }
  //   // DataGraph.setValue(data.count);
  // }

  function run () {
    // Set up stats area
    var componentStatsId = $('.stats-holder').data('component');
    var stats = new Stats();

    // Set up log table
    var logTable = new LogTable('.log-holder', $('.log-holder').data('component'));

    socket.on(DataTypes.COUNT, function(event, data) {
      if (componentStatsId !== data.component.id) {return;}
      stats.addStat(data.id, data.counterName, data.count);
    });

    socket.on(DataTypes.LOG, logTable.addLog);
  }

  return {
    run: run
  };
});
