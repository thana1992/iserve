<%@ page info="SCCS id: $Id$"%>
<%@ page errorPage="/jsp/errorpage.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.fs.bean.util.*"%>
<%@ page import="com.fs.bean.misc.*"%>
<jsp:useBean id="fsAccessor" scope="session" class="com.fs.bean.util.AccessorBean"/>
<jsp:useBean id="fsScreen" scope="request" class="com.fs.dev.library.ScreenUtility"/>
<%
	String fs_lang = PageUtility.getDefaultLanguage(request);
	String fs_key = fsAccessor.getFsKey();
	if(fs_key==null || fs_key.trim().length()<=0) fs_key = session.getId();
	String fs_url = (String)request.getAttribute("fs_url");
	if(fs_url==null || fs_url.trim().length()==0) {
		String fs_forwarder = "/main/program_not_found.jsp";
		RequestDispatcher rd = application.getRequestDispatcher(fs_forwarder);
		rd.forward(request, response);
		return;
	}
	String fs_appid = (String)request.getAttribute("fs_appid");
	if(fs_appid==null) fs_appid = "";
	String fs_method = (String)request.getAttribute("fs_method");
	if(fs_method==null || fs_method.trim().length()==0) fs_method = "POST";
	String fs_user = fsAccessor.getFsUser()==null?"":fsAccessor.getFsUser();
	String fs_username = fsAccessor.getFsUserNaming()==null?"":fsAccessor.getFsUserNaming();
	String fs_site = fsAccessor.getFsSite()==null?"":fsAccessor.getFsSite();
	String fs_branch = fsAccessor.getFsBranch()==null?"":fsAccessor.getFsBranch();
%>
<!DOCTYPE html>
<html>
	<head>
		<title>Program Load</title>
		<jsp:include page="../jsp/meta.jsp"/>
		<link href="../img/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<script type="text/javascript">
			sessionStorage.setItem("TOKEN_KEY","<%=fs_key%>");
			function startprog() {
				fsformprogram.submit();
			}
		</script>
	</head>
	<body onload="startprog()">
		<form id="fsformprogram" name="fsformprogram" method="<%=fs_method%>" action="<%=fs_url%>">
			<input type="hidden" name="fsSeed" value="<%=System.currentTimeMillis()%>" />
			<input type="hidden" name="fsKey" value="<%=fs_key%>" />
			<input type="hidden" name="fsProg" value="<%=fs_appid%>" />
			<input type="hidden" name="userid" value="<%=fs_user%>" />
			<input type="hidden" name="username" value="<%=fs_username%>" />
			<input type="hidden" name="site" value="<%=fs_site%>" />
			<input type="hidden" name="branch" value="<%=fs_branch%>" />
			<input type="hidden" name="language" value="<%=fs_lang%>" />
		</form>
	</body>
</html>
