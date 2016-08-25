/**
 * author: motianyu
 */
var pageflag = "1_1_1";

(function() {
	$(document).ready(function(){
		upload_log_file();
		instance_database_tree();
	});
}).call(this);

/* Custom functions that help in getting remote data and drawing a chart to a div */
/* 响应后台数据并渲染到页面的可扩展方法*/

function createNewLineChart(divId) {
    var chart = {
        options: {
            chart: {
                renderTo: divId,
                type: 'area', 
                zoomType: 'x'
            }
            
        }
    };
    chart = jQuery.extend(true, {}, getBaseChart(), chart);
    chart.init(chart.options);
    return chart;
}

function getBaseChart() {

    var baseChart = {
        highchart: null,
        defaults: {

            chart: {
                renderTo: null,
                shadow: true,
                borderColor: '#ebba95',
                borderWidth: 2,
                defaultSeriesType: 'column',
                width: 400,
                height: 250
            },
            credits: {
                enabled: false
            },
            exporting: {
                enabled: true
            },
            title: {
                text: null,
                align: 'center',
                style: {
                    color: '#3E576F',
                    fontWeight: 'bold',
                    fontSize: '16px',
                    fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                }
            },
            subtitle: {
                text: '单击下方颜色块可控制相应项目的显示'
            },
            xAxis: {
                categories: [],
                title: {
                    text: null,
                    style: {
                        color: '#3E576F',
                        fontWeight: 'bold',
                        fontSize: '12px',
                        fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                    }
                },
                labels: {
                    enabled: false
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: null,
                    style: {
                        color: '#3E576F',
                        fontWeight: 'bold',
                        fontSize: '12px',
                        fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                    }
                },
                labels: {
                    style: {
                        color: '#3E576F',
                        fontSize: '12px',
                        fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                    }
                },
                plotLines: [{
                    value: 0,
                    width: 1
                }]
            },
            tooltip: {
                crosshairs: true,
                formatter: function() {
                    return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y;
                }
            },
            legend: {
                layout: 'horizontal',
                backgroundColor: '#ffffff',
                align: 'center',
                verticalAlign: 'bottom',
                borderWidth: 1,
                shadow: true,
                style: {
                    color: '#3366cc',
                    fontWeight: 'bold',
                    fontSize: '9px',
                    fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                }
            },
            plotOptions: {
                series: {
                    marker: {
                        enabled: false
                    }
                }
            },
            series: []

        },

        // here you'll merge the defaults with the object options
        // 合并默认设置
        init: function(options) {
            this.highchart = jQuery.extend({}, this.defaults, options);
        },

        create: function() {
            new Highcharts.Chart(this.highchart);
        }

    };
    return baseChart;
}//function end

function getRemoteDataDrawChart(url, linechart) {
	
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var real = url + '?logId='+ node.data.key+ '&_=';
    $.ajax({
        url: real,
        dataType: 'json',
        success: function(data) {

            var categories = data.categories;
            var title = data.title;
            var yTitle = data.yAxisTitle;
            var xTitle = data.xAxisTitle;
            var divId =  data.divId;

            //populate the lineChart options (highchart)
            //注入数据
            linechart.highchart.xAxis.categories = categories;
            linechart.highchart.title.text = title;
            linechart.highchart.yAxis.title.text = yTitle;
            linechart.highchart.xAxis.title.text = xTitle;
            linechart.highchart.chart.renderTo = divId;

            $.each(data.series, function(i, seriesItem) {
                var series = {
                    data: []
                };
                series.name = seriesItem.name;
                series.color = seriesItem.color;

                $.each(seriesItem.data, function(j, seriesItemData) {
                    series.data.push(parseFloat(seriesItemData));
                });
              
                linechart.highchart.series[i] = series;
            });
            
            //draw the chart
            //绘制图表
            linechart.create();
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(xhr.status);
            alert(thrownError);
        },
        cache: false
    });
} //function end

function createNewPieChart(divId) {
    var chart = {
        options: {
            chart: {
                renderTo: divId
            }           
        }
    };
    chart = jQuery.extend(true, {}, getBasePieChart(), chart);
    chart.init(chart.options);
    return chart;
}

//Make monochrome colors and set them as default for all pies
Highcharts.getOptions().plotOptions.pie.colors = (function () {
    var colors = [],
        base = Highcharts.getOptions().colors[0],
        i

    for (i = 0; i < 10; i++) {
        // Start out with a darkened base color (negative brighten), and end
        // up with a much brighter color
        colors.push(Highcharts.Color(base).brighten((i - 3) / 20).get());
    }
    return colors;
}());

function getBasePieChart() {

    var baseChart = {
        highchart: null,
        defaults: {

            chart: {
                renderTo: null,
                plotBackgroundColor: null,  
                plotBorderWidth: null,  
                plotShadow: false  
            },
            credits: {
                enabled: false
            },
            exporting: {
                enabled: true
            },
            title: {
                text: null,
                align: 'center',
                style: {
                    color: '#3E576F',
                    fontWeight: 'bold',
                    fontSize: '16px',
                    fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                }
            },
            subtitle: {
                text: '鼠标在颜色块悬停可查看相关信息'
            },
            tooltip: {
        	    pointFormat: '<b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        enabled: false,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                        }
                    }
                }
            },
            series: []

        },

        // here you'll merge the defaults with the object options
        // 合并默认设置
        init: function(options) {
            this.highchart = jQuery.extend({}, this.defaults, options);
        },

        create: function() {
            new Highcharts.Chart(this.highchart);
        }

    };
    return baseChart;
}//function end

function getRemoteDataDrawPieChart(url, piechart) {
	
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var real = url + '?logId='+ node.data.key+ '&_=';
    $.ajax({
        url: real,
        dataType: 'json',
        success: function(data) {

            var title = data.title;
            var divId =  data.divId;
            var array = new Array();

            //populate the lineChart options (highchart)
            //注入数据
            piechart.highchart.title.text = title;
            piechart.highchart.chart.renderTo = divId;

            $.each(data.data, function(i, seriesItem) {
                var series = {
                	type:"pie",
                    data: array
                };
                
                var tmp = new Array();  
                tmp[0] = seriesItem.name;  
                tmp[1] = seriesItem.frequency;  
                array[i] = tmp;
  
                piechart.highchart.series[i] = series;
            });
            
            //draw the chart
            //绘制图表
            piechart.create();
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(xhr.status);
            alert(thrownError);
        },
        cache: false
    });
} //function end