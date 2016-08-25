//表格的对象
var oTable1;


(function() {
	$(document).ready(function() {	 			
		
		$('#modelfileupload1').click(function(){
			$('#progress1 .bar').css(
		            'width',
		              '0%'
		    );
			$('#uploaded-files1 h1').html("");
		});
		
		$('#modelfileupload1').fileupload({
	    	url:'uploadfiles/modeldifferentiation',
	    	autoUpload: true,
	    	
	        dataType: 'json',
	        
	        formData : {
	        	index : 1
	        },
	        
	        done: function (e, data) {
	        	
	        	clearParams1();
	        	
	        	var filepath = data.result['filepath'];
	        	uploadfilepath1=filepath;
	        	$.ajax({
				      cache: true,
				      type: "POST",
				      data: {
				    	  epmlFile:uploadfilepath1,
				      },
				      url:'process/epmlToPng',
				      async: false,
				      beforeSend:function(){
				      },
				      error: function(request) {
				          alert("Connection error");
				      },
				      success: function(data) {
				      	if(data.state == "SUCCESS"){
				      		var pngFilePath = data.pngFile.replace("\\", "/");
				      		$('#img1').attr("src",pngFilePath);
				      	} else {
				      		alert(data.message);
				      	}
				      	
				      }
				  });
	        },
	        
	        progressall: function (e, data) {
		        var progress = parseInt(data.loaded / data.total * 100, 10);
		        $('#progress1 .bar').css(
		            'width',
		            progress + '%'
		        );
		        if(data.loaded/data.total==1){
		        	$('#uploaded-files1 h1').html("上传完毕");
		        }
	   		},
	   		
			dropZone: $('#dropzone')
	    });

		
		$("#btnselectfromDB1").click(function() {
			$("#modelsInDB1").dynatree({
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
					getTableValues1();
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






/* Init the table */
function getTableValues1() {
	$('#modelsTable1').empty();
	/*
	 * Initialse DataTables, with no sorting on the 'details' column
	 */
	oTable1 = $('#modelsTable1').dataTable(
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
					
					$("#modelsTable1 tbody tr").unbind("click");
					$("#modelsTable1 tbody tr").click(function(e) {
						
						if ($(this).hasClass('row_selected')) {
							$(this).removeClass('row_selected');
						} else {
							//alert("bbbb");
							oTable1.$('tr.row_selected').removeClass('row_selected');
							$(this).addClass('row_selected');
						}
					});
				},

			});
}

function clearParams1(){
	filepath1="";
	processId1="";
	uploadfilepath1="";
}


function confirmQueryByModelInDB1(){
	clearParams1();
	var anSelected = fnGetSelected(oTable1);
	processId1=oTable1.fnGetData(anSelected[0])[1];
	var psvgPath =  oTable1.fnGetData(anSelected[0])[2].replace("\\", "/");
	$('#img1').attr("src",psvgPath);
	$("#showModelsInDB1").modal('hide');
	
}
//打开绘制窗口
function openDrawWin1(){
	divNum=1;
	/*var str = $('#confirmInfo1').serialize();
	//解决中文乱码
	str = decodeURIComponent(str,true);
	var arr = new Array();
	arr = str.split('&');
	processType = arr[0].split('=')[1];*/
	selectProcessType=processType;
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
