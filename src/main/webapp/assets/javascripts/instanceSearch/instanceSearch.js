var pageflag = "0_1_4";
function buttonRelease() {
	
	var indexType = $("#index_type_selected").val();
	var query = document.getElementById("queryGoal").value;
	$.ajax({
		cache : true,
		type : "GET",
		url : 'instanceIndex/query_check',
		data : {
			type : indexType,
			q : query
		},
		async : true,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				var childTextHintTemp = document.getElementById("textHintTemp");
				if (childTextHintTemp != null) {
					document.getElementById("textHint").removeChild(childTextHintTemp);
				}
				document.getElementById("btnSearch").removeAttribute("disabled");
			}
			else {
				document.getElementById("textHint").innerHTML = "<p id ='textHintTemp' style='color: #FF0000;'>输入格式有误！</p>";
				$("#btnSearch").attr("disabled", true);
			}
			
		}
	});
	
}

function getHelpInfo() {
	var indexType = $("#index_type_selected").val();
	var child1, child2;
	
	if(indexType == "length") {
		child1 = document.getElementById("temp1");
		child2 = document.getElementById("temp2");
		if (child1 != null) {
			document.getElementById("hintLog").removeChild(child1);
			document.getElementById("hintLog").removeChild(child2);
		}
		document.getElementById("hintLog").innerHTML="<p id='temp1'>"+internationalNames["instanceSearch.resultAlert"]+"</p>" +
				"<p id='temp2'>"+internationalNames["instanceSearch.resultLength"]+"</p>";
	}
	else if(indexType == "duration") {
		child1 = document.getElementById("temp1");
		child2 = document.getElementById("temp2");
		if (child1 != null) {
			document.getElementById("hintLog").removeChild(child1);
			document.getElementById("hintLog").removeChild(child2);
		}
		document.getElementById("hintLog").innerHTML="<p id='temp1'>"+internationalNames["instanceSearch.resultAlert"]+"</p>" +
				"<p id='temp2'>"+internationalNames["instanceSearch.resulttimeConsuming"]+"</p>";
	}
	else if(indexType == "caseEvent") {
		child1 = document.getElementById("temp1");
		child2 = document.getElementById("temp2");
		if (child1 != null) {
			document.getElementById("hintLog").removeChild(child1);
			document.getElementById("hintLog").removeChild(child2);
		}
		document.getElementById("hintLog").innerHTML="<p id='temp1'>"+internationalNames["instanceSearch.resultAlert"]+"</p>" +
				"<p id='temp2'>"+internationalNames["instanceSearch.resultActivity"]+"</p>";
	}
	else if(indexType == "adjacentEvent") {
		child1 = document.getElementById("temp1");
		child2 = document.getElementById("temp2");
		if (child1 != null) {
			document.getElementById("hintLog").removeChild(child1);
			document.getElementById("hintLog").removeChild(child2);
		}
		document.getElementById("hintLog").innerHTML="<p id='temp1'>"+internationalNames["instanceSearch.resultAlert"]+"</p>" +
				"<p id='temp2'>"+internationalNames["instanceSearch.AdjacentActivity"]+"</p>";
	}
}

function getIndexType() {
	var indexType = $("#index_type_selected").val();
	if(indexType == undefined || indexType == "") {
		alert("Please choose a index type!");
	}
	
	var query = document.getElementById("queryGoal").value;
	if (indexType == "length") {
		$.ajax({
			cache : true,
			type : "POST",
			url : 'instanceIndex/length_query',
			data : {
				q : query
			},
			async : true,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				//alert("bingo");
				load_instance_database(indexType);
				$("#btnSearch").attr("disabled", true);
			}
		});
	}
	else if (indexType == "duration") {
		$.ajax({
			cache : true,
			type : "POST",
			url : 'instanceIndex/duration_query',
			data : {
				q : query
			},
			async : true,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				//alert("bingo");
				load_instance_database(indexType);
				$("#btnSearch").attr("disabled", true);
			}
		});
	}
	else if (indexType == "caseEvent") {
		$.ajax({
			cache : true,
			type : "POST",
			url : 'instanceIndex/event_query',
			data : {
				q : query,
			},
			async : true,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				//alert("bingo");
				load_instance_database(indexType);
				$("#btnSearch").attr("disabled", true);
			}
		});
	}
	else if (indexType == "adjacentEvent") {
		$.ajax({
			cache : true,
			type : "POST",
			url : 'instanceIndex/adjacentEvent_query',
			data : {
				q : query,
			},
			async : true,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				//alert("bingo");
				load_instance_database(indexType);
				$("#btnSearch").attr("disabled", true);
			}
		});
	}
}

function showLengthIndexInfo() {
	document.getElementById("hintLog").innerHTML="<p>"+internationalNames["instanceSearch.resultAlert"]+"</p><p>"+internationalNames["instanceSearch.resultLength"]+"</p>";
}

function load_instance_database(str){
	var nCloneTh = document.createElement('th');
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#queryResultTable').dataTable({
		"bDestroy" : true,
		"bProcessing" : true,
		"sAjaxSource" : "instanceIndex/result_show",
		"fnServerParams" : function(aoData) {
			aoData.push({
				"name" : "logId",
				"value" : str
			});
		},
		"sServerMethod" : "GET",
		"aoColumns" : [ {
			"bVisible" : false,
			"bSearchable" : false,
			"sTitle" : "日志ID",
			"sClass" : "center"
		}, {
			"sTitle" : "日志名称",
			"sClass" : "center"
		}, {
			"sTitle" : "所属目录",
			"sClass" : "center"
		}, {
			"sTitle" : "创建时间",
			"sClass" : "center"
		}],
		"fnInitComplete" : function() {
			$('#instanceTable thead tr').each(function() {
				this.insertBefore(nCloneTh, this.childNodes[0]);
			});
		},
		"fnCreatedRow" : function(nRow, aData, iDataIndex) {
		},
		"fnDrawCallback" : function() {
			$('#newtable tbody tr').each(
				function() {
					this.childNodes[0].Abbr = "defined";
				});
			$('.details').each(
				function() {
					this.parentNode.childNodes[0].Abbr = "defined";
				});
			$('#instanceTable tbody tr').each(
				function() {
					if (this.childNodes[0].Abbr != "defined") {
					
						this.insertBefore(nCloneTd.cloneNode(true),
								this.childNodes[0]);
						this.childNodes[0].Abbr = "defined";
					}
					
			});
			$("#instanceTable tbody tr").unbind("click");
			$("#instanceTable tbody tr").click(function(e) {
				 if ( $(this).hasClass('row_selected') ) {
				 $(this).removeClass('row_selected'); } else {
				 $(this).addClass('row_selected'); }
			});
			$("#instanceTable tbody td").dblclick(
				function(e) {
					processIdforrename = oTable.fnGetData(oTable
							.fnGetPosition(this)[0], 0);
				});

			$('.editable1111').editable({
				url : 'process/renameProcess',
				type : 'text',
				pk : 1,
				mode : 'inline',
				toggle : 'dblclick',
				name : 'newname',
				// value:newname 传给后台的值
				title : 'Change process name',
				params : function(params) {
					// originally params contain pk, name and value
					params.processId = processIdforrename;
					return params;
				},
				validate: function(value) {
				    if($.trim(value) == '') {
				        return 'This field is required';
				    }
				},
				success : function(response, newValue) {
					if (response.state == 'FAILED'){
						bootbox.dialog(response.message, [ {
							label : "Okay",
							"class" : "btn-primary"
						} ]);
						$(this).text(value);
					}
				}

			});
		},

	});
}

