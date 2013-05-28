define(['lib/logTable', 'stats/stats', 'lib/socket', 'datatype/datatypes'], function(LogTable, Stats, socket, DataTypes) {
    'use strict';

    // Set up stats area
    var componentStatsId = $('.stats-holder').data('component');
    var stats = null;

    // Set up log table
    var logTable = new LogTable('.log-holder', $('.log-holder').data('component'));

    socket.on(componentStatsId, function(event, data) {
        if (data.dataType === DataTypes.LOG) {
            logTable.addLog(data);
        } else {
            if (stats === null) {stats = new Stats();}
            stats.addStat(data.id, data.name, data.value);
        }
    });

});
