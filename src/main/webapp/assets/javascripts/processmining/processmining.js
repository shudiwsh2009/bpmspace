var pageflag = "1_2_0";
//var inputFilePathFromDB;
//var outputFileNameFromDB;
var tabIndex = 0;

function choose_tab_index(){
	$('#fileChoose a[href="#home"]').click(function (e) {
		e.preventDefault();
		//$(this).tab('show');
		tabIndex = 0;
		$('#fileChoose a:first').tab('show');
	})
	$('#fileChoose a[href="#FromInstanceDB"]').click(function (e) {
		e.preventDefault();
		//$(this).tab('show');
		tabIndex = 1;
		$('#fileChoose a:last').tab('show');
	})
}

function upload_log_file(){
	var inputFilePath = null;
	var outputFileName = null;
	var fileValid = false;
	
	$('#logfileuploadpm').fileupload({
		url:'uploadfiles/processmining',
		autoUpload: true,
	    dataType: 'json',
	    done: function (e, data) {
	    	$("tr:has(td)").remove();
	        $.each(data.result, function (index, file) {
	        	// 返回需要的值
		    	inputFilePath = file.inputFilePath;
			    outputFileName = file.outputFileName;
			    fileValid = file.valid;
			    
	            $("#uploaded-files").append(
	            		$('<tr/>')
	            		.append($('<td/>').text(file.fileName))
	            		.append($('<td/>').text(file.fileSize))
	            		.append($('<td/>').text(file.fileType))
	            		.append($('<td/>').html("<a href='uploadfiles/get/"+index+"'>Click</a>"))
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
	
	$("#generate_mining_btn").click(function() {
		var algorithm = $("#mining_algorithm_selected").val();
		
		if(algorithm == undefined || algorithm == "") {
			bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>请选择一个挖掘算法！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
		}
		if(tabIndex == 0) {
			if(inputFilePath == null || outputFileName == null) {
				bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>请先上传文件！<big></font>", [ {
					label : "确定",
					"class" : "btn-primary"
				} ]);
			} else if(fileValid == false) {
				bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>文件不合法，请上传一个【.mxml】文件！<big></font>", [ {
					label : "确定",
					"class" : "btn-primary"
				} ]);
			} else {
				if (tabIndex == 0) {
					generate_processmining(algorithm, inputFilePath, outputFileName);
				}
				else {
					generate_processmining(algorithm, inputFilePathFromDB, outputFileNameFromDB);
				}
			}
		}
		else {
			if(inputFilePathFromDB == null || outputFileNameFromDB == null) {
				bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>请先上传文件！<big></font>", [ {
					label : "确定",
					"class" : "btn-primary"
				} ]);
			} else {
				if (tabIndex == 0) {
					generate_processmining(algorithm, inputFilePath, outputFileName);
				}
				else {
					generate_processmining(algorithm, inputFilePathFromDB, outputFileNameFromDB);
				}
			}
		}
		
	});
}

function generate_processmining(algorithm, inputFilePath, outputFileName) {
	$.ajax({
        cache: true,
        type: "POST",
        url: 'processmining/mining',
        data: { algorithm: algorithm, 
        	    inputFilePath: inputFilePath, 
        	    outputFileName: outputFileName
        	   },
        async: true,
        beforeSend:function(){
        	console.log(12);
			$("#waitForResponse").modal('show');
		},
        error: function(request) {
        	$("#waitForResponse").modal('hide');
        	bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>挖掘过程出现问题，请重新上传文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
        },
        success: function(data) {
        	if(data.state == "SUCCESS"){
        		$("#waitForResponse").modal('hide');
        		show_minging_result(data.png, data.file_folder, data.file_user, data.file_type, data.file_name, data.file_suffix);
        	} else {
        		alert(data.message);
        	}
        }
	});
}

function show_minging_result(png_path, file_folder, file_user, file_type, file_name, file_suffix) {
	$("#png_result").html("<img src='"+png_path+"'/>");
	$("#download_picture").html("<a target='_blank' href='"+png_path+"' class='btn btn-primary center' name='button'>查看大图</a>");
	$("#download_file").html("<a href='uploadfiles/download/"+file_folder+"/"+file_user+"/"+file_type+"/"+file_name+"/"+file_suffix+"' class='btn btn-primary center' name='button'>下载【.pnml】文件</a>");
	$("#minging_result").show();
}

