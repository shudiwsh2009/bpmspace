/**
 * 
 */
//表格的对象
var oTable1;
var inputFilePath="";
var processId1=new Array();
var filepaths = new Array();
var drawFilePath="";
var userId = "lvcheng";
var key = 1;
var selKeys;
var folder="";
var filenames;
var pageflag = "1_0_4";

var treeData = [
		{title: internationalNames["FileTree.alreadySelectedFile"], isFolder: true, key: "rootfolder",id:"rootfolder",
		    children: [
		     
		    ]
		  }];

                
               

(function() {
	$(document).ready(function() {	 
		
		Array.prototype.indexOf = function(val) {            
			for (var i = 0; i < this.length; i++) {
				if (this[i] == val) return i;
			}
			return -1;
		};
		
		Array.prototype.remove = function(val) {
			var index = this.indexOf(val);
			if (index > -1) {
				this.splice(index, 1);
			}
		};
		
		$('#btn_caclulate').click(function(){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepaths:filepaths,
			    	  processIds:processId1    //.join(":")
			      },
			      url:'model/modelMerge',
			      async: false,
			      beforeSend:function(){
			      },
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		
			      		//window.open (data.output.replace("\\", "/"), 'newwindow', 'height=100, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no');
			      		$("#calculateResult").html("<p><a href=\""+data.merge.replace(/\\/g, "/")+"\"><font color='#00acec' face='微软雅黑'><big>"+internationalNames["modelMerge.js.downloadresult"]+"<big></font></a></p>");
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		});
		
		$("#tree2").dynatree({
			checkbox: true,
			selectMode: 3,
			children: treeData,
			minExpandLevel : 2,
			title : "选择的模型",
			fx : {
				height : "toggle",
				duration : 200
			},
			autoFocus : false, 
			onSelect: function(select, node) {
		        // Get a list of all selected nodes, and convert to a key array:
				  selKeys= $.map(node.tree.getSelectedNodes(), function(node){
		          return node.data.key;
		        });
				  if(selKeys[0]=="rootfolder")
					  selKeys.shift();
				for(var i=0;i<selKeys.length;i++){
					
				}
		        console.debug(selKeys);
		        
		      },

		      onClick: function(node, event) {
		        // We should not toggle, if target was "checkbox", because this
		        // would result in double-toggle (i.e. no toggle)
		        if( node.getEventTargetType(event) == "title" )
		          node.toggleSelect();
		      },
		      onKeydown: function(node, event) {
		        if( event.which == 32 ) {
		          node.toggleSelect();
		          return false;
		        }
		      },
		      // The following options are only required, if we have more than one tree on one page:
		      cookieId: "dynatree-Cb2",
		      idPrefix: "dynatree-Cb2-"

		});
		//弹出框显示出来 hide为隐藏
		//$("#modelsInDB").modal('show');
	
		
		$('#modelfileupload1').click(function(){
			$('#progress1 .bar').css(
		            'width',
		              '0%'
		    );
			$('#uploaded-files1 h1').html("");
		});
		
		
		
		
		$("#testAdd").click(function(){
			key++;
	      // Sample: add an hierarchic branch using code.
	      // This is how we would add tree nodes programatically
			 var folderNode =  $("#tree2").dynatree("getTree").getNodeByKey("rootfolder");
			 var childNode = folderNode.addChild({
		        title: "Programatically addded nodes",
		        tooltip: "",
		        key:key,
		        isFolder: false
		      });
		      
	    });

		
		$('#btnremoveprocesses').click(function(){
			var delFileNames="";
			
			for(var i=0;i<selKeys.length;i++){
				if(selKeys[i].indexOf("file")!=-1){
					$("#tree2").dynatree("getTree").getNodeByKey(selKeys[i]).remove();
					filepaths.remove(selKeys[i].split("_")[1]);
				}
				if(selKeys[i].indexOf("catalog")!=-1){
					$("#tree2").dynatree("getTree").getNodeByKey(selKeys[i]).remove();
					processId1.remove(selKeys[i].split("_")[1]);
				}
				
			}
		});
		
		//上传部分
		$('#modelfileupload1').fileupload({
	    	url:'uploadfiles/modelMerge',
	    	autoUpload: true,
	    	
	        dataType: 'json',
	        
	        formData : {
	        	index : 1
	        },
	        
	        done: function (e, data) {
	        	if(data.result['status']=="sucess"){
	        		var filepath=data.result['filepath'];
	        		filepaths.push(filepath);
		        	var filename = data.result['filename'];
				      // Sample: add an hierarchic branch using code.
				      // This is how we would add tree nodes programatically
					 var folderNode =  $("#tree2").dynatree("getTree").getNodeByKey("rootfolder");
					 var childNode = folderNode.addChild({
				        title: filename,
				        tooltip: "",
				        key:"file_"+filepath,
				        isFolder: false
				      });
	        	}else if(data.result['status']=="failed"){
	        		bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>请上传bpmn文件！<big></font>", [ {
						label : "确定",
						"class" : "btn-primary"
					} ]);
	        	}
	        	
			    
		  
	        	//上传以后想要得到图
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
							$(this).addClass('row_selected');
						}
					});
				},

			});
}



function confirmQueryByModelInDB1(){
	var anSelected = fnGetSelected(oTable1);
	var result = '';
	for (var i = 0; i < anSelected.length; i++) {
		var pid = oTable1.fnGetData(anSelected[i])[1];
		processId1.push(pid);
		var pname = oTable1.fnGetData(anSelected[i])[3];
		key++;
	      // Sample: add an hierarchic branch using code.
	      // This is how we would add tree nodes programatically
		 var folderNode =  $("#tree2").dynatree("getTree").getNodeByKey("rootfolder");
		 var childNode = folderNode.addChild({
	        title: pname,
	        tooltip: "",
	        key:"catalog_"+pid,
	        isFolder: false
	      });
	}
	$("#showModelsInDB1").modal('hide');
	
}

function fnGetSelected(oTableLocal) {
	return oTableLocal.$('tr.row_selected');
}






