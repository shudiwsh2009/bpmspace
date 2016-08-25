var oTable;
var path = "assets/";
var processIdforrename;// 没有办法了。。。
var typeforrename;
var openMethod;
var processIdforedit;
var eidtTr;
var userId = "lvcheng";
var catalogId;
var processType="";
var jsonPath;
var svgPath;
var processName;
var processDescription;
var xmlPath;

var pageflag = "0_0_2";

(function() {
	$(document)
			.ready(
					function() {
						
						
						$("#tree2").dynatree({
							title : "Lazy loading sample",
							fx : {
								height : "toggle",
								duration : 200
							},
							autoFocus : false, // Set focus to first child,
							// when expanding or
							// lazy-loading.
							// In real life we would call a URL on the server
							// like this:
							initAjax : {
								url : 'processCatalog/getRootCatalogs',
								data : {
									mode : "funnyMode"
								}
							},

							onActivate : function(node) {
								catalogId = node.data.key;
								getTableValues();

							},

							onLazyRead : function(node) {
								// In real life we would call something like
								// this:
								node.appendAjax({
									url : "processCatalog/getCatalogs",
									data : {
										catalogId : node.data.key,
										mode : "funnyMode"
									}
								});

							}
						});
						
						$('#modelfileupload1').click(function(){
							$('#progress1 .bar').css(
						            'width',
						              '0%'
						    );
							$('#uploaded-files1 h1').html("");
						});
						
						
						

						$("#btnCreateDB").click(function() {

							$("#divNewMoXingKu").modal('show');

						});

						$("#moveProcess").click(function() {

							$("#tree3").dynatree({
								title : "Lazy loading sample",
								fx : {
									height : "toggle",
									duration : 200
								},
								autoFocus : false, // Set focus to first child,
								// when expanding or
								// lazy-loading.
								// In real life we would call a URL on the
								// server
								// like this:
								initAjax : {
									url : 'processCatalog/getRootCatalogs',
									data : {
										mode : "funnyMode"
									}
								},

								onActivate : function(node) {
								},

								onLazyRead : function(node) {
									// In real life we would call something like
									// this:
									node.appendAjax({
										url : "processCatalog/getCatalogs",
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

						$("#btnDeleteDB")
								.click(
										function() {
											bootbox
													.confirm(
															internationalNames["modelDB.confirm.deleteAllprocess"],
															function(result) {
																if (result) {
																	deleteCatalog();
																}

															});

										});

						$("#exportProcess").click(function() {

							var anSelected = fnGetSelected(oTable);
							// 删除选中的一行
							/*
							 * if ( anSelected.length !== 0 ) {
							 * oTable.fnDeleteRow( anSelected[0] ); }
							 */

							var result = '';
							for (var i = 0; i < anSelected.length; i++) {
								var pid = oTable.fnGetData(anSelected[i])[1];
								result = result + pid + ':';
							}

							$.ajax({
								cache : true,
								type : "POST",
								url : 'process/exportProcesses',
								data : {
									processIds : result
								},// 
								async : true,
								error : function(request) {
									alert("Connection error");
								},
								success : function(data) {
									if (data.state == "SUCCESS") {
										window.open (data.exports.replace("\\", "/"), 'newwindow', 'height=100, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no');
									} else {
										alert(data.message);
									}

								}
							});

						});

						$('#modelTable tbody td embed').live(
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
								//
							});

						});

					});
}).call(this);

function fnGetSelected(oTableLocal) {
	return oTableLocal.$('tr.row_selected');
}

/* Init the table */
function getTableValues() {

	// var nCloneTh = document.createElement('th');
	// var nCloneTd = document.createElement('td');
	// var src = path + "details_open.png";
	// nCloneTd.innerHTML = '<embed src=\'' + src + '\'>';
	// nCloneTd.className = "center";

	$('#modelTable').empty();

	/*
	 * Initialse DataTables, with no sorting on the 'details' column
	 */
	oTable = $('#modelTable').dataTable(
			{
				"bDestroy" : true,
				"bProcessing" : true,
				"bPaginate": true,
				
				"bFilter": false,
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

				

				"aoColumns" : [ {
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
					"sTitle" : internationalNames["DBTable.modelName"],
					"sClass" : "editable1111 center"
				}, {
					"sTitle" : internationalNames["DBTable.modelType"],
					"sClass" : "center"
				}, {
					"bVisible" : false,
					"sTitle" : internationalNames["DBTable.CreateInfo"],
					"sClass" : "center"
				}, {
					"bVisible" : false,
					"sTitle" : internationalNames["DBTable.Lastmodifiedtime"],
					"sClass" : "center"
				}, {
					"sTitle" : internationalNames["DBTable.size"],
					"sClass" : "center"
				}, {
					"sTitle" : internationalNames["DBTable.version"],
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
					// $('#newtable tbody tr').each(function() {
					// this.childNodes[0].Abbr = "defined";
					// });
					// $('.details').each(
					//
					// function() {
					// this.parentNode.childNodes[0].Abbr = "defined";
					// });
					//
					// $('#modelTable tbody tr').each(
					// function() {
					//
					// if (this.childNodes[0].Abbr != "defined") {
					//
					// this.insertBefore(nCloneTd.cloneNode(true),
					// this.childNodes[0]);
					// this.childNodes[0].Abbr = "defined";
					// }
					//
					// });
					$("#modelTable tbody tr").unbind("click");
					$("#modelTable tbody tr").click(function(e) {
						if ($(this).hasClass('row_selected')) {
							$(this).removeClass('row_selected');
						} else {
							$(this).addClass('row_selected');
						}
					});

					$("#modelTable tbody td").dblclick(
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

function fnFormatDetails(oTable, nTr) {
	var aData = oTable.fnGetData(nTr);
	var path = aData[2].replace("\\", "/");

	var sOut = '<table id="newtable" cellpadding="5"  cellspacing="0" border="0" style="padding-left:50px;">';
	sOut += '<tr><td>Create Information:</td><td>' + aData[5] + '</td></tr>';
	sOut += '<tr><td>Modify Information:</td><td>' + aData[6] + '</td></tr>';
	sOut += '<tr><td><a data-toggle=\'modal\' href=\'#lookBigImage\' role=\'button\'>'
			+ '<img id=\'suoluetu'
			+ aData[3]
			+ aData[4]
			+ '\''
			+ ' width=\'100\' height=\'50\'  type=\'image/svg-xml\' src=\''
			+ path
			+ '\'/>'
			+ '</a> </td><td><a class=\'btn btn-link edit\' onClick=\'fneditProcess("'
			+ aData[1]
			+ ','
			+ oTable.fnGetPosition(nTr)
			+ '"  )\' role=\'button\'>'
			+ ' <i class=\'icon-pencil\'></i>'
			+ '</a></td>' + '</tr>';
	sOut += '</table>';
	$("#imgLookBigImage").attr("src", path);

	return sOut;
}



function fneditProcess(value) {
	var processId = value.split(',')[0];
	eidtTr = value.split(',')[1];
	$.ajax({
		cache : true,
		type : "POST",
		url : 'process/checkEditProcess',
		data : {
			processId : processId
		},// 
		async : true,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				openMethod = "edit";
				processIdforedit = processId;
				processType=data.type;
				openWinEdit(data.type, data.xmlPath);
			} else {
				bootbox.dialog(data.message, [ {
					label : "Okay",
					"class" : "btn-primary"
				} ]);
			}
			// oTable.fnClearTable();
			oTable.fnDraw();

		}
	});
	// openWinEdit();
	// oTable.fnUpdate('c', 0, 3);
	// openWinEdit();
	/*
	 * oTable.fnClearTable(); oTable.fnDestroy(); getTableValues(); revision
	 * ],false);
	 * 
	 * $.ajax({ cache : true, type : "POST", url : 'process/editProcess',
	 * 
	 * data : { processId : processId, }, async : false, error :
	 * function(request) { alert("Connection error"); }, success :
	 * function(data) { if (data.state == "SUCCESS") { // 关闭弹出modal
	 * openMethod="edit"; openEditWin(); } else { alert(data.message); } } });
	 */
}

function openWinEdit(type, path) {
	
	var src = "";
	var path = path.replace("\\", "/");
	processType = type;
	var style = "width=600,height=800,location=no,directories=no,toolbar=no,status=no,menubar=no,resizable=no,scrollbars=no";

	if (type == "BPMN") {
		src = "http://localhost:8080/oryx/editor;bpmn2.0#/" + path;
	}
	if (type == "EPC") {
		src = "http://localhost:8080/oryx/editor;epc#/" + path;
	}
	if (type == "PETRINET") {
		src = "http://localhost:8080/oryx/editor;petrinet#/" + path;
	}
	window.open(src, "processsapce绘制模型工具", style);
}

function openWin() {
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

function setPath(value) {
	var path = value;
	var arr = new Array();
	arr = path.split('&');
	svgPath = arr[0];
	jsonPath = arr[1];
	xmlPath=arr[2];
	
	if(openMethod=="import"){
		$.ajax({
			cache : true,
			type : "POST",
			url : 'process/addProcess',
			data : {
				processName : processName,
				processDescription : processDescription,
				processType : processType,
				catalogId : catalogId,
				jsonPath : jsonPath,
				svgPath : svgPath,
				xmlPath:xmlPath
			},// 
			async : true,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				if (data.state == "SUCCESS") {
					var newSvgPath = data.svgPath;
					var size = data.size;
					var processId = data.processId;
					var createInfo = data.createInfo;
					var lastModifyInfo = data.lastModifyInfo;
					var revision = data.revision;
					oTable.fnAddData([ "<embed src=\""+"assets/details_open.png\">",processId, newSvgPath, processName,
							processType, createInfo, lastModifyInfo, size,
							revision ], false);

				} else {
					alert(data.message);
				}
				// oTable.fnClearTable();
				oTable.fnDraw();
			}
		});
	}
	
	if (openMethod == "new") {
		$.ajax({
			cache : true,
			type : "POST",
			url : 'process/addProcess',
			data : {
				processName : processName,
				processDescription : processDescription,
				processType : processType,
				catalogId : catalogId,
				jsonPath : jsonPath,
				svgPath : svgPath,
				xmlPath:xmlPath
			},// 
			async : true,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				if (data.state == "SUCCESS") {
					var newSvgPath = data.svgPath;
					var size = data.size;
					var processId = data.processId;
					var createInfo = data.createInfo;
					var lastModifyInfo = data.lastModifyInfo;
					var revision = data.revision;
					oTable.fnAddData([ "<embed src=\""+"assets/details_open.png\">",processId, newSvgPath, processName,
							processType, createInfo, lastModifyInfo, size,
							revision ], false);

				} else {
					alert(data.message);
				}
				// oTable.fnClearTable();
				oTable.fnDraw();

			}
		});
	} else if (openMethod == "edit") {
		$
				.ajax({
					type : "POST",
					url : 'process/editProcess',
					data : {
						processId : processIdforedit,
						jsonPath : jsonPath,
						svgPath : svgPath,
						xmlPath:xmlPath
					},// 
					async : true,
					//设置等待gif图
					beforeSend:function(){
						$("#waitForResponse").modal('show');
					},
					error : function(request) {
						$("#waitForResponse").modal('hide');
						alert("Connection error");
					},
					success : function(data) {
						$("#waitForResponse").modal('hide');
						if (data.state == "SUCCESS") {
							if (oTable.fnIsOpen(oTable.fnGetNodes(eidtTr))) {
								$(oTable.fnGetNodes(eidtTr)).find('embed')[0].src = "assets/details_open.png";
								oTable.fnClose(oTable.fnGetNodes(eidtTr));
							}
							
							// 最好能实现修改缩略图，将内容全部更新
							oTable.fnUpdate([
									"<embed src=\""
											+ "assets/details_open.png\">",
									data.processId, data.svgPath, data.name,
									data.type, data.createInfo,
									data.lastModifyInfo, data.size,
									data.revision ], eidtTr);
							bootbox.dialog("修改成功", [ {
								label : "Okay",
								"class" : "btn-primary"
							} ]);
						} else {
							alert(data.message);
						}
						// oTable.fnClearTable();

					}
				});
	}

}


function confirmInfoforimport() {
	$('#progress1 .bar').css(
            'width',
              '0%'
    );
	$('#uploaded-files1 h1').html("");
	var str = $('#confirmInfoforimport').serialize();
	str = decodeURIComponent(str,true);
	var arr = new Array();

	arr = str.split('&');

	processName = arr[0].split('=')[1];
	processDescription = arr[1].split('=')[1];
	processType = arr[2].split('=')[1];

	// 以后从后台传回来要打开的地址，避免过多暴露

	//上传部分
	$('#modelfileupload1').fileupload({
    	url:'uploadfiles/modelimport',
    	autoUpload: true,
    	
        dataType: 'json',
        
        formData : {
        	processType :  arr[2].split('=')[1]
        },
        
        done: function (e, data) {
        	if(data.result['status']=="FAILED"){
        		$('#uploaded-files1 h1').html(data.result['message']);
        	}
        	else{
        		$("#uploadModelFile1").modal('hide');
	        	var jsonpath = data.result['jsonpath'];
	        	openMethod="import";
	        	jsonpath=jsonpath.replace(/\\/g, "/");
	        	openWinEdit(processType, jsonpath);
        	}
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
	
	$.ajax({
		cache : true,
		type : "POST",
		url : 'process/checkProcess',

		data : str + '&catalogId=' + catalogId,// 
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				// 关闭弹出modal
				$("#importModelType").modal('hide');
				$("#uploadModelFile1").modal('show');
			} else {
				alert(data.message);
			}

		}
	});
}
function confirmInfo() {
	var str = $('#confirmInfo').serialize();
	str = decodeURIComponent(str,true);
	var arr = new Array();

	arr = str.split('&');

	processName = arr[0].split('=')[1];
	processDescription = arr[1].split('=')[1];
	processType = arr[2].split('=')[1];

	// 以后从后台传回来要打开的地址，避免过多暴露

	$.ajax({
		cache : true,
		type : "POST",
		url : 'process/checkProcess',

		data : str + '&catalogId=' + catalogId,// 
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				// 关闭弹出modal
				$("#selectModelType").modal('hide');
				openMethod = "new";
				openWin();
			} else {
				alert(data.message);
			}

		}
	});
}

function confirmKuName() {
	var str = $('#fmNewMoXingKu').serialize();
	//中文乱码
	str = decodeURIComponent(str,true); 
	var name = str.split('=')[1];
	if (name == "") {
		alert("模型库名称不能为空");
	}
	//alert(name);
	var node = $("#tree2").dynatree("getActiveNode");
	if(node==null){
		$("#divNewMoXingKu").modal('hide');
		bootbox.dialog("请先选择父模型库", [ {
			label : "Okay",
			"class" : "btn-primary"
		} ]);
	}else{
		$.ajax({
			url : "processCatalog/addCatalog",
			type : "POST",
			datatype : "json",
			data : {
				name : name,
				parentId : node.data.key
			},
			success : function(data) {
				if (data.state == "FAILED") {
					alert(data.message);
				} else {
					$("#divNewMoXingKu").modal('hide');
					var key = data.catalogId;
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
	
}

function deleteTableRows() {

	var anSelected = fnGetSelected(oTable);
	// 删除选中的一行
	/*
	 * if ( anSelected.length !== 0 ) { oTable.fnDeleteRow( anSelected[0] ); }
	 */

	var result = '';
	for (var i = 0; i < anSelected.length; i++) {
		var pid = oTable.fnGetData(anSelected[i])[1];
		result = result + pid + ':';
	}

	$
			.ajax({
				cache : true,
				type : "POST",
				url : 'process/removeProcesses',
				data : {
					processIds : result
				},// 
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {

						// oTable.fnDraw();

						for (var j = 0; j < anSelected.length; j++) {
							oTable.fnDeleteRow(anSelected[j]);
						}

					} else if (data.state == "PART") {
						var pids = data.processIds.split(':');
						for (var j = 0; j < anSelected.length; j++) {
							for (var m = 0; m < pids.length; m++) {
								if (oTable.fnGetData(anSelected[j])[1] != pids[m]) {
									oTable.fnDeleteRow(anSelected[j]);
								}
							}
						}

						var str = '';
						for (var m = 0; m < pids.length; m++) {
							str = str + '<p>' + pids[m] + '</p>';
						}
						document.getElementById("divresponseForRemoveContent").innerHTML = str;
						$("#divresponseForRemove").modal('show');
					} else {

						bootbox.dialog(data.message, [ {
							label : "Okay",
							"class" : "btn-primary"
						} ]);
					}

				}
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
						url : 'process/moveProcesses',
						data : {
							processIds : result,
							targetCatalogId : node.data.key,
							operator : 'FORCE'
						},// 
						async : true,
						error : function(request) {
							alert("Connection error");
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

							} else {
								alert(data.message);
							}

						}
					});
		} else {
			$.ajax({
				cache : true,
				type : "POST",
				url : 'process/cloneProcesses',

				data : {
					processIds : result,
					targetCatalogId : node.data.key,
					operator : 'FORCE'
				},// 
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						bootbox.dialog("SUCCESS!", [ {
							label : "Okay",
							"class" : "btn-primary"
						} ]);
					} else {
						alert(data.message);
					}

				}
			});
		}
	}
	$("#divresponseForMove").modal('hide');
}
function uploadModelFile1(){
	$("#uploadModelFile1").modal('hide');
	
}

function confirmMove() {
	var node = $("#tree3").dynatree("getActiveNode");
	var node1 = $("#tree2").dynatree("getActiveNode");
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
		var pid = oTable.fnGetData(anSelected[i])[1];
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
						url : 'process/moveProcesses',
						data : {
							processIds : result,
							targetCatalogId : node.data.key
						},// 
						async : true,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data) {
							if (data.state == "SUCCESS") {

								// 删除原有
								for (var j = 0; j < anSelected.length; j++) {
									oTable.fnDeleteRow(anSelected[j]);
								}
								//oTable.fnDraw();
								bootbox.dialog("SUCCESS!", [ {
									label : "Okay",
									"class" : "btn-primary"
								} ]);

							} else if (data.state == "PERMISSION") {
								str = '<p>一下为没有权限的模型名称</p>'
								var permissionIds = data.permissionIds
										.split(':');
								var permissionNames = data.permissionNames
										.split('%_%');
								for (var m = 0; m < permissionNames.length - 1; m++) {
									str = str + '<p>' + permissionNames[m]
											+ '</p>';
								}
								for (var j = 0; j < anSelected.length; j++) {
									for (var m = 0; m < permissionIds.length - 1; m++) {
										if (oTable.fnGetData(anSelected[j])[1] != permissionIds[m]) {
											oTable.fnDeleteRow(anSelected[j]);
										}
									}
								}
								document
										.getElementById("divresponseForMoveContent").innerHTML = str;
								$("#divresponseForMove").modal('show');

							} else if (data.state == "DUPLICATE") {

								str = '<p>以下为命名重复的模型名称</p>'
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
							} else if (data.state == "BOTH") {
								str = '<p>一下为没有权限的模型名称</p>'
								var permissionIds = data.permissionIds
										.split(':');
								var permissionNames = data.permissionNames
										.split('%_%');
								for (var m = 0; m < permissionNames.length - 1; m++) {
									str = str + '<p>' + permissionNames[m]
											+ '</p>';
								}
								for (var j = 0; j < anSelected.length; j++) {
									for (var m = 0; m < permissionIds.length - 1; m++) {
										if (oTable.fnGetData(anSelected[j])[1] != permissionIds[m]) {
											oTable.fnDeleteRow(anSelected[j]);
										}
									}
								}
								str = '<p>以下为命名重复的模型名称</p>'
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
							} else {
								alert(data.message);
							}

						}
					});
		} else {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : 'process/cloneProcesses',
						data : {
							processIds : result,
							targetCatalogId : node.data.key
						},// 
						async : true,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data) {
							var str = '';

							if (data.state == "SUCCESS") {
								bootbox.dialog("SUCCESS!", [ {
									label : "Okay",
									"class" : "btn-primary"
								} ]);
							} else if (data.state == "PERMISSION") {
								str = str + '<p>一下为没有权限的模型名称</p>'
								var permissionIds = data.permissionIds
										.split(':');
								var permissionNames = data.permissionNames
										.split('%_%');
								for (var m = 0; m < permissionNames.length - 1; m++) {
									str = str + '<p>' + permissionNames[m]
											+ '</p>';
								}
								document
										.getElementById("divresponseForMoveContent").innerHTML = str;
								$("#divresponseForMove").modal('show');

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
							} else if (data.state == "BOTH") {
								str = str + '<p>一下为没有权限的模型名称</p>'
								var permissionIds = data.permissionIds
										.split(':');
								var permissionNames = data.permissionNames
										.split('%_%');
								for (var m = 0; m < permissionNames.length - 1; m++) {
									str = str + '<p>' + permissionNames[m]
											+ '</p>';
								}

								str = str + '<p>以下为命名重复的模型名称</p>'
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

function deleteCatalog() {
	var node = $("#tree2").dynatree("getActiveNode");
	$.ajax({
		cache : true,
		type : "POST",
		url : 'processCatalog/removeCatalog',
		data : {
			catalogId : node.data.key
		},// 
		async : true,
		error : function(request) {
			alert('ss');

		},
		success : function(data) {
			if (data.state == "SUCCESS") {
				node.remove();
				$('#modelTable').empty();
				
			} else {
				alert(data.message);
			}

		}
	});
}