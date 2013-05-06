define(['text!html/stats.html', 'stats/stat', 'stats/chart'], function(html, Stat, Chart) {
    'use strict';

    var statHolder = '.stats';
    var chartHolder = '.chart';

    function Stats() {
        this._init();
        this._stats = {};
        this._charts = {};
        this.addStat = this.addStat.bind(this);
        return this;
    }

    Stats.prototype = {
        _init: function() {
            var template = $(html);
            $('.stats-holder').append(template);
        },
        _setStat: function(id) {

            for(var stat in this._stats){
                if(this._stats.hasOwnProperty(stat)){
                    this._stats[stat].deactivate();
                }
            }

            for(var chart in this._charts){
                if(this._charts.hasOwnProperty(chart)){
                    this._charts[chart].hide();
                }
            }

            this._stats[id].activate();
            this._charts[id].show();
            $('.stats-title').html(this._stats[id].statName);
        },
        addStat: function(id, name, value) {
            if (!this._stats[id]) {
                this._stats[id] = new Stat(statHolder, id, name, value);
                var self = this;
                this._stats[id].statNode.click(function() {
                    self._setStat(id);
                });
                this._charts[id] = new Chart(chartHolder, id, name, value);
                this._setStat(id);
            } else {
                this._stats[id].setStat(value);
                this._charts[id].update(value);
            }
        }
    };

    return Stats;

});
