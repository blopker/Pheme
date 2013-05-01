define(['lib/logTable', 'lib/stats', 'lib/socket', 'datatype/datatypes'], function(LogTable, Stats, socket, DataTypes) {
    'use strict';

    // Set up stats area
    var componentStatsId = $('.stats-holder').data('component');
    var stats = new Stats();

    // Set up log table
    var logTable = new LogTable('.log-holder', $('.log-holder').data('component'));

    socket.on(componentStatsId, function(event, data) {
        if (data.dataType === DataTypes.LOG) {
            logTable.addLog(data);
        } else {
            stats.addStat(data.id, data.name, data.value);
        }
    });

});
