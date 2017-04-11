<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/websocket.js"></script>
		<script type="text/javascript">
			var userid=prompt("用户ID:");
			jQuery(document).ready(function(){
				var jsocket=jQuery.websocket({
					domain :window.location.hostname,
					port : 9696,	
					protocol : 'websocket',
					onOpen:function(event){
			            jQuery("textarea[readonly=readonly]").append("连接成功!\n");
			        },  
			        onError:function(event){  
			        	jQuery("textarea[readonly=readonly]").append("连接错误!\n");
			        },
			        onMessage:function(msg){  
			           if(msg){
			           		jQuery("textarea[readonly=readonly]").append(msg+"\n");
			           }
			        }
				});
				var send=function(event){
					if(this.type=="button"){
						var text=jQuery("input[type=text]");
						if(text.val()){
							jsocket.send("{\"userid\":\""+userid+"\",\"value\":\""+text.val()+"\"}");
							text.val("");
						}
					}
					jQuery("input[type=text]").focus();
				};
				jQuery("input[type=text]").keydown(send);
				jQuery("input[type=button]").click(send);
			});
    </script>
  </head>
  
  <body>
   <div>
			<input type="text">
			<input type="button" value="发送">
		</div>
		<textarea rows="20" cols="50" readonly="readonly"></textarea>
  </body>
</html>
