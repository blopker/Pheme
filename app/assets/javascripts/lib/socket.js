define(['lib/pubsub'], function(pubsub) {
    'use strict';
    var eventPrefix = 'pheme.';
    var subscriptions = [];
    var sess = null;


    (function connect() {
        ab.connect(WS_URL,
        // WAMP session was established

        function(session) {
            console.log('Connected!');
            sess = session;
            // subscribe to topics, providing an event handler
            subscriptions.forEach(function(topic) {
                session.subscribe(topic, onMessage);
            });
        },

        // WAMP session is gone

        function(code, reason) {
            console.log('Connection lost (' + reason + ')');
        }, {
            skipSubprotocolCheck: true,
            skipSubprotocolAnnounce: true
        } // Important! Play rejects all subprotocols...
        );
    })();


    function on(type, callback) {
        subscriptions.push(eventPrefix + type);
        // In case we don't have a socket connection yet.
        if (sess !== null) {
            sess.subscribe(eventPrefix + type, onMessage);
        }
        return pubsub.subscribe(eventPrefix + type, callback);
    }

    function onMessage(topic, data) {
        pubsub.publishSync(topic, data);
    }

    function call(method, data) {
        // TODO
    }

    return {
        on: on,
        call: call
    };
});
