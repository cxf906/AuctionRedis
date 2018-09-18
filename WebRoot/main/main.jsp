<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'main.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/themes/icon.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery.easyui.min.js"></script>
	<script type="text/javascript">
	    function qiang(isbn){
	    	$('#win').window({
			      title:'�����Ŷ��У������ĵȴ�......'
		     });
		    $('#win').window('open');  
		      
	         $.ajax({
			   type: "POST",
			   url: "<%=basePath%>AuctionSvl",
			   data: "isbn="+isbn,
			   success: function(msg){
				   $('#win').window('close');  
				   var dataArray = Array();
				   dataArray = msg.split("*");	
				   var left = dataArray[2];				  
				   if(parseInt(left)<0){	
					  $.messager.alert('xiaohp','����ʧ�ܣ�','error');
				   }else{
					  $.messager.alert('xiaohp',"�����ɹ�����ʣ" + parseInt(left) + "����Ʒ",'info');
				   }
			   }
			});
	    }
	</script>

  </head>
  
  <body>
    <table align="center" width=90%>
      <tr>
      	<td align=right><jsp:include page="mhead.jsp"></jsp:include>
      	</td>
      </tr>
      <tr>
      	<td>
      		<table border="1" width=100%>    
      		    <c:forEach var="bk" items="${books}">
      				<tr><td rowspan=3><img width=100 height=100 src="<%=basePath%>${bk.picurl}"/></td><td colspan=2 align=center style="color:red"><a href="<%=basePath%>BookDetailSvl?isbn=${bk.isbn}">${bk.bname}</a></td></tr>
       				<tr><td>��Ʒ�۸�</td><td>${bk.price}</td></tr>
       				<tr><td>������</td><td>${bk.press}</td></tr>
       				<c:if test="${bk.bkCount>0}">
       					<tr><td colspan=3 align="center"> <input type="button" onclick="qiang('${bk.isbn}')" value="����"></td></tr>
       				</c:if>           		       			
       		    </c:forEach>
    	</table>
      	</td>
      </tr>     
    </table>
    <div id="win" class="easyui-window" style="width:236px;height:55px"   
        		data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closable:false,closed:true">   
   				<img src="<%=basePath%>pic/roll.gif">
	</div>  
  </body>
</html>
