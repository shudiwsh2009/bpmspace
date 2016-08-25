<%@page import="java.util.ArrayList"%>
<%@page import="com.chinamobile.bpmspace.core.domain.process.ProcessCatalog"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ArrayList<ProcessCatalog> catalogList = (ArrayList<ProcessCatalog>)(request.getAttribute("catalogList"));
%>
<%
	String userId = (String)request.getAttribute("userId");
%>
<%
	String catalogId = (String)request.getAttribute("catalogId");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>用户目录</title>
	<style type = "text/css">
		td{
			width:60px;
		}
	</style>
	<script type="text/javascript">
        function chickAll(){
        // 全选方法
            var chickobj = document.getElementsByName("num");
            for(var i = 0 ; i<chickobj.length ; i++){
                chickobj[i].checked = "checked";
            }
        }
        function Nochick(){
        // 反选方法   
            var chickobj = document.getElementsByName("num");
            for(var i = 0 ; i<chickobj.length ; i++){
                chickobj[i].checked = !chickobj[i].checked;
            }
        }
    </script>
</head>
<body>
	<div id = "main">
		<form name="form1" action ="delete" method = "post">
			<table border ="1" align = "center" style="border-collapse:collapse;">
				<tr align="center">
               		<td colspan="7">目录列表</td>
            	</tr>
            	<tr align="center">
                	<td></td>
                	<td>名称</td>
                	<td>类型</td>
                	<td>创建时间</td>
               		<td colspan="2">操作</td>
            	</tr>
            	
            	<%
            	            		for(int i = 0; i < catalogList.size(); ++i) {
            	            	            		ProcessCatalog catalog = catalogList.get(i);
            	            	%>
            		<tr align="center">
            			<td><input type="checkbox" value="<%=catalog.getId()%>" name="num"/></td>
            			<td><a href="/bpmspace/catalog/<%=userId %>/<%=catalog.getId()%>"><%=catalog.getName()%></a></td>
            			<td><%=catalog.getType()%></td>
            			<td></td>
            			<td><a href="/bpmspace/catalog/deteleCatalog?catalogId=<%=catalog.getId()%>">删除</a></td>
            			<td></td>
            		</tr>
           		<%
           		}
           		%>
			</table>
			<table align = "center">
				<tr>
					<td><input type ="button" value ="全选" name="checkall" id = "checkall" onclick="chickAll()"/></td>
					<td><input type ="button" value ="反选" name="nocheck" id= "nocheck" onclick="Nochick()"/></td>
					<td><input type ="submit" value ="批量删除"/></td>
				</tr>
			</table>
		</form>
		<form name="form2" action="/bpmspace/catalog/addCatalog" method="post">
			<table align = "center" border="1" style="border-collapse: collapse;">
			    <tr>
			       	<td>
			       		<input type="hidden" name="userId" value="<%=userId%>"/>
			       		<input type="hidden" name="catalogId" value="<%=catalogId%>"/>
			       		<input type="text" name="name"/>
			       		<span style = "color:red; font-size:13px;" id = "td2">${addMessage}</span>
			       		<input type="submit" value="新建" />
			       	</td>
			    </tr>
		    </table>
		</form>
	</div>
</body>
</html>