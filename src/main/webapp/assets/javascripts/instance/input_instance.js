function upload_log_file(){
	
	$('#fileuploadInstance').fileupload({
		url:'uploadfiles/input_instance',
		autoUpload: true,
        dataType: 'json',
        done: function (e, data) {
        	alert("pppp");
        	var node = $("#instanceDB_tree").dynatree("getActiveNode");
        	$.each(data.result, function (index, file) {
	        	// 返回需要的值
		    	logName = file.fileName;
			    logId = file.fileId;
			    
			    var key = logId;
				var childNode = node.addChild({
					title : logName,
					key : key,
					isFolder : 'false'
				});
	        }); 
	    },
	});
}