/**
 * 1111
 */
/**
 * author: xiaoyb
 * modified: motianyu
 */
/**
 * author: xiaoyb
 */
var delete_node_logId;
var selected_node;
var catalogIdForInput;
var logId;
var oTable;
/**
 * 	flag is 0, means that this page is first loading
 * 	flag is 1, means that this page is already loaded
 */
var flag = 0;
function showHint(str) {
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	$.ajax({
		cache : true,
		type : "GET",
		url : 'log/name_check',
		data : {
			tmpName : str,
			catalogId : node.data.key
		},
		async : true,
		error : function(request) {
			alert("Connection error 6");
		},
		success : function(data) {
			if (data.state == "FAILED") {
				//alert('LogName pass succeed!!');
				document.getElementById("txtHint").innerHTML="<a class='btn btn-danger btn-mini' href='#'><i class='icon-remove'></i></a>";
				document.getElementById("hintLog").innerHTML="<p id='temp'>该名称已存在！</p>";
				$("#btnNext").attr("disabled", true);
			} else {
				var child = document.getElementById("temp");
				if (child != null) {
					document.getElementById("hintLog").removeChild(child);
					document.getElementById("btnNext").removeAttribute("disabled");
				}
				document.getElementById("txtHint").innerHTML="<a class='btn btn-success btn-mini' href='#' ><i class='icon-ok'></i></a>";
			}
		}
	});
}

function LogNameUpload() {
	
	$.ajax({
		cache : true,
		type : "POST",
		url : 'uploadfiles/logName_input',
		data : {
			logName : document.getElementById("logName").value
		},
		async : true,
		error : function(request) {
			alert("Connection error 7");
		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				//alert('LogName pass succeed!!');
				document.getElementById("logName").value = "";
				$("#divNewLogName").modal('hide');
				$("#divChooseLog").modal('show');
				$("#btn-upload-finish").attr("disabled", true);
			} else {
				alert(data.message);
			}
		}
	});
}

function cleanModal() {
	var child = document.getElementById("log-upload-success");
	if (child != null) {
		document.getElementById("upload-files-info").removeChild(child);
	}
	$('#progress .bar').css(
            'width',
             '0%'
        );
}

