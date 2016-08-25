//表格的对象
var oTable;
var pageflag = "0_0_4";
//for graph query, the string representation
var inputGraph = null;
var graphQueryUrl = "querymodel/graphquery_file";   //repository, file

(function() {
	$(document).ready(function() {	 
		
		loadIndexList('m');			
		
		
		$("#btn_text_query").click(function(){
			var query = $("#query_input").val();
			if(query == undefined || query == null || query.length == 0){
				alert("请输入查询！");
			}
			var index_id = $("#index_list_1  option:selected").val();
			if(index_id == undefined || index_id == null || index_id.length == 0){
				alert("请选择索引！");
			}
			load_query_result("querymodel/textquery","queryResult1",index_id, query);
		});
		
		$("#btn_graph_query").click(function(){
			/*
			var query = $("#query_input").val();
			if(query == undefined || query == null || query.length == 0){
				alert("请输入查询！");
			}*/
			var index_id = $("#index_list_2  option:selected").val();
			if(index_id == undefined || index_id == null || index_id.length == 0){
				alert("请选择索引！");
			}
			if(inputGraph == null){
				alert("请选择一个模型！");
			}
			var query = inputGraph;  //获取查询
			load_query_result(graphQueryUrl,"queryResult2",index_id, query);
		});
		
		$("#btnselectfromDB").click(function() {
			$("#modelsInDB").dynatree({
				title : "从模型库中选择",
				fx : {
					height : "toggle",
					duration : 200
				},
				autoFocus : false, 
				initAjax : {
					url : 'processCatalog/getRootCatalogs',
					data : {
						mode : "funnyMode"
					}
				},
				onActivate : function(node) {
					//获得类别
					catalogId = node.data.key;
					//加载模型
					getTableValues();
				},
				onLazyRead : function(node) {
					node.appendAjax({
						url : "processCatalog/getCatalogs",
						data : {
							catalogId : node.data.key,
							mode : "funnyMode"
						}
					});
				}
			});
			//弹出框显示出来 hide为隐藏
			//$("#modelsInDB").modal('show');
		});
	});
	
	
	$('#modelfileupload').fileupload({
		url:'uploadfiles/modelquery',
		autoUpload: true,
	    dataType: 'json',
	    done: function (e, data) {
	    	$("tr:has(td)").remove();
	        $.each(data.result, function (index, file) {
	        	// 返回需要的值
			    inputGraph = file.inputFilePath;
				graphQueryUrl = "querymodel/graphquery_file";
			    
	            $("#uploaded-files").append(
	            		$('<tr/>')
	            		.append($('<td/>').text(file.fileName))
	            		.append($('<td/>').text(file.fileSize))
	            		.append($('<td/>').text(file.fileType))
	            );
	        }); 
	    },
	    progressall: function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .bar').css(
	            'width',
	            progress + '%'
	        );
		},	
		dropZone: $('#dropzone')
	});
	
	
}).call(this);






/* Init the table */
function getTableValues() {
	$('#modelsTable').empty();
	/*
	 * Initialse DataTables, with no sorting on the 'details' column
	 */
	oTable = $('#modelsTable').dataTable(
			{
				"bSort":false,
				"bFilter" : false,
				"bDestroy" : true,
				"bProcessing" : true,
				"bPaginate": true,
				"bInfo": true, 
				//服务器端分页必需的三个参数
				"sAjaxSource" : "process/getProcesses",
				"bServerSide": true,
				"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
					 oSettings.jqXHR = $.ajax( {
					        "dataType": 'json',
					        "type": "get",
					        "url": sSource,
					        "data": aoData,
					        "success":  fnCallback
					      } );
				},
				"fnServerParams" : function(aoData) {
					aoData.push({
						"name" : "catalogId",
						"value" : catalogId
					});

				},
				//表格的标题，只显示名称，类型
				"aoColumns" : [ {
					"bVisible" : false,
					"bSearchable" : false,
					"sClass" : "center",
					"bSortable" : false
				}, {
					"bSearchable" : false,
					"bVisible" : false,
					"sTitle" : "模型id(隐藏)",
					"sClass" : "center"
				}, {
					"bSearchable" : false,
					"bVisible" : false,
					"sTitle" : "被隐藏的模型路径信息",
					"sClass" : "center"
				}, {
					"sTitle" : "模型名称",
					"sClass" : "center"
				}, {
					"sTitle" : "模型类型",
					"sClass" : "center"
				}, {
					"bVisible" : false,
					"sTitle" : "创建信息",
					"sClass" : "center"
				}, {
					"bVisible" : false,
					"sTitle" : "最近修改信息",
					"sClass" : "center"
				}, {
					"bVisible" : false,
					"sTitle" : "大小",
					"sClass" : "center"
				}, {
					"bVisible" : false,
					"sTitle" : "版本号",
					"sClass" : "center"
				} ],

				"fnDrawCallback" : function() {
					
					$("#modelsTable tbody tr").unbind("click");
					$("#modelsTable tbody tr").click(function(e) {
						
						if ($(this).hasClass('row_selected')) {
							$(this).removeClass('row_selected');
						} else {
							//alert("bbbb");
							oTable.$('tr.row_selected').removeClass('row_selected');
							$(this).addClass('row_selected');
						}
					});
				},

			});
}

//返回被选中的项，用来点击确定后操作
function fnGetSelected(oTableLocal) {
	return oTableLocal.$('tr.row_selected');
}

function confirmQueryByModelInDB(){
	var anSelected = fnGetSelected(oTable);
	//模型id
	var pid = oTable.fnGetData(anSelected[0])[1];
	$
	.ajax({
		cache : true,
		type : "POST",
		url : '',
		data : {
			processId : pid
		},// 
		async : true,
		error : function(request) {
			bootbox.dialog("连接错误", [ {
				label : "Okay",
				"class" : "btn-primary"
			} ]);
		},
		success : function(data) {
			$("#modelsInDB").modal('hide');
		}
	});
	
}
//打开绘制窗口
function openDrawWin(){
	var str = $('#confirmInfo').serialize();
	//解决中文乱码
	str = decodeURIComponent(str,true);
	var arr = new Array();
	arr = str.split('&');
	var processType = arr[0].split('=')[1];
	var src = "";
	var style = "width=600,height=800,location=no,directories=no,toolbar=no,status=no,menubar=no,resizable=no,scrollbars=no";
	if (processType == "BPMN") {
		src = "http://localhost:8080/oryx/editor;bpmn2.0?stencilset=/stencilsets/bpmn2.0/bpmn2.0.json";
	}
	if (processType == "EPC") {
		src = "http://localhost:8080/oryx/editor;epc?stencilset=/stencilsets/epc/epc.json";
	}
	if (processType == "PETRINET") {
		src = "http://localhost:8080/oryx/editor;petrinet?stencilset=/stencilsets/petrinets/petrinet.json";
	}
	if (src == "") {
		alert("please select a type first");
	} else {
		window.open(src, "processsapce绘制模型工具", style);
	}
}
//完成绘制后调用的方法
function setPath(value) {
	var path = value;
	var arr = new Array();
	arr = path.split('&');
	//svgPath = arr[0];
	//jsonPath = arr[1];
	//xmlPath=arr[2];
	inputGraph = arr[2];
	graphQueryUrl = "querymodel/graphquery_file";
	/*
	$.ajax({
		cache : true,
		type : "POST",
		url : '',
		data : {
			xmlPath:xmlPath
		},// 
		async : true,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.state == "SUCCESS") {

			} else {
				bootbox.dialog("连接错误", [ {
					label : "Okay",
					"class" : "btn-primary"
				} ]);
			}
		}
	});*/
}