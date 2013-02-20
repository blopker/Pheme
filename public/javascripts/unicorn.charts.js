/**
 * Unicorn Admin Template
 * Diablo9983 -> diablo9983@gmail.com
**/
$(document).ready(function(){

    if ($('.chart')[0]) {
        chart();
	// // === Prepare the chart data ===/
	// var sin = [], cos = [];
 //    for (var i = 0; i < 14; i += 0.5) {
 //        sin.push([i, Math.sin(i)]);
 //        cos.push([i, Math.cos(i)]);
 //    }

	// // === Make chart === //
 //    var plot = $.plot($(".chart"),
 //           [ { data: sin, label: "sin(x)", color: "#BA1E20"}, { data: cos, label: "cos(x)",color: "#459D1C" } ], {
 //               series: {
 //                   lines: { show: true },
 //                   points: { show: true }
 //               },
 //               grid: { hoverable: true, clickable: true },
 //               yaxis: { min: -1.6, max: 1.6 }
	// 	   });

	// // === Point hover in chart === //
 //    var previousPoint = null;
 //    $(".chart").bind("plothover", function (event, pos, item) {

 //        if (item) {
 //            if (previousPoint != item.dataIndex) {
 //                previousPoint = item.dataIndex;

 //                $('#tooltip').fadeOut(200,function(){
	// 				$(this).remove();
	// 			});
 //                var x = item.datapoint[0].toFixed(2),
	// 				y = item.datapoint[1].toFixed(2);

 //                unicorn.flot_tooltip(item.pageX, item.pageY,item.series.label + " of " + x + " = " + y);
 //            }

 //        } else {
	// 		$('#tooltip').fadeOut(200,function(){
	// 				$(this).remove();
	// 			});
 //            previousPoint = null;
 //        }
 //    });
    }

});

function chart () {
        var container = $(".chart");

        // Determine how many data points to keep based on the placeholder's initial size;
        // this gives us a nice high-res plot while avoiding more than one point per pixel.

        var maximum = container.outerWidth() / 2 || 300;

        //

        var data = [];

        function getRandomData() {

            if (data.length) {
                data = data.slice(1);
            }

            while (data.length < maximum) {
                var previous = data.length ? data[data.length - 1] : 50;
                var y = previous + Math.random() * 10 - 5;
                data.push(y < 0 ? 0 : y > 100 ? 100 : y);
            }

            // zip the generated y values with the x values

            var res = [];
            for (var i = 0; i < data.length; ++i) {
                res.push([i, data[i]]);
            }

            return res;
        }

        //

       var series = [{
            data: getRandomData(),
            lines: {
                fill: true
            }
        }];

        //

        var plot = $.plot(container, series, {
            grid: {
                borderWidth: 1,
                minBorderMargin: 20,
                labelMargin: 10,
                backgroundColor: {
                    colors: ["#fff", "#e4f4f4"]
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
                        markings.push({ xaxis: { from: x, to: x + xaxis.tickSize }, color: "rgba(232, 232, 255, 0.2)" });
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

        // Create the demo X and Y axis labels

        var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
            .text("Response Time (ms)")
            .appendTo(container);

        // Since CSS transforms use the top-left corner of the label as the transform origin,
        // we need to center the y-axis label by shifting it down by half its width.
        // Subtract 20 to factor the chart's bottom margin into the centering.

        yaxisLabel.css("margin-top", yaxisLabel.width() / 2 - 20);

        // Update the random dataset at 25FPS for a smoothly-animating chart

        setInterval(function updateRandom() {
            series[0].data = getRandomData();
            plot.setData(series);
            plot.draw();
        }, 40);

}

unicorn = {
		// === Tooltip for flot charts === //
		flot_tooltip: function(x, y, contents) {

			$('<div id="tooltip">' + contents + '</div>').css( {
				top: y + 5,
				left: x + 5
			}).appendTo("body").fadeIn(200);
		}
}
