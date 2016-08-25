var pageflag = "1_2_2";
function upload_log_file(){
	var inputFilePath = null;
	var fileValid = false;
	
	$('#logfileuploadsnm').fileupload({
		url:'uploadfiles/socialnetworkmining',
		autoUpload: true,
	    dataType: 'json',
	    done: function (e, data) {
	    	$("tr:has(td)").remove();
	        $.each(data.result, function (index, file) {
	        	// 返回需要的值
		    	inputFilePath = file.inputFilePath;
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
	
	$("#generate_mining_btn").click(function(){
		
		// choice
		var choice = 1;
		var handoverCheck = document.getElementById('handoverCheck');
		var workingtogetherCheck = document.getElementById('workingtogetherCheck');
		var simiartaskCheck = document.getElementById('simiartaskCheck');
		if(handoverCheck.checked == true)
			choice = 1;
		if(workingtogetherCheck.checked == true)
			choice = 2;
		if(simiartaskCheck.checked == true)
			choice = 3;
		
		// adjustment
		var adjustment = 0;
		adjustment = $("#adjustment").val()*1;
		if(adjustment == '') {
			adjustment = 20;
		}
		if(adjustment < 1 || adjustment >99 || adjustment == "NaN") {
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
			bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>文件不合法，请上传一个【.mxml】文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
		} else {
			generate_socialnetworkmining(choice, adjustment/100.0, inputFilePath);
		}
	});
}

function generate_socialnetworkmining(choice, adjustment, inputFilePath) {
	$.ajax({
        cache: true,
        type: "POST",
        url: 'socialnetworkmining/mining',
        data: { choice: choice, 
        		adjustment: adjustment, 
        		inputFilePath: inputFilePath
        },
        async: true,
        beforeSend:function(){
			$("#waitForResponse").modal('show');
		},
        error: function(request) {
        	$("#waitForResponse").modal('hide');
        	bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>分析过程出现问题，请重新上传文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
        },
        success: function(data) {
    		$("#waitForResponse").modal('hide');
        	if(data.state == "SUCCESS"){
        		show_minging_result(data.png);
        	} else {
        		alert(data.message);
        	}
        }
	});
}

function show_minging_result(png_path) {
	$("#png_result").html("<img src='"+png_path+"'/>");
	$("#download_picture").html("<a target='_blank' href='"+png_path+"' class='btn btn-primary center' name='button'>查看大图</a>");
	$("#minging_result").show();
}
