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

function upload_log_file() {
	
	$('#fileuploadInstance').fileupload({
		url:'uploadfiles/input_instance',
		autoUpload: true,
        dataType: 'json',
        done: function (e, data) {
        	$("#upload-files-info").append("<label id='log-upload-success'>日志上传成功！</label>");
        	document.getElementById("btn-upload-finish").removeAttribute("disabled");
        	var node = $("#instanceDB_tree").dynatree("getActiveNode");
        	catalogId = node.data.key;
        	node.appendAjax({
				type : "GET",
				url : "instanceCatalog/getCatalogs",
				data : {
					catalogId : node.data.key,
					mode : "funnyMode"
				}
			});
        	load_log_database();
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
				statisticTableCases();
				statisticActivityTable();
				statisticResourceTable();
				getRemoteDataDrawChart('statisticCharts/statisticInstanceChart', createNewLineChart('instanceChart'));
				getRemoteDataDrawChart('statisticCharts/statisticActivityChart', createNewLineChart('activityChart'));
				getRemoteDataDrawChart('statisticCharts/statisticResourceChart', createNewLineChart('resourceChart'));
				getRemoteDataDrawPieChart('statisticCharts/statisticResourcePieChart', createNewPieChart('resourcePieChart'));
				getRemoteDataDrawPieChart('statisticCharts/statisticActivityPieChart', createNewPieChart('activityPieChart'));	

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
                alert("Connection error 1");
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


function forceMove() {
	var node = $("#tree3").dynatree("getActiveNode");
	var result = "";
	var pids = [];
	// 获得被选中的值
	$('input[name="forceMoveItem"]:checked').each(function() {
		result = result + ($(this).val()) + ':';
		pids.push($(this).val());
	});
	if (pids.length == 0) {
		bootbox.dialog("同学，你没有选择", [ {
			label : "Okay",
			"class" : "btn-primary"
		} ]);
	} else {
		// 获得复选框内的值 值为模型的id 在创建的时候生成的 通过ajax与后台交互
		if (!document.getElementById("checkbox").checked) {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : 'instanceCatalog/moveLogs',
						data : {
							logIds : result,
							targetCatalogId : node.data.key,
							operator : 'FORCE'
						},// 
						async : true,
						error : function(request) {
							alert("Connection error 2");
							$("#divyidongmoxing").modal('hide');
						},
						success : function(data) {
							if (data.state == "SUCCESS") {
								// 删除原有 并且删除一定要移动的
								for (var j = 0; j < anSelected.length; j++) {
									for (var m = 0; m < pids.length; m++) {
										if (oTable.fnGetData(anSelected[j])[1] == pids[m]) {
											oTable.fnDeleteRow(anSelected[j]);
										}
									}
								}
								oTable.fnDraw();
								nodeInInstanceDBTree = $("#instanceDB_tree").dynatree("getTree");
								nodeInInstanceDBTree.tree.reload();

								
							} else {
								alert(data.message);
							}

						}
					});
		} else {
			$.ajax({
				cache : true,
				type : "POST",
				url : 'instanceCatalog/cloneLogs',

				data : {
					logIds : result,
					targetCatalogId : node.data.key,
					operator : 'FORCE'
				},// 
				async : true,
				error : function(request) {
					alert("Connection error 3");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						bootbox.dialog("SUCCESS!", [ {
							label : "Okay",
							"class" : "btn-primary"
						} ]);
						
						nodeInInstanceDBTree = $("#instanceDB_tree").dynatree("getTree");
						nodeInInstanceDBTree.tree.reload();
						
					} else {
						alert(data.message);
					}

				}
			});
		}
	}
	$("#divresponseForMove").modal('hide');
}

function confirmMove() {
	var nodeInInstanceDBTree;
	var node = $("#tree3").dynatree("getActiveNode");
	var node1 = $("#instanceDB_tree").dynatree("getActiveNode");
	var anSelected = fnGetSelected(oTable);
	if (anSelected.length == 0) {
		bootbox.dialog("You Choose none item!", [ {
			label : "Okay",
			"class" : "btn-primary"
		} ]);
	}
	// 删除选中的一行
	/*
	 * if ( anSelected.length !== 0 ) { oTable.fnDeleteRow( anSelected[0] ); }
	 */

	var result = '';
	for (var i = 0; i < anSelected.length; i++) {
		var pid = oTable.fnGetData(anSelected[i])[0];
		result = result + pid + ':';
	}

	if (node.data.key == node1.data.key) {
		bootbox.dialog("You select the same catalog!", [ {
			label : "Okay",
			"class" : "btn-danger"
		} ]);
	} else {
		if (!document.getElementById("checkbox").checked) {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : 'instanceCatalog/moveLogs',
						data : {
							logIds : result,
							targetCatalogId : node.data.key
						},// 
						async : true,
						error : function(request) {
							alert("Connection error 4");
						},
						success : function(data) {
							if (data.state == "SUCCESS") {

								// 删除原有
								for (var j = 0; j < anSelected.length; j++) {
									oTable.fnDeleteRow(anSelected[j]);
								}
								oTable.fnDraw();
								bootbox.dialog("SUCCESS!", [ {
									label : "Okay",
									"class" : "btn-primary"
								} ]);
								
								//node1.move(node, child);
								
								node1.tree.reload();
								node1.appendAjax({
									type : "GET",
									url : "instanceCatalog/getCatalogs",
									data : {
										catalogId : node1.data.key,
										mode : "funnyMode"
									}
								});
								
								nodeInInstanceDBTree = $("#instanceDB_tree").dynatree("getTree");
								nodeInInstanceDBTree.tree.reload();
								/**
								 * 
								 
								nodeInInstanceDBTree.appendAjax({
									type : "GET",
									ulr : "instanceCatalog/getCatalogs",
									data : {
										catalogId : nodeInInstanceDBTree.data.key,
										mode : "funnyMode"
									}
								});
								*/
							} 
							else if (data.state == "DUPLICATE") {

								str = '<p>以下为命名重复的日志名称</p>'
								var duplicateIds = data.duplicateIds.split(':');
								var duplicateNames = data.duplicateNames
										.split('%_%');
								for (var m = 0; m < duplicateIds.length - 1; m++) {
									str = str
											+ '<p>'
											+ '<input type="checkbox" name="forceMoveItem" value='
											+ '"' + duplicateIds[m] + '"'
											+ '/>' + duplicateNames[m] + '</p>';
								}
								for (var j = 0; j < anSelected.length; j++) {
									for (var m = 0; m < duplicateIds.length - 1; m++) {
										if (oTable.fnGetData(anSelected[j])[1] != duplicateIds[m]) {
											oTable.fnDeleteRow(anSelected[j]);
										}
									}
								}
								document
										.getElementById("divresponseForMoveContent").innerHTML = str;
								$("#divresponseForMove").modal('show');
							} 
							else {
								alert(data.message);
							}

						}
					});
		} else {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : 'instanceCatalog/cloneLogs',
						data : {
							logIds : result,
							targetCatalogId : node.data.key
						},// 
						async : true,
						error : function(request) {
							alert("Connection error 5");
						},
						success : function(data) {
							var str = '';

							if (data.state == "SUCCESS") {
								bootbox.dialog("SUCCESS!", [ {
									label : "Okay",
									"class" : "btn-primary"
								} ]);
								node1.tree.reload();
								node1.appendAjax({
									type : "GET",
									url : "instanceCatalog/getCatalogs",
									data : {
										catalogId : node1.data.key,
										mode : "funnyMode"
									}
								});
								
								nodeInInstanceDBTree = $("#instanceDB_tree").dynatree("getTree");
								nodeInInstanceDBTree.tree.reload();
								/**
								 * 
								 
								nodeInInstanceDBTree.appendAjax({
									type : "GET",
									ulr : "instanceCatalog/getCatalogs",
									data : {
										catalogId : nodeInInstanceDBTree.data.key,
										mode : "funnyMode"
									}
								});
								*/
								
							} else if (data.state == "DUPLICATE") {
								str = str + '<p>以下为命名重复的模型名称,选择是否覆盖</p>'
								var duplicateIds = data.duplicateIds.split(':');
								var duplicateNames = data.duplicateNames
										.split('%_%');
								for (var m = 0; m < duplicateIds.length - 1; m++) {
									str = str
											+ '<p>'
											+ '<input type="checkbox" name="forceMoveItem" value='
											+ '"' + duplicateIds[m] + '"'
											+ '/>' + duplicateNames[m] + '</p>';
								}
								document
										.getElementById("divresponseForMoveContent").innerHTML = str;
								$("#divresponseForMove").modal('show');
							} else {
								alert(data.message);
							}

						}
					});

		}
		$("#divyidongmoxing").modal('hide');
	}

}

