function drawPie(chartName, statusCounter) {

    var dataVals = [];
    $.each(statusCounter, function(key, value) {
        dataVals.push([key, value]);
    });

    // Build the chart
    $(chartName).highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        colors: ['green', 'yellow', 'red'],
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        title:{
            text:''
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    },
                    connectorColor: 'silver'
                }
            }
        },
        series: [{
            type: 'pie',
            data: dataVals
        }]
    });
}

$.getJSON( "/api/scoreboard", function( data ) {
    var counter235 = {"No Restrictions":0, "Swim Advisory":0, "Swim Ban":0};
    var counter1000 = {"No Restrictions":0, "Swim Advisory":0, "Swim Ban":0};
    $.each(data.beachRankings, function(index, bRanking) {
        $.each(bRanking.ecoliMeasurements, function(i, eMeas) {
        	
        	if (eMeas.reading >= 1000) {
                if (eMeas.maxSwimStatus == 'No Restrictions'){
                	counter1000['No Restrictions']++;
                }
                else if (eMeas.maxSwimStatus == 'Swim Advisory'){
                	counter1000['Swim Advisory']++;
                }
                else if (eMeas.maxSwimStatus == 'Swim Ban'){
                	counter1000['Swim Ban']++;
                }        		
        	}
        	else if (eMeas.reading >= 235) {
                if (eMeas.maxSwimStatus == 'No Restrictions'){
                	counter235['No Restrictions']++;
                }
                else if (eMeas.maxSwimStatus == 'Swim Advisory'){
                	counter235['Swim Advisory']++;
                }
                else if (eMeas.maxSwimStatus == 'Swim Ban'){
                	counter235['Swim Ban']++;
                }            		
        	}
        });
    });
    drawPie("#chart235", counter235);
    drawPie("#chart1000", counter1000);
});