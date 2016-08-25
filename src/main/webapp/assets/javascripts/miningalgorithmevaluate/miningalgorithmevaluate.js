var pageflag = "1_2_1";
function upload_model_file(){
	var inputFilePath = null;
	var outputFileName = null;
	var fileValid = false;
	
	$('#modelfileuploadmae').fileupload({
		url:'uploadfiles/miningevaluator',
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
	
	$("#miningalgorithmevaluate_btn").click(function(){
		
		// miningAlgorithm
		var miningAlgorithm = $("#miningAlgorithm_selected").val();
		if(miningAlgorithm == undefined || miningAlgorithm == "") {
			alert("Please choose a minging algorithm!");
		}
		
		// logGenerateAlgorithm
		var logGenerateAlgorithm = $("#logGenerateAlgorithm_selected").val();
		if(logGenerateAlgorithm == undefined || logGenerateAlgorithm == "") {
			alert("Please choose a log generate algorithm!");
		}
		
		// similarityAlgorithm
		var similarityAlgorithm = $("#similarityAlgorithm_selected").val();
		if(similarityAlgorithm == undefined || similarityAlgorithm == "") {
			alert("Please choose a minging algorithm!");
		}
		
		// similarityStrAlgorithm
		var similarityStrAlgorithm = $("#similarityStrAlgorithm_selected").val();
		if(similarityStrAlgorithm == undefined || similarityStrAlgorithm == "") {
			alert("Please choose a minging algorithm!");
		}
		
		// completeness
		var completeness = 100;
		completeness = $("#completeness").val()*1;
		if(completeness == '') {
			completeness = 100;
		}
		if(completeness < 1 || completeness >100 || completeness == "NaN") {
			bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>【关联系数】输入有误！请重新输入.<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
		} else if(inputFilePath == null) {
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
			miningalgorithmevaluate(inputFilePath, outputFileName, miningAlgorithm, logGenerateAlgorithm, similarityAlgorithm, similarityStrAlgorithm, completeness/100.0);
		}
	});
}

function miningalgorithmevaluate(inputFilePath, outputFileName, miningAlgorithm, logGenerateAlgorithm, similarityAlgorithm, similarityStrAlgorithm, completeness) {
	$.ajax({
		cache: true,
		type: "POST",
        url: 'miningevaluator/evaluate',
        data: {
        	inputFilePath: inputFilePath, 
        	outputFileName: outputFileName,
        	miningAlgorithm: miningAlgorithm,
			logGenerateAlgorithm: logGenerateAlgorithm,
			similarityAlgorithm: similarityAlgorithm,
			similarityStrAlgorithm: similarityStrAlgorithm,
			completeness: completeness
    	   },
    	async: true,
    	beforeSend:function(){
			$("#waitForResponse").modal('show');
		},
    	error: function(request) {
    		$("#waitForResponse").modal('hide');
    		bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>评估过程出现问题，请重新上传文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
    	},
    	success: function(data) {
    		if(data.state == "SUCCESS") {
    			$("#waitForResponse").modal('hide');
    			show_evaluating_result(data.averageSimilarity, data.meanDeviation, data.averageStrSimilarity, data.meanStrDeviation, data.averageSim, data.file_folder, data.file_user, data.file_type, data.file_name, data.file_suffix);
    		} else {
    			alert(data.message);
    		}
    	}
	});
}

function show_evaluating_result(averageSimilarity, meanDeviation, averageStrSimilarity, meanStrDeviation, averageSim, file_folder, file_user, file_type, file_name, file_suffix) {
	$("#abs").html("<b><big>&nbsp;"+averageSimilarity+"</big></b>");
	$("#mbd").html("<b><big>&nbsp;"+meanDeviation+"</big></b>");
	$("#ass").html("<b><big>&nbsp;"+averageStrSimilarity+"</big></b>");
	$("#msd").html("<b><big>&nbsp;"+meanStrDeviation+"</big></b>");
	$("#as").html("<b><big>&nbsp;"+averageSim+"</big></b>");
	$("#download_file").html("<a href='uploadfiles/download/"+file_folder+"/"+file_user+"/"+file_type+"/"+file_name+"/"+file_suffix+"' class='btn btn-primary center' name='button'>下载【.xls】文件</a>");
	
	$("#evaluating_result").show();
}