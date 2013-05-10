define(['stats/stats', 'lib/socket'], function(Stats, socket) {
    'use strict';

    // Set up stats area
    var stats = new Stats();

    socket.on('system', function(event, data) {
        for(var s in data){
            if (data.hasOwnProperty(s)) {
                stats.addStat(s, s, data[s]);
            }
        }
    });

});
