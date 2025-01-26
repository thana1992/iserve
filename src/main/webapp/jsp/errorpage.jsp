<%@ page isErrorPage="true" %>
<%@ page contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.fs.bean.util.*" %>
<jsp:useBean id="fsAccessor" scope="session" class="com.fs.bean.util.AccessorBean"/>
<jsp:useBean id="fsGlobal" scope="request" class="com.fs.bean.util.GlobalBean"/>
<jsp:setProperty name="fsGlobal" property="*"/>
<%
	String goMessage = "Go Back";
	String errorMessage = PageUtility.getParameter(request,"message");
	if((errorMessage==null) || errorMessage.trim().length()==0) {
		Object errorObject = request.getAttribute("error");
		if(errorObject!=null) errorMessage = errorObject.toString();
	}
	if((errorMessage==null) || errorMessage.trim().length()==0) {
		errorMessage = "General Protection Error Occured";
	}
	int errorCode = 0;
	if(exception!=null) {
		exception.printStackTrace();
		if(exception instanceof com.fs.bean.BeanException) {
			errorCode = ((com.fs.bean.BeanException)exception).getErrorCode();
		}
		if(exception instanceof javax.el.ELException) {
			javax.el.ELException ex = (javax.el.ELException)exception;
			if(ex.getCause() instanceof com.fs.bean.BeanException) {
				errorCode = ((com.fs.bean.BeanException)ex.getCause()).getErrorCode();
			}
		}
	}
	int statusCode = 400;
	if(errorCode==-4011 || errorCode==-4010 || errorCode==-4013 || errorCode==-4014) {
		statusCode = 401; 
	}
	response.setStatus(statusCode);
	String fs_context = request.getContextPath()+"/";
	if(fsGlobal.isAjax()) {
		fsGlobal.setThrowable(exception);
		fsGlobal.createResponseStatus(out, response);		
		return;
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<title>Error</title>
		<link href="../css/base_style.css" rel="stylesheet" type="text/css"/>
		<link href="../css/user_style.css" rel="stylesheet" type="text/css"/>
		<style nonce="YXNzdXJl">
			a { font-size: 1.5rem; text-decoration: none; } 
			.info { text-align: center; color:red; font-size: 1.2rem; }
			.text-center { text-align: center; }
			.portalbody { margin:0px; font-size:1rem; font-weight:400;line-height:1.5; background-color: whitesmoke; }
			#errorlayer { text-align:center; font-size: 16px; }
		</style>
		<script nonce="YXNzdXJl">
			function doGoBack() {
			<%if(errorCode==-4011 || errorCode==-4010) { goMessage = "Go Login"; %>
			try{
				var aurl = window.top.location.protocol+"//"+window.top.location.hostname+(window.top.location.port!=""?":"+window.top.location.port:"")+"<%=fs_context%>";
				window.top.location = aurl;
				//window.parent.top.location.reload(true);
				return;
			}catch(ex) { }
			window.history.back();
			<%} else {%>
			window.history.back();
			<%}%>
			}
			window.onload = function() {
				gobacklinker.onclick = doGoBack;
			}
		</script>
	</head>
	<body class="portalbody portalbody-off">
		<br></br><br></br>
		<div class="info">
			<%=errorMessage%>
		</div>
		<br></br>
		<div class="text-center">
			<a href="#" id="gobacklinker"><%=goMessage %></a>
		</div>
		<br></br>
		<div id="errorlayer">
		<%if(exception!=null && exception.getMessage()!=null && !exception.getMessage().equals("")) {%>
		<%=BeanUtility.preserveXML(exception.getMessage())%>
		<%}else{ String fs_exception = exception==null?"":""+exception;  %>
		<%=BeanUtility.preserveXML(fs_exception)%>
		<%}%>
		</div>
		<br></br>
	</body>
</html>