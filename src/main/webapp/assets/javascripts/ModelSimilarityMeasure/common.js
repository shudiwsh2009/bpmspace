var selectProcessType="";
var filepath1="",filepath2="";
var userId = "lvcheng";
var processType="PETRINET";
var algorithm;
var processId1="",processId2="";
//用于是第几个div在调用绘制工具
var divNum;
var web_root = webapproot;
var uploadfilepath1="",uploadfilepath2="";
var pageflag = "1_0_1";

(function() {
	$(document).ready(function() {	 					
		loadIndexList();			
	});
	 
	
  
	$("#btnCalculate").click(function(e) {
		algorithm=$("#algrotithmslist").val();
		
		if(uploadfilepath1!=""&&processId2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath:uploadfilepath1,
			    	  processId:processId2,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFileAndRepository',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		if(uploadfilepath1!=""&&filepath2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath1:uploadfilepath1,
			    	  filepath2:filepath2,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFile',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		if(uploadfilepath1!=""&&uploadfilepath2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath1:uploadfilepath1,
			    	  filepath2:uploadfilepath2,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFile',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		if(processId1!=""&&uploadfilepath2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath:uploadfilepath2,
			    	  processId:processId1,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFileAndRepository',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		if(filepath1!=""&&uploadfilepath2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath1:filepath1,
			    	  filepath2:uploadfilepath2,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFile',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		
		if(processId1!=""&&processId2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  processId1:processId1,
			    	  processId2:processId2,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInRepository',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		
		if(filepath1!=""&&filepath2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath1:filepath1,
			    	  filepath2:filepath2,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFile',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		
		if(filepath1!=""&&processId2!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath:filepath1,
			    	  processId:processId2,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFileAndRepository',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		if(filepath2!=""&&processId1!=""){
			$.ajax({
			      cache: true,
			      type: "POST",
			      data: {
			    	  filepath:filepath2,
			    	  processId:processId1,
			    	  algorithm:algorithm
			
			      },
			      url:'model/similarityInFileAndRepository',
			      async: true,
			      error: function(request) {
			          alert("Connection error");
			      },
			      success: function(data) {
			      	if(data.state == "SUCCESS"){
			      		$('#calculateResult h1').html("算法"+algorithm+"计算之后得到的结果:"+data.similarity);
			      	} else {
			      		alert(data.message);
			      	}
			      	
			      }
			  });
		}
		
	});
	
	
	
	
}).call(this);
	
//返回被选中的项，用来点击确定后操作
function fnGetSelected(oTableLocal) {
	return oTableLocal.$('tr.row_selected');
}

function loadIndexList(){
	 $.ajax({
      cache: true,
      type: "POST",
      url:'model/similarityAlgorithm',
      async: true,
      error: function(request) {
          alert("Connection error");
      },
      success: function(data) {
      	if(data.state == "SUCCESS"){
      		var list = eval(data.algorithms);
      		var htmlContent1 = "";
      		
      		var i = 0;
      		while(i < list.length){
      			var item = list[i];
      			htmlContent1 += "<option value='"+item+"'>" + item;
      			i++;
      		}
      	   $("#algrotithmslist").html(htmlContent1);
      	} else {
      		alert(data.message);
      	}
      	
      }
  });
}

//完成绘制后调用的方法
function setPath(value) {
	
	if(divNum==1){
		
		clearParams1();
		
		var path = value;
		var arr = new Array();
		arr = path.split('&');
		//svgPath = arr[0];
		//jsonPath = arr[1];
		//xmlPath=arr[2];
		filepath1 = arr[2];
		
		var str = arr[0].split('\\');
		
		var svgPath= str[str.length-1];
		svgPath="../backend/"+svgPath;
		
		$('#img1').attr("src",svgPath);
	}else{
		
		clearParams2();
		
		var path = value;
		var arr = new Array();
		arr = path.split('&');
		//svgPath = arr[0];
		//jsonPath = arr[1];
		//xmlPath=arr[2];
		filepath2 = arr[2];
		var str = arr[0].split('\\');
		
		var svgPath= str[str.length-1];
		svgPath="../backend/"+svgPath;
		
		$('#img2').attr("src",svgPath);
	}
	
	
	
}