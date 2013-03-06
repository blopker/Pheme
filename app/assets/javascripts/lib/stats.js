define(['text!html/stats.html', 'lib/stat', 'lib/dataGraph'], function(html, Stat, DataGraph) {
  'use strict';

  var statHolder = '.stats';
  var chartHolder = '.chart-holder';

  function Stats () {
    this._init();
    this.addStat = this.addStat.bind(this);
    return this;
  }

  Stats.prototype = {
    _stats: {},
    _charts: {},
    _init: function() {
      var template = $(html);
      $('.stats-holder').append(template);
    },
    addStat: function(id, name, value) {
      if(!this._stats[id]){
        this._stats[id] = new Stat(statHolder, name, value);
        this._charts[id] = new DataGraph(chartHolder, name, value);
      } else {
        this._stats[id].setStat(value);
        this._charts[id].update(value);
      }
    }
  };

  return Stats;

});
