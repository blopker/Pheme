define(function() {
  'use strict';

  var currentValue = 0;

  function setCurrentValue (value) {
    currentValue = value;
  }

  function run () {
    chart();
  }

  function chart () {
    var container = $('.chart');

    // Determine how many data points to keep based on the placeholder's initial size;
    // this gives us a nice high-res plot while avoiding more than one point per pixel.

    var maximum = container.outerWidth() / 2 || 300;

    var data = [];

    function makeData() {

        if (data.length) {
            data = data.slice(1);
        }

        while (data.length < maximum) {
            data.push(currentValue);
        }

        // zip the generated y values with the x values

        var res = [];
        for (var i = 0; i < data.length; ++i) {
            res.push([i, data[i]]);
        }

        return res;
    }

   var series = [{
        data: currentValue,
        lines: {
            fill: true
        }
    }];

    var plot = $.plot(container, series, {
        grid: {
            borderWidth: 1,
            minBorderMargin: 20,
            labelMargin: 10,
            backgroundColor: {
                colors: ['#fff', '#e4f4f4']
            },
            hoverable: true,
            mouseActiveRadius: 50,
            margin: {
                top: 8,
                bottom: 20,
                left: 20
            },
            markings: function(axes) {
                var markings = [];
                var xaxis = axes.xaxis;
                for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
                    markings.push({ xaxis: { from: x, to: x + xaxis.tickSize }, color: 'rgba(232, 232, 255, 0.2)' });
                }
                return markings;
            }
        },
        yaxis: {
            min: 0,
            max: 110
        },
        legend: {
            show: true
        }
    });

    setInterval(function updateRandom() {
        series[0].data = makeData();
        plot.setData(series);
        plot.draw();
    }, 40);

  }

  return {
    run: run,
    setValue: setCurrentValue
  };
});
