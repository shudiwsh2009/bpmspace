var pageflag = "0_1_1";
function upload_model_file(){
	var inputFilePath = null;
	var outputFileName = null;
	var fileValid = false;
	
	$('#modelfileuploadlg').fileupload({
		url:'uploadfiles/loggenerator',
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
	
	$("#loggenerate_btn").click(function(){
		// pre..		
		var dS1 = false;
		var tS = false;
		var tarCompleteness = -2.0;
		var causalCompleteness = -2.0;
		var freqCompleteness = -2.0;
		var noiseType = [0, 0, 0, 0, 0];
		var noiseFlag = 1;

		var dsCheck = document.getElementById('dsplus_check');
		if(dsCheck.checked == true)
			dS1 = true;
		
		var tsCheck = document.getElementById('ts_check');
		if(tsCheck.checked == true)
			tS = true;
		
		var tarCheck = document.getElementById('tarCheck');
		if(tarCheck.checked == true) {
			tarCompleteness = $("#tar_completeness").val()*1;
			if(tarCompleteness == '') {
				tarCompleteness = 1.0;
			} else if(tarCompleteness < 1 || tarCompleteness >99) {
				bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>【TAR完整性】输入有误！请重新输入.<big></font>", [ {
					label : "确定",
					"class" : "btn-primary"
				} ]);
			} else {
				tarCompleteness = tarCompleteness/100.0;
			}
		}
		
		var causalCheck = document.getElementById('causalCheck');
		if(causalCheck.checked == true) {
			causalCompleteness = $("#casual_completeness").val()*1;
			if(causalCompleteness == '') {
				causalCompleteness = 1.0;
			} else if(causalCompleteness < 1 || causalCompleteness >99) {
				bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>【因果完整性】输入有误！请重新输入.<big></font>", [ {
					label : "确定",
					"class" : "btn-primary"
				} ]);
			} else {
				causalCompleteness = causalCompleteness/100.0;
			}
		}
		
		var freqCheck = document.getElementById('freqCheck');
		if(freqCheck.checked == true) {
			freqCompleteness = $("#freq_completeness").val()*1;
			if(freqCompleteness == '') {
				freqCompleteness = 1.0;
			} else if(freqCompleteness < 1 || freqCompleteness >99) {
				bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>【频率完整性】输入有误！请重新输入.<big></font>", [ {
					label : "确定",
					"class" : "btn-primary"
				} ]);
			} else {
				freqCompleteness = freqCompleteness/100.0;
			}
		}
		
		var noNoise = document.getElementById('noNoise');
		if(noNoise.checked == true)
			noiseFlag = 1;
		
		var haveNoise = document.getElementById('haveNoise');
		var noiseDegree = 0.05;
		if(haveNoise.checked == true) {
			noiseFlag = 2;
			var noise1 = document.getElementById('noise1');
			var noise2 = document.getElementById('noise2');
			var noise3 = document.getElementById('noise3');
			var noise4 = document.getElementById('noise4');
			var noise5 = document.getElementById('noise5');
			
			if(noise1.checked == true)
				noiseType[0] = 1;
			if(noise2.checked == true)
				noiseType[1] = 1;
			if(noise3.checked == true)
				noiseType[2] = 1;
			if(noise4.checked == true)
				noiseType[3] = 1;
			if(noise5.checked == true)
				noiseType[4] = 1;
			
			noiseDegree = $("#noise_degree").val()*1;
			if(noiseDegree == '') {
				noiseDegree = 0.05;
			} else if(noiseDegree < 0 || noiseDegree >10) {
				bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>【噪音系数】输入有误(0-10%)！请重新输入.<big></font>", [ {
					label : "确定",
					"class" : "btn-primary"
				} ]);
			} else {
				noiseDegree = noiseDegree/100.0;
			}
		}
		
		if(inputFilePath == null || outputFileName == null) {
			bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>请先上传文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
		} else if(fileValid == false) {
			bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>文件不合法，请上传一个【.pnml】文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
		} else {
			var array = new Array();
			array.push(noiseType[0], noiseType[1], noiseType[2], noiseType[3], noiseType[4]);
			var params = array.join(",");
			customizable_loggenerate(dS1, tS, tarCompleteness, causalCompleteness, freqCompleteness, params, noiseFlag, noiseDegree, inputFilePath, outputFileName);
		}
	});
}

function customizable_loggenerate(dS1, tS, tarCompleteness, causalCompleteness, freqCompleteness, params, noiseFlag, noiseDegree, inputFilePath, outputFileName) {
	$.ajax({
		cache: true,
		type: "POST",
        url: 'loggenerator/cgenerate',
        data: {
        	dS1: dS1,
        	tS: tS,
        	tarCompleteness: tarCompleteness,
        	causalCompleteness: causalCompleteness,
        	freqCompleteness: freqCompleteness,
        	"noiseTypeString": params,
        	noiseFlag: noiseFlag,
        	noiseDegree: noiseDegree,
        	inputFilePath: inputFilePath, 
        	outputFileName: outputFileName
        },
        async: true,
        beforeSend:function(){
			$("#waitForResponse").modal('show');
		},
        error: function(request) {
        	$("#waitForResponse").modal('hide');
        	bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>日志生成过程出现问题，请重新上传文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
        },
        success: function(data) {
        	if(data.state == "SUCCESS") {
        		$("#waitForResponse").modal('hide');
        		show_generate_result(data.file_folder, data.file_user, data.file_type, data.file_name, data.file_suffix);
        	} else {
        		alert(data.message);
        	}
       }
	});
}

function show_generate_result(file_folder, file_user, file_type, file_name, file_suffix) {
	$("#download_file").html("<a href='uploadfiles/download/"+file_folder+"/"+file_user+"/"+file_type+"/"+file_name+"/"+file_suffix+"' class='btn btn-primary center' name='button'>下载【.mxml】文件</a>");
	$("#generate_result").show();
}

function default_loggenerate(inputFilePath, outputFileName) {
	alert("efault_loggenerate");
	$.ajax({
		cache: true,
		type: "POST",
        url: 'loggenerator/dgenerate',
        data: {
        	inputFilePath: inputFilePath, 
        	outputFileName: outputFileName
    	   },
    	async: true,
    	error: function(request) {
    		alert("Connection error");
    	},
    	success: function(data) {
    		if(data.state == "SUCCESS") {
    			alert("日志生成成功! 点击“确定”显示结果。");
        		show_generate_result(data.file_folder, data.file_user, data.file_type, data.file_name, data.file_suffix);
        	} else {
    			alert(data.message);
    		}
    	}
	});
}
