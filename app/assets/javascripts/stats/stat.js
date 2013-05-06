define(['text!html/stat.html'], function(html) {
    'use strict';

    function Stat(selectorUL, id, statName, stat) {
        this.stat = stat || 0;
        this.statName = statName;
        this._init(selectorUL);
        return this;
    }

    Stat.prototype = {
        _init: function(selector) {
            this.statNode = this._html(this.statName);
            $(selector).append(this.statNode);
            this.setStat(this.stat);
        },
        _html: function(name) {
            var stat = $(html);
            stat.children('.stat-name').text(name);
            return stat;
        },
        setStat: function(statInt) {
            this.statNode.children('.stat-value').text(statInt);
        },
        activate: function() {
            this.deactivate();
            this.statNode.addClass('active');
        },
        deactivate: function() {
            this.statNode.removeClass('active');
        }
    };
    return Stat;
});