function instance_database_tree(){
	$("#instanceDB_tree").dynatree({
		title : "Lazy loading sample",
		fx : {
			height : "toggle",
			duration : 200
		},
		autoFocus : true, // Set focus to first child,
							// when expanding or
							// lazy-loading.
		// In real life we would call a URL on the server
		// like this:
		initAjax : {
			url : 'instanceCatalog/getRootCatalogs',
			data : {
				mode : "funnyMode"
			}
		},
		onActivate : function(node) {
			var node = $("#instanceDB_tree").dynatree("getActiveNode");
			for (i in node.getChildren) {
				alert(i.data.title);
			}
			catalogIdForInput = node.data.key;
			catalogId = node.data.key;
			delete_node_logId = node.data.key;
			selected_node = node;
			node_info_pass();
			
			if (!node.data.isFolder) {
				load_instance_database();
				//alert("selectedA");
				$.ajax({
					cache : true,
					type : "POST",
					url : 'log/log2mxml',
					data : {
						logId : node.data.key
					},
					async : true,
					error : function(request) {
						alert("Connection error 11");
					},
					success : function(data) {
						inputFilePathFromDB = data.inputPrePath;
						outputFileNameFromDB = data.outputPrePath;
						if (data.outputPath == "hhhh") {
							alert(data.outputPath);
							document.getElementById("txtHint").innerHTML="<a class='btn btn-danger btn-mini' href='#'><i class='icon-remove'></i></a>";
							document.getElementById("hintLog").innerHTML="<p id='temp'>该名称已存在！</p>";
							$("#btnNext").attr("disabled", true);
						} else {
							var child = document.getElementById("temp");
							if (child != null) {
								document.getElementById("hintLog").removeChild(child);
								document.getElementById("btnNext").removeAttribute("disabled");
							}
							document.getElementById("txtHint").innerHTML="<a class='btn btn-success btn-mini' href='#' ><i class='icon-ok'></i></a>";
						}
					}
				});
			}
			else if (node.data.isFolder) {
				node.appendAjax({
					type : "GET",
					url : "instanceCatalog/getCatalogs",
					data : {
						catalogId : node.data.key,
						mode : "funnyMode"
					}
				});
				load_log_database();
			}
		},
		
		onLazyRead : function(node) {
			if (!node.data.isFolder) {
				instance_activity_tree();
				//alert("selectedB");
			}
			else {
			}
		}
		
	});
	$("#moveProcess").click(function() {
		$("#tree3").dynatree({
			title : "Lazy loading sample",
			fx : {
				height : "toggle",
				duration : 200
			},
			autoFocus : false, 
			initAjax : {
				url : 'instanceCatalog/getRootCatalogs',
				data : {
					mode : "funnyMode"
				}
			},
			onActivate : function(node) {
				catalogId = node.data.key;
			},
			onLazyRead : function(node) {
				node.appendAjax({
					type : "GET",
					url : "instanceCatalog/getCatalogs",
					data : {
						catalogId : node.data.key,
						mode : "funnyMode"
					}
				});
			}
		});
		var anSelected = fnGetSelected(oTable);
		if (anSelected.length == 0) {
			bootbox.dialog("You Choose none item!", [ {
				label : "Okay",
				"class" : "btn-primary"
			} ]);
		} else {
			$("#divyidongmoxing").modal('show');
		}
	});
	$("#exportProcess").click(function() {
		var anSelected = fnGetSelected(oTable);
		var result = '';
		for (var i = 0; i < anSelected.length; i++) {
			var pid = oTable.fnGetData(anSelected[i])[0];
			result =result+ pid + ':';
		}
		$.ajax({
			cache : true,
			type : "POST",
			url : 'process/exportProcess',
			data : {
				ids : result
			},// 
			async : true,
			error : function(request) {
				alert("Connection error 8");
			},
			success : function(data) {
				if (data.state == "SUCCESS") {
					alert('Save Successed!!');
				} else {
					alert(data.message);
				}
			}
		});
	});
	$('#instanceTable tbody td embed').live(
			'click',
			function() {
				var nTr = $(this).parents('tr')[0];
				if (oTable.fnIsOpen(nTr)) {
					/* This row is already open - close it */
					this.src = path + "details_open.png";
					oTable.fnClose(nTr);
				} else {
					/* Open this row */
					this.src = path + "details_close.png";
					oTable.fnOpen(nTr, fnFormatDetails(
							oTable, nTr), 'details');
				}
			});
	/* Add a click handler for the delete row */
	$('#deleteProcess').click(function() {
		bootbox.confirm("Are you sure?", function(result) {
			if (result) {
				deleteTableRows();
			}
		});
	});
	
}

function fnGetSelected(oTableLocal) {
	return oTableLocal.$('tr.row_selected');
}


function node_info_pass() {
	
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	catalogIdForInput = node.data.key;
	$.ajax({
		url : "uploadfiles/node_info_pass",
		type : "POST",
		datatype : "json",
		data : {
			catalogId : catalogIdForInput
		},
		success : function(data) {
		},
		error : function(data) {
			alert("fail");
		}
	});
}

