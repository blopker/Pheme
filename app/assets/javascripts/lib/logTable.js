define(['text!html/logs.html'], function(html) {
    'use strict';

    function LogTable(selector, componentId) {
        this.componentId = componentId || '';
        this.table = this._createTable(selector);
        this.sortByColumn(3);
        this.addLog = this.addLog.bind(this);
        return this;
    }

    LogTable.prototype = {
        _createTable: function(selector) {
            var widget = $(selector);
            var table = widget.append(html).find('.log-table');
            var dataTable = table.dataTable({
                'bJQueryUI': true,
                'sPaginationType': 'full_numbers',
                'sDom': '<""l>t<"F"fp>'
            });
            return dataTable;
        },
        sortByColumn: function(number) {
            this.table.fnSort([
                [number, 'desc']
            ]);
        },
        addLog: function(log) {
            if (log.component.id === this.componentId || this.componentId === '') {
                var d = new Date(log.created);
                var level = '[' + log.logType + ']';
                this.table.fnAddData([level, log.message, log.component.componentName, d.getTime()]);
            }
        }
    };
    return LogTable;
});
