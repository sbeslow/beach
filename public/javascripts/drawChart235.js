function drawChart235(statusCounter) {

    var dataVals = [];
    $.each(statusCounter, function(key, value) {
        dataVals.push([key, value]);
    });

    // Radialize the colors
    Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function (color) {
        return {
            radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
            stops: [
                [0, color],
                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
            ]
        };
    });

    // Build the chart
    $('#chart235').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        title: {
            text: 'Swim advisories on beach-days where e-coli was measured at greater than 235 CCE'
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
    var statusCounter = {"No Restrictions":0, "Swim Advisory":0, "Swim Ban":0};
    $.each(data.beachRankings, function(index, bRanking) {
        $.each(bRanking.ecoliMeasurements, function(i, eMeas) {

            if (eMeas.reading >= 235){
                if (eMeas.maxSwimStatus == 'No Restrictions'){
                    statusCounter['No Restrictions']++;
                }
                else if (eMeas.maxSwimStatus == 'Swim Advisory'){
                    statusCounter['Swim Advisory']++;
                }
                else if (eMeas.maxSwimStatus == 'Swim Ban'){
                    statusCounter['Swim Ban']++;
                }
            }
        });
    });
    drawChart235(statusCounter);
});