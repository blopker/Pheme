/**
 * Unicorn Admin Template
 * Diablo9983 -> diablo9983@gmail.com
**/
$(document).ready(function(){


	// === Prepare the chart data ===/
	var sin = [], cos = [];
    for (var i = 0; i < 14; i += 0.5) {
        sin.push([i, Math.sin(i)]);
        cos.push([i, Math.cos(i)]);
    }

	// === Make chart === //
    var plot = $.plot($(".chart"),
           [ { data: sin, label: "sin(x)", color: "#BA1E20"}, { data: cos, label: "cos(x)",color: "#459D1C" } ], {
               series: {
                   lines: { show: true },
                   points: { show: true }
               },
               grid: { hoverable: true, clickable: true },
               yaxis: { min: -1.6, max: 1.6 }
		   });

	// === Point hover in chart === //
    var previousPoint = null;
    $(".chart").bind("plothover", function (event, pos, item) {

        if (item) {
            if (previousPoint != item.dataIndex) {
                previousPoint = item.dataIndex;

                $('#tooltip').fadeOut(200,function(){
					$(this).remove();
				});
                var x = item.datapoint[0].toFixed(2),
					y = item.datapoint[1].toFixed(2);

                unicorn.flot_tooltip(item.pageX, item.pageY,item.series.label + " of " + x + " = " + y);
            }

        } else {
			$('#tooltip').fadeOut(200,function(){
					$(this).remove();
				});
            previousPoint = null;
        }
    });


    $('.data-table').dataTable({
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "sDom": '<""l>t<"F"fp>'
    });

    $('input[type=checkbox],input[type=radio],input[type=file]').uniform();

    // $('select').select2();

    $("span.icon input:checkbox, th input:checkbox").click(function() {
        var checkedStatus = this.checked;
        var checkbox = $(this).parents('.widget-box').find('tr td:first-child input:checkbox');
        checkbox.each(function() {
            this.checked = checkedStatus;
            if (checkedStatus == this.checked) {
                $(this).closest('.checker > span').removeClass('checked');
            }
            if (this.checked) {
                $(this).closest('.checker > span').addClass('checked');
            }
        });
    });
});


unicorn = {
		// === Tooltip for flot charts === //
		flot_tooltip: function(x, y, contents) {

			$('<div id="tooltip">' + contents + '</div>').css( {
				top: y + 5,
				left: x + 5
			}).appendTo("body").fadeIn(200);
		}
}
