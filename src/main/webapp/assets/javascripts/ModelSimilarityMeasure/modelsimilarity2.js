//表格的对象
var oTable2;


(function() {
	$(document).ready(function() {	 			
		
		$('#modelfileupload2').click(function(){
			$('#progress2 .bar').css(
		            'width',
		              '0%'
		    );
			$('#uploaded-files2 h1').html("");
		});
		
		$('#modelfileupload2').fileupload({
	    	url:'uploadfiles/modelsimilarity',
	    	autoUpload: true,
	    	
	        dataType: 'json',
	        
	        formData : {
	        	index : 2
	        },
	        
	        done: function (e, data) {
	        	
	        	clearParams2();
	        	
	        	var filepath = data.result['filepath'];
	        	uploadfilepath2=filepath;
	        	$.ajax({
				      cache: true,
				      type: "POST",
				      data: {
				    	  pnmlFile:uploadfilepath2,
				      },
				      url:'process/pnmlToPng',
				      async: false,
				      beforeSend:function(){
				      },
				      error: function(request) {
				          alert("Connection error");
				      },
				      success: function(data) {
				      	if(data.state == "SUCCESS"){
				      		var pngFilePath = data.pngFile.replace("\\", "/");
				      		$('#img2').attr("src",pngFilePath);
				      	} else {
				      		alert(data.message);
				      	}
				      	
				      }
				  });
	        	
	        },
	        
	        progressall: function (e, data) {
		        var progress = parseInt(data.loaded / data.total * 100, 10);
		        $('#progress2 .bar').css(
		            'width',
		            progress + '%'
		        );
		        if(data.loaded/data.total==1){
		        	$('#uploaded-files2 h1').html("上传完毕");
		        }
	   		},
	   		
			dropZone: $('#dropzone')
	    });
		
		$("#btnselectfromDB2").click(function() {
			$("#modelsInDB2").dynatree({
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
					getTableValues2();
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
	
	
	
	
	
}).call(this);



function clearParams2(){
	filepath2="";
	processId2="";
	uploadfilepath2="";
}


/* Init the table */
function getTableValues2() {
	$('#modelsTable2').empty();
	/*
	 * Initialse DataTables, with no sorting on the 'details' column
	 */
	oTable2 = $('#modelsTable2').dataTable(
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
					
					$("#modelsTable2 tbody tr").unbind("click");
					$("#modelsTable2 tbody tr").click(function(e) {
						
						if ($(this).hasClass('row_selected')) {
							$(this).removeClass('row_selected');
						} else {
							//alert("bbbb");
							oTable2.$('tr.row_selected').removeClass('row_selected');
							$(this).addClass('row_selected');
						}
					});
				},

			});
}



function confirmQueryByModelInDB2(){
	
	clearParams2();
	
	var anSelected = fnGetSelected(oTable2);
	processId2=oTable2.fnGetData(anSelected[0])[1];
	var psvgPath =  oTable2.fnGetData(anSelected[0])[2].replace("\\", "/");
	$('#img2').attr("src",psvgPath);
	$("#showModelsInDB2").modal('hide');
	
}
//打开绘制窗口
function openDrawWin2(){
	divNum=2;
	
	
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
