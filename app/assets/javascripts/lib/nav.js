define(['lib/socket'], function(socket) {
    'use strict';
    socket.on('components', function(event, data) {
        $('.computerCounter').text(data.computerCount);
        $('.jobCounter').text(data.jobCount);
    });
});
