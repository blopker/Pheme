define(['lib/socket', 'datatype/datatypes'], function(socket, datatypes) {
  'use strict';

  function LogTable(selector, componentId) {
    this.componentId = componentId || '';
    this.table = this._createTable(selector);
    this.sortByColumn(3);
    return this;
  }

  LogTable.prototype = {
    _createTable: function(selector) {
      var dataTable = $(selector).dataTable({
                      'bJQueryUI': true,
                      'sPaginationType': 'full_numbers',
                      'sDom': '<""l>t<"F"fp>'
                  });
      this._setListener();
      return dataTable;
    },
    sortByColumn: function(number) {
      this.table.fnSort([[ number, 'desc' ]]);
    },
    _setListener: function() {
      socket.on(datatypes.LOG, this._addLog.bind(this));
    },
    _addLog: function(event, log) {
      if (log.component.id === this.componentId || this.componentId === '') {
        var d = new Date(log.created);
        var level = '[' + log.logType + ']';
        this.table.fnAddData([level, log.message, log.component.componentName, d.getTime()]);
      }
    }
  };
  return LogTable;
});
