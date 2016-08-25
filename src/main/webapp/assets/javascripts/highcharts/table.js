/**
 * author: motianyu
 */
function statisticInstanceTable(){
	var div = document.getElementById("instanceTableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("instanceTableOperator").innerHTML="<table id='instanceTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#instanceTable').dataTable({
		"bDestroy" : true,
		"bProcessing" : true,
		"sAjaxSource" : "statisticTable/instanceStatistic",
		"fnServerParams" : function(aoData) {
			aoData.push({
				"name" : "logId",
				"value" : node.data.key
			});
		},
		"sServerMethod" : "GET",
		"aoColumns" : [ {
			"sTitle" : internationalNames["InstanceLogOverViewTable.instanceId"],
			"sClass" : "center"
		},{
			"sTitle" : internationalNames["InstanceLogOverViewTable.ActivityNum"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["InstanceLogOverViewTable.startTime"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["InstanceLogOverViewTable.endTime"],
			"sClass" : "center"
		},  {
			"sTitle" : internationalNames["InstanceLogOverViewTable.avergeConsuming"],
			"sClass" : "center"
		},  {
			"sTitle" : internationalNames["InstanceLogOverViewTable.medianConsuming"],
			"sClass" : "center"
		},  {
			"sTitle" : internationalNames["InstanceLogOverViewTable.totalConsuming"],
			"sClass" : "center"
		}],
	});
}

function statisticActivityTable(){
	var div = document.getElementById("activityTableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("activityTableOperator").innerHTML="<table id='activityTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#activityTable').dataTable({
		"bDestroy" : true,
		"bProcessing" : true,
		"sAjaxSource" : "statisticTable/activityStatistic",
		"fnServerParams" : function(aoData) {
			aoData.push({
				"name" : "logId",
				"value" : node.data.key
			});
		},
		"sServerMethod" : "GET",
		"aoColumns" : [ {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.acttivityName"],
			"sClass" : "center"
		},{										
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.occurFrequency"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.relativefrequency"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.averageConsuming"],
			"sClass" : "center"
		},  {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.totalCosuming"],
			"sClass" : "center"
		}],
	});
}

function statisticResourceTable(){
	var div = document.getElementById("resourceTableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("resourceTableOperator").innerHTML="<table id='resourceTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#resourceTable').dataTable({
		"bDestroy" : true,
		"bProcessing" : true,
		"sAjaxSource" : "statisticTable/resourceStatistic",
		"fnServerParams" : function(aoData) {
			aoData.push({
				"name" : "logId",
				"value" : node.data.key
			});
		},
		"sServerMethod" : "GET",
		"aoColumns" : [ {
			"sTitle" : internationalNames["ResouceTable.resouceName"],
			"sClass" : "center"
		},{
			"sTitle" : internationalNames["ResouceTable.occurFrequency"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["ResouceTable.relativeFrequency"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["ResouceTable.averageConsuming"],
			"sClass" : "center"
		},  {
			"sTitle" : internationalNames["ResouceTable.totalCosuming"],
			"sClass" : "center"
		} ],
	});
}

function statisticVariantsTable(){
	var div = document.getElementById("variantsTableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("variantsTableOperator").innerHTML="<table id='variantsTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
	var node = $("#activity_tree").dynatree("getActiveNode");
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#variantsTable').dataTable({
		"bDestroy" : true,
		"bProcessing" : true,
		"sAjaxSource" : "statisticTable/variantsStatistic",
		"fnServerParams" : function(aoData) {
			aoData.push({
				"name" : "caseId",
				"value" : node.data.key
			});
		},
		"sServerMethod" : "GET",
		"aoColumns" : [ {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.acttivityName"],
			"sClass" : "center"
		},{
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.excuteResources"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.startTime"],
			"sClass" : "center"
		}, {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.endTime"],
			"sClass" : "center"
		},  {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.totalCosuming"],
			"sClass" : "center"
		},  {
			"sTitle" : internationalNames["InstanceActivityStatisticsTables.waitingTime"],
			"sClass" : "center"
		}],
	});
}