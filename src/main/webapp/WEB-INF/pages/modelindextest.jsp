<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Model Index Test</title>
<!-- / jquery -->
<script src='assets/javascripts/jquery/jquery.min.js' type='text/javascript'></script>
<script type="text/javascript">

function TestModelIndexConstruction(){
	var n = $("#modelIndexConstruction #n").val();
	var minT = $("#modelIndexConstruction #minT").val();
	var maxT = $("#modelIndexConstruction #maxT").val();
	var maxName = $("#modelIndexConstruction #maxName").val();
	
	$.ajax({
        cache: true,
        type: "POST",
        url:'indextest/modelIndexConstruction',
        data:{n:n, minT:minT,maxT:maxT,maxName:maxName},
        async: true,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {
        	alert(data.state);        	
        }
    });
}

function TestModelIndexQuery(){
	var n = $("#modelIndexQuery #n").val();
	
	$.ajax({
        cache: true,
        type: "POST",
        url:'indextest/modelIndexQuery',
        data:{n:n},
        async: true,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {
        	alert(data.state);        	
        }
    });
}
	
</script>


</head>
<body>
<hr>
<div id="modelIndexConstruction">
<h3>测试1：在下面输入参数进行模型索引创建测试</h3>
<table >
<tr><th>参数</th><th>值</th></tr>
<tr><td>模型数量</td><td><input type="text" id="n"></td></tr>
<tr><td>最小任务数</td><td><input type="text" id="minT"></td></tr>
<tr><td>最大任务数</td><td><input type="text" id="maxT"></td></tr>
<tr><td>任务名称长度最大值</td><td><input type="text" id="maxName"></td></tr>
</table>

<button onclick="TestModelIndexConstruction()">开始测试</button>
</div>
<hr>
<div id="modelIndexQuery">
<h3>测试2：在下面输入参数进行基于索引的模型检索测试</h3>
<table >
<tr><th>参数</th><th>值</th></tr>
<tr><td>模型数量</td><td><input type="text" id="n"></td></tr>
</table>

<button onclick="TestModelIndexQuery()">开始测试</button>
</div>
<hr>


</body>
</html>