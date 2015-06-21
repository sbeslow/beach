var data;

d3.json("/api/scoreboard", function(error, json) {
	if (error) return console.warn(error);
	data = json;
	drawPieChart();
	
});

function drawPieChart(){
	
	var salesData = [ {
		label : "No Restrictions",
		value: 67,
		color : "#3366CC"
	}, {
		label : "Swim Advisory",
		value: 13,
		color : "#DC3912"
	}, {
		label : "Swim Ban",
		value: 20,
		color : "#FF9900"
	}];


	Donut3D.draw("salesDonut", salesData, 150, 150, 130, 100, 30, 0.4);
	

}