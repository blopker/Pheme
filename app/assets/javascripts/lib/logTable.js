define(['text!html/logs.html'], function(html) {
    'use strict';

    function LogTable(selector, componentId) {
        this.componentId = componentId || '';
        this.table = this._createTable(selector);
        this.sortByColumn(3);
        this.addLog = this.addLog.bind(this);
        var laTable = this.table;
        window.setInterval(function() {
            laTable.fnAdjustColumnSizing();
        }, 2000);
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
                var message = $('<pre>').html(log.message);
                // var message = log.message ;
                this.table.fnAddData([level, message.html(), log.component.componentName, d.getTime()]);
            }
        }
    };
    return LogTable;
});