function tableClean() {
	
	var div = document.getElementById("tableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("tableOperator").innerHTML="<table id='instanceTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
}


function load_instance_database(){
	var div = document.getElementById("tableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("tableOperator").innerHTML="<table id='instanceTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var nCloneTh = document.createElement('th');
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#instanceTable').dataTable({
		"bDestroy" : true,
		"bProcessing" : true,
		"sAjaxSource" : "instanceManagement/instanceDB_load",
		"fnServerParams" : function(aoData) {
			aoData.push({
				"name" : "logId",
				"value" : node.data.key
			});
		},
		"sServerMethod" : "GET",
		"aoColumns" : [ {
			"bVisible" : false,
			"bSearchable" : false,
			"sTitle" : "实例id",
			"sClass" : "center"
		}, {
			"bVisible": false,
			"sTitle" : "实例名称",
			"sClass" : "center"
		}, {
			"bVisible" : false,
			"sTitle" : "实例创建人",
			"sClass" : "center"
		}, {
			"sTitle" : "活动名称",
			"sClass" : "center"
		}, {
			"sTitle" : "活动人",
			"sClass" : "center"
		}, {
			"sTitle" : "开始时间",
			"sClass" : "center"
		}, {
			"sTitle" : "结束时间",
			"sClass" : "center"
		} ],
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


function load_log_database(){
	var div = document.getElementById("tableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("tableOperator").innerHTML="<table id='instanceTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
	//$('#instanceTable').dataTable();
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var nCloneTh = document.createElement('th');
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#instanceTable').dataTable(
			{
				"bDestroy" : true,
				"bProcessing" : true,
				//"bPaginate": true,
				//"bInfo": true, 
				//服务器端分页必需的三个参数
				"sAjaxSource" : "instanceCatalog/catalog_load",
				//"bServerSide": true,
				/*"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
					 oSettings.jqXHR = $.ajax( {
					        "dataType": 'json',
					        "type": "get",
					        "url": sSource,
					        "data": aoData,
					        "success":  fnCallback
					      } );
					 
				},
				*/
				

				"fnServerParams" : function(aoData) {

					aoData.push({
						"name" : "catalogId",
						"value" : node.data.key
					});

				},

				
				"sServerMethod" : "GET",
				"aoColumns" : [ {
					"bVisible" : false,
					"sTitle" : "id",
					"sClass" : "center"
				}, {
					"sTitle" : "类型",
					"sClass" : "center"
				}, {
					"sTitle" : "名称",
					"sClass" : "center"
				}, {
					"sTitle" : "所属目录",
					"sClass" : "center"
				}, {
					"sTitle" : "创建时间",
					"sClass" : "center"
				} ],

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

function load_log_database_new(node){
	var div = document.getElementById("tableOperator");
    while(div.hasChildNodes()) 
    {
        div.removeChild(div.firstChild);
    }
    document.getElementById("tableOperator").innerHTML="<table id='instanceTable' class='table table-hover'><thead></thead><tbody></tbody></table>";
	//$('#instanceTable').dataTable();
	//var node = $("#instanceDB_tree").dynatree("getActiveNode");
	var nCloneTh = document.createElement('th');
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#instanceTable').dataTable(
			{
				"bDestroy" : true,
				"bProcessing" : true,
				//"bPaginate": true,
				//"bInfo": true, 
				//服务器端分页必需的三个参数
				"sAjaxSource" : "instanceCatalog/catalog_load",
				"bServerSide": true,
				/*"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
					 oSettings.jqXHR = $.ajax( {
					        "dataType": 'json',
					        "type": "get",
					        "url": sSource,
					        "data": aoData,
					        "success":  fnCallback
					      } );
					 
				},
				*/
				

				"fnServerParams" : function(aoData) {

					aoData.push({
						"name" : "catalogId",
						"value" : node.data.key
					});

				},

				
				"sServerMethod" : "GET",
				"aoColumns" : [ {
					"sTitle" : "id",
					"sClass" : "center"
				}, {
					"sTitle" : "类型",
					"sClass" : "center"
				}, {
					"sTitle" : "名称",
					"sClass" : "center"
				}, {
					"sTitle" : "所属目录",
					"sClass" : "center"
				}, {
					"sTitle" : "创建时间",
					"sClass" : "center"
				} ],

				"fnInitComplete" : function() {

					// $('#modelTable thead tr').each(function() {
					// this.insertBefore(nCloneTh, this.childNodes[0]);
					//
					// });
				},

				"fnCreatedRow" : function(nRow, aData, iDataIndex) {

				},
				

				"fnDrawCallback" : function() {
					$("#instanceTable tbody tr").unbind("click");
					$("#instanceTable tbody tr").click(function(e) {
						if ($(this).hasClass('row_selected')) {
							$(this).removeClass('row_selected');
						} else {
							$(this).addClass('row_selected');
						}
					});

					$("#instanceTable tbody td").dblclick(
							function(e) {
								processIdforrename = oTable.fnGetData(oTable
										.fnGetPosition(this)[0], 1);
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
						validate : function(value) {
							if ($.trim(value) == '') {
								return 'This field is required';
							}
						},
						success : function(response, newValue) {
							if (response.state == 'FAILED') {
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
	// 隐藏第二列
	// oTable.fnSetColumnVis( 0, false );
}



