define(['lib/logTable'], function(LogTable) {
  'use strict';

  function run () {
    var logTable = new LogTable('.data-table', component.id);
  }

  return {
    run: run
  };
});
