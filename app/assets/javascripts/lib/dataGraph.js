define(['text!html/chart.html'], function(html) {
  'use strict';

  function DataGraph (selector, name, value) {
    this.currentValue = value;
    this._init(selector, name);
    this.update = this.update.bind(this);
    this._updateSeries = this._updateSeries.bind(this);
    setInterval(this._updateSeries, 40);
    return this;
  }

  DataGraph.prototype = {
    _data: [],
    _maxValue: 0,
    _init: function(selector, name) {
      var container = $(selector);
      var template = $(html);
      template.find('.chart-title').text(name);
      var chartContainer = template.find('.chart');
      container.append(template);

      // Determine how many data points to keep based on the placeholder's initial size;
      // this gives us a nice high-res plot while avoiding more than one point per pixel.
      this._maxXAxis = chartContainer.outerWidth() / 2 || 300;

      var series = [{
           data: this.currentValue,
           lines: {
               fill: true
           }
       }];

      this._plot = $.plot(chartContainer, series, {
              grid: {
                  borderWidth: 1,
                  minBorderMargin: 20,
                  labelMargin: 10,
                  backgroundColor: {
                      colors: ['#fff', '#e4f4f4']
                  },
                  margin: {
                      top: 8,
                      bottom: 20,
                      left: 20
                  }
              }
          });
    },
    _updateSeries: function() {
      var data = this._data;
      if(data.length){
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

      var series = [{
           data: res,
           lines: {
               fill: true
           }
       }];

      this._plot.setData(series);

      if (this.currentValue > this._maxValue) {
        this._maxValue = this.currentValue;
        this._plot.setupGrid();
      }

      this._plot.draw();
    },
    update: function(value) {
      this.currentValue = value;
    }
  };
  return DataGraph;
});
