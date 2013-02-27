define(function() {
  'use strict';

  function Stat(selectorUL, statName, stat) {
    this.stat = stat || 0;
    this.statName = statName;
    this._init(selectorUL);
    return this;
  }

  Stat.prototype = {
    _init: function(selector) {
      this.statNode = $(this._html(this.statName));
      $(selector).append(this.statNode);
      this.setStat(this.stat);
    },
    _html: function(name) {
      return '<li><h4 class="stat-value"></h4><span class="stat-name">'+name+'</span></li>';
    },
    setStat: function(statInt) {
      this.statNode.children('.stat-value').text(statInt);
    }
  };
  return Stat;
});
