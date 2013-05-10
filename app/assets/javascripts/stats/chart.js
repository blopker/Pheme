define(function() {
    'use strict';

    function Chart(selector, id, name, value) {
        this.currentValue = value;
        this.container = this._createContainer(selector, id);
        this._plot = this._createPlot(this.container);
        this.update = this.update.bind(this);
        this._updateSeries = this._updateSeries.bind(this);
        this._data = [];
        setInterval(this._updateSeries, 40);
        return this;
    }

    Chart.prototype = {
        _createContainer: function(selector, id) {
            var parent = $(selector);
            var container = $('<div>');
            container.attr('id', id);
            parent.append(container);
            return container;
        },
        _createPlot: function(container) {
            // Determine how many data points to keep based on the placeholder's initial size;
            // this gives us a nice high-res plot while avoiding more than one point per pixel.
            this._maxXAxis = container.outerWidth() / 2 || 300;

            var series = [this.currentValue];

            // setup plot
            var options = {
                series: {
                    shadowSize: 0
                }, // drawing is faster without shadows
                xaxis: {
                    show: false
                },
                yaxis: {
                    show: false
                }

            };
            var plot = $.plot(container, series, options);
            return plot;
        },
        _updateSeries: function() {
            var data = this._data;
            if (data.length) {
                data = data.slice(1);
            }

            while (data.length < this._maxXAxis) {
                data.push(this.currentValue);
            }
            this._data = data;

            // zip the generated y values with the x values
            var res = [];
            for (var i = 0; i < data.length; ++i) {
                res.push([i, data[i]]);
            }

            this._plot.setData([res]);

            this._plot.draw();

            // Keep the line visible
            var yaxis = this._plot.getAxes().yaxis;
            if (this.currentValue > yaxis.max || this.currentValue < yaxis.min) {
                this._plot.setupGrid();
            }
        },
        update: function(value) {
            this.currentValue = value;
        },
        hide: function() {
            this.container.hide();
        },
        show: function() {
            this.container.show();
            this._plot.setupGrid();
        }
    };
    return Chart;
});