function fnGetSelected(oTableLocal) {
	return oTableLocal.$('tr.row_selected');
}

function deleteCatalog(node) {
	//var node = $("#instanceDB_tree").dynatree("getActiveNode");
	$.ajax({
		cache : true,
		type : "POST",
		url : 'instanceCatalog/removeCatalog',
		data : {
			catalogId : node.data.key
		},// 
		async : true,
		error : function(request) {

		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				node.remove();
				
			} else {
				//alert(data.message);
			}

		}
	});
}

function deleteLog(node) {
	//var node = $("#instanceDB_tree").dynatree("getActiveNode");
	$.ajax({
		cache : true,
		type : "POST",
		url : 'log/log_delete',
		data : {
			logId : node.data.key
		},// 
		async : true,
		error : function(request) {
			//alert('ss');

		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				node.remove();
				oTable.fnClearTable();
				/**
				 * 
				 
				for (var j = 0; j < anSelected.length; j++) {
					oTable.fnDeleteRow(anSelected[j]);
				}
				oTable.fnDraw();*/
			} else {
				//alert(data.message);
			}

		}
	});
}

function confirmRemove() {
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	//var nodeInInstanceDBTree;
	//var node = $("#tree3").dynatree("getActiveNode");
	//var node1 = $("#instanceDB_tree").dynatree("getActiveNode");
	var anSelected = fnGetSelected(oTable);
	if (anSelected.length == 0) {
		bootbox.dialog("You Choose none item!", [ {
			label : "Okay",
			"class" : "btn-primary"
		} ]);
	}
	var result = '';
	for (var i = 0; i < anSelected.length; i++) {
		var lid = oTable.fnGetData(anSelected[i])[0];
		result = result + lid + ':';
	}
	
	var match = null;
	$("#instanceDB_tree").dynatree("getRoot").visit(function(node){
	    if(node.data.key === lid){
	        match = node;
	    }
	});
	//alert("Found " + match);
	deleteCatalog(match);
	deleteLog(match);
	load_log_database();
	$("#divNewRemoveConfirm").modal('hide');
}

function confirmName() {
	var str = $('#fmNewShiLiKu').serialize();
	var name = str.split('=')[1];
	if (name == "") {
		alert("实例库名称不能为空");
	}
	var node = $("#instanceDB_tree").dynatree("getActiveNode");
	$.ajax({
		url : "instanceCatalog/addCatalog",
		type : "GET",
		datatype : "json",
		data : {
			name : name,
			parentId : node.data.key
		},
		success : function(data) {
			if (data.state == "FAILED") {
				alert(data.message);
			} else {
				document.getElementById("shiLiKuName").value = "";
				$("#divNewShiLiKu").modal('hide');
				var key = data.catalogId;
				/**
				 * 
				node.appendAjax({
					type : "GET",
					url : "instanceCatalog/getCatalogs",
					data : {
						catalogId : key,
						mode : "funnyMode"
					}
				});
				*/
				 var childNode = node.addChild({
					title : name,
					key : key,
					isFolder : 'true'
				});
				
			}

		},
		error : function(data) {
			alert("fail");
		}
	});
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