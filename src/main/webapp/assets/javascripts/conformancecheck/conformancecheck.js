function upload_log_file(){
	var inputFilePath = null;
	var fileValid = false;
	
	$('#logfileuploadcc').fileupload({
		url:'uploadfiles/conformancecheck',
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
	
	$("#conformancecheck_btn").click(function(){
		// pre...
		var fitness = false;
		var f = false;
		var pSE = false;
		var pPC = false;
		var precision = false;
		var saB = false;
		var aaB = false;
		var structure = false;
		var saS = false;
		var aaS = false;
		
		// fitness
		var fCheck = document.getElementById('f_check');
		if(fCheck.checked == true) {
			f = true;
			fitness = true;
		}
		var pSECheck = document.getElementById('pSE_check');
		if(pSECheck.checked == true) {
			pSE = true;
			fitness = true;
		}
		var pPCCheck = document.getElementById('pPC_check');
		if(pPCCheck.checked == true) {
			pPC = true;
			fitness = true;
		}
		
		// precision
		var saBCheck = document.getElementById('saB_check');
		if(saBCheck.checked == true) {
			saB = true;
			precision = true;
		}
		var aaBCheck = document.getElementById('aaB_check');
		if(aaBCheck.checked == true) {
			aaB = true;
			precision = true;
		}
		
		// structure
		var saSCheck = document.getElementById('saS_check');
		if(saSCheck.checked == true) {
			saS = true;
			structure = true;
		}
		var aaSCheck = document.getElementById('aaS_check');
		if(aaSCheck.checked == true) {
			aaS = true;
			structure = true;
		}
		
		// mining alogorithm
		var algorithm = $("#mining_algorithm_selected").val();
		if(algorithm == undefined || algorithm == "") {
			bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>请选择一个挖掘算法！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
		}
		
		// input file check
		if(inputFilePath == null) {
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
			generate_conformancecheck(inputFilePath, algorithm, fitness, f, pSE, pPC, precision, saB, aaB, structure, saS, aaS);
		}
	});
}

function generate_conformancecheck(inputFilePath, algorithm, fitness, f, pSE, pPC, precision, saB, aaB, structure, saS, aaS) {
	$.ajax({
        cache: true,
        type: "POST",
        url: 'conformancecheck/ccheck',
        data: { inputFilePath: inputFilePath, 
        		algorithm: algorithm, 
        		fitness: fitness,
        		f: f,
        		pSE: pSE,
        		pPC: pPC,
        		precision: precision,
        		saB: saB,
        		aaB: aaB,
        		structure: structure,
        		saS: saS,
        		aaS: aaS
        	   },
        async: true,
        beforeSend:function(){
			$("#waitForResponse").modal('show');
		},
        error: function(request) {
        	$("#waitForResponse").modal('hide');
            bootbox.dialog("<font color='#00acec' face='微软雅黑'><big>校验过程出现问题，请重新上传文件！<big></font>", [ {
				label : "确定",
				"class" : "btn-primary"
			} ]);
        },
        success: function(data) {
        	if(data.state == "SUCCESS"){
        		$("#waitForResponse").modal('hide');
        		show_checking_result(data.f_fitness, data.p_sBA, data.p_aBA, data.p_dMF, data.s_sSA, data.s_aSA);
        	} else {
        		alert(data.message);
        	}
        }
	});
}

function show_checking_result(f_fitness, p_sBA, p_aBA, p_dMF, s_sSA, s_aSA) {
	if(!(f_fitness < 0.0)) {
		$("#f_fitness").html("<b><big>&nbsp;"+f_fitness+"</big></b>");
	} else {
		$("#f_fitness").html("<b><big>&nbsp;无此项</big></b>");
	}
	
	if(!(p_sBA < 0.0)) {
		$("#p_sBA").html("<b><big>&nbsp;"+p_sBA+"</big></b>");
	} else {
		$("#p_sBA").html("<b><big>&nbsp;无此项</big></b>");
	}
	
	if(!(p_aBA < 0.0)) {
		$("#p_aBA").html("<b><big>&nbsp;"+p_aBA+"</big></b>");
	} else {
		$("#p_aBA").html("<b><big>&nbsp;无此项</big></b>");
	}
	
	if(!(p_dMF < 0.0)) {
		$("#p_dMF").html("<b><big>&nbsp;"+p_dMF+"</big></b>");
	} else {
		$("#p_dMF").html("<b><big>&nbsp;无此项</big></b>");
	}
	
	if(!(s_sSA < 0.0)) {
		$("#s_sSA").html("<b><big>&nbsp;"+s_sSA+"</big></b>");
	} else {
		$("#s_sSA").html("<b><big>&nbsp;无此项</big></b>");
	}
		
	if(!(s_aSA < 0.0)) {
		$("#s_aSA").html("<b><big>&nbsp;"+s_aSA+"</big></b>");
	} else {
		$("#s_aSA").html("<b><big>&nbsp;无此项</big></b>");
	}
	
	$("#checking_result").show();
}
