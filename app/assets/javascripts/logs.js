define(['lib/logTable', 'lib/socket', 'datatype/datatypes'], function(LogTable, socket, DataTypes) {
    'use strict';

    var logTable = new LogTable('.log-holder');
    socket.on('logs', function(topic, data) {
        logTable.addLog(data);
    });

});
