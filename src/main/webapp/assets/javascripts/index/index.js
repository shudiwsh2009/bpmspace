/**
 * author: chenhz
 */
var pageflag = "0_2_1";
String.prototype.format = function(args) {
    var result = this;
    if (arguments.length > 0) {    
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if(args[key]!=undefined){
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                	var reg= new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
};

var uploadJarFile = null;


function loadIndexList(category){
	 $.ajax({
         cache: true,
         type: "POST",
         url:'index/il',
         data:{cat:category},
         async: true,
         error: function(request) {
             alert("Connection error");
         },
         success: function(data) {
         	if(data.state == "SUCCESS"){
         		var list = eval(data.list);
         	    var indexRowTemplete = "<tr id='{0}'><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td>";
         	    indexRowTemplete += "<td><div class='text-center'>{4}</div></td></tr>";
         		var htmlContent = "";
         		var i = 0;
         		var state="启动";
         		var btn ="";
         		while(i < list.length){
         			var item = list[i];
         			if(item.state =="START"){
         				state = "启动";
         				btn = "<a class='btn btn-danger btn-mini' href='#'><i class='icon-remove'></i></a>";
         			}
         			else if(item.state =="STOP"){ 
         				state = "停止";
         				btn = "<a class='btn btn-success btn-mini' href='#' ><i class='icon-ok'></i></a>";
         			}else{   //item.state = UNAUTHORIZED
         				state = "未授权";
         				btn = "";
         			}
         			var indexRowContent = indexRowTemplete.format(item.id,item.class_name,item.description,state,btn);
         			htmlContent += indexRowContent;
         			i++;
         		}
         	   $("#constructed_index_list").html(htmlContent);
         	  bind_index_start_action();
         	  bind_index_stop_action();
         	} else {
         		alert(data.message);
         	}
         	
         }
     });
}

function bind_index_stop_action(){
	$("#constructed_index_list tr td a .icon-remove").unbind("click");
	
	$("#constructed_index_list tr td a .icon-remove").click(function(){
		//show a confirm dialog
		//stop the index 
		var id = $(this).parent().parent().parent().parent().attr("id");
		if(id == null || id == undefined){
			alert("Fail due to html.");
			return;
		}
		 $.ajax({
	         cache: true,
	         type: "POST",
	         url:'index/stop',
	         data:{iid:id},// 
	         async: true,
	         error: function(request) {
	             alert("Connection error");
	         },
	         success: function(data) {
	         	if(data.state == "SUCCESS"){
	         		var sel = "#constructed_index_list #"+data.iid;
	         		$(sel).children().eq(3).html("停止");
	         		$(sel).children().eq(4).html("<a class='btn btn-success btn-mini' href='#' ><i class='icon-ok'></i></a>");
	         		bind_index_start_action();
	         	} else {
	         		alert(data.message);
	         	}
	         }
	     });
	});
}


function bind_index_start_action(){
	$("#constructed_index_list tr td a .icon-ok").unbind("click");
	
	$("#constructed_index_list tr td a .icon-ok").click(function(){
		//show a confirm dialog
		//start the index 
		
		var id = $(this).parent().parent().parent().parent().attr("id");
		if(id == null || id == undefined){
			alert("Fail due to html.");
			return;
		}
		 $.ajax({
	         cache: true,
	         type: "POST",
	         url:'index/start',
	         data:{iid:id},// 
	         async: true,
	         error: function(request) {
	             alert("Connection error");
	         },
	         success: function(data) {
	         	if(data.state == "SUCCESS"){
	         		var sel = "#constructed_index_list #"+data.iid;
	         		$(sel).children().eq(3).html("启动");
	         		$(sel).children().eq(4).html("<a class='btn btn-danger btn-mini' href='#'><i class='icon-remove'></i></a>");
	         		bind_index_stop_action();
	         	} else {
	         		alert(data.message);
	         	}
	         }
	     });
	});
}

function bind_upload_jar_file(){
	$('#modelfileupload').fileupload({
		url:'uploadfiles/modeljarfile',
		autoUpload: true,
	    dataType: 'json',
	    done: function (e, data) {
	    	$("tr:has(td)").remove();
	        $.each(data.result, function (index, file) {
	        	// 返回需要的值
			    uploadJarFile = file.inputFilePath;
			    
	            $("#uploaded-files").append(
	            		$('<tr/>')
	            		.append($('<td/>').text(file.fileName))
	            		.append($('<td/>').text(file.fileSize))
	            		.append($('<td/>').text(file.fileType))
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
}


function bind_register_index(category){
		
	$("#index_register_btn").click(function(){
		var cn = $("#index_classname_input").val();
		if(cn == undefined || cn == ""){
			alert("索引类名为空，请输入！");
			return;
		}
		var desc = $("#index_desc_input").val();
		if(uploadJarFile == undefined || uploadJarFile == null){
			alert("索引文件路径为空，请选择！");
			return;
		}
		
		register_index(category,cn,desc);	
	});
}


function register_index(category,cn,desc){
	//add to database 
	 $.ajax({
        cache: true,
        type: "POST",
        url:'index/register',
        data:{cat:category,cn:cn,desc:desc},// 
        async: true,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {
        	if(data.state == "SUCCESS"){
        		//update html
        		var indexRowTemplete = "<tr id='{0}'><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td>";
          	    indexRowTemplete += "<td><div class='text-center'><a class='btn btn-success btn-mini' href='#' ><i class='icon-ok'></i></a></div></td></tr>";
	          	cn = cn.substr(cn.lastIndexOf('.')+1);
          	    var htmlContent = indexRowTemplete.format(data.iid,cn,desc,"停止");
          	    $("#constructed_index_list").append(htmlContent);
	         	bind_index_start_action();
	         	bind_index_stop_action();
	         	uploadJarFile = null;
        	} else {
        		alert(data.message);
        	}
        }
    });
	
}



function bind_index_panel_header_event(){
	$("#constrcued_index_panel").hover(function(){
		$("#constrcued_index_panel .box-header").animate({backgroundColor : '#f8a326'}, 3000);
	},function(){
		$("#constrcued_index_panel .box-header").animate({backgroundColor : '#fffff'}, 1000);
	});
	$("#unconstrcued_index_panel").hover(function(){
		$("#unconstrcued_index_panel .box-header").animate({backgroundColor : '#f8a326'}, 3000);
	},function(){
		$("#unconstrcued_index_panel .box-header").animate({backgroundColor : '#fffff'}, 1000);
	});	
}
