define(function(){
  'use strict';

  function DataTable(selector) {
    this.table = this._createTable(selector);
    return this;
  }

  DataTable.prototype = {
    _createTable: function(selector) {
      return $(selector).dataTable({
                      'bJQueryUI': true,
                      'sPaginationType': 'full_numbers',
                      'sDom': '<""l>t<"F"fp>'
                  });
    },
    sortByColumn: function(number) {
      this.table.fnSort([[ number, 'desc' ]]);
    },
    addData: function(data) {
      this.table.fnAddData(data);
    }
  };
  return DataTable;
});
