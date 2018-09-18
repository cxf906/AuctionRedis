<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script language="javascript" src="<%=basePath%>script/jquery-1.4.4.min.js"></script>
<script language="javascript">
	$(document).ready(function(){
		  $("#btn").click(function(){
		    doGetMsg();
		  });  
	});

	function doGetMsg()
	{
		$("#table1 tr.mytr").remove();			//删除上次操作相关的行
		for(var i=0; i<20; i++)
		{
			getMsg(i);
		}
	}
	function getMsg(i)
	{
		$.ajax({ 
			url: "<%=basePath%>AuctionSvl?isbn=is001", 				//数据请求页面的url
			type:"get", 						           //数据传递方式(get或post)
			dataType:"html", 					//期待数据返回的数据格式(例如 "xml", "html", "script",或 "json")
			timeout:50000, 						//超时时间
			cache:false,						//缓存
			dataFilter:function (data, type) 
			{
			    return data;					 // 返回预处理后的数据
			},
			success:function(data,testStatus)    //当请求成功时触发的回调函数
			{				
				var dataArray = Array();
				dataArray = data.split("*");
								
				/*填充表格*/
				$("#table1").append("<tr id='tr"+i+"' class='mytr'></tr>");
				for(var j=0; j<dataArray.length; j++)
				{
					$("#tr"+i).append("<td>"+dataArray[j]+"</td>");
				}				
				
			},
			error:function(msg) //当请求失败时触发的函数
			{
				
			}
		});
				
	}
</script>
</head>
<body >
	<form id="form1">
		<table id="table1" width="800" border="1" align="center" cellspacing="0" bordercolor="#000000">
			<tr>
				<th>userno</th><th>threadName</th><th>剩余数量</th>
			</tr>
			<tr><td colspan=3 align=center><input type="button" id="btn" value="start" /></td></tr>	   
		</table>
			
	</form>
	
</body>
</html>

