<%@ page info="SCCS id: $Id$"%>
<%@ page errorPage="/jsp/errorpage.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ page import="com.fs.bean.*" %>
<%@ page import="com.fs.bean.util.*"%>
<%@ page import="com.fs.bean.misc.*"%>

<jsp:useBean id="fsAccessor" scope="session" class="com.fs.bean.util.AccessorBean"/>
<jsp:useBean id="fsScreen" scope="request" class="com.fs.dev.library.ScreenUtility"/>
<jsp:useBean id="fsLabel" scope="request" class="com.fs.bean.util.LabelConfig"/>
<jsp:useBean id="fsPager" scope="request" class="com.fs.bean.util.Pager"/>
<jsp:useBean id="fsGlobal" scope="request" class="com.fs.bean.util.GlobalBean"/>
<%
final String fs_progid = PageUtility.getParameter(request,"appid");
if(fs_progid!=null && fs_progid.trim().length()>0) {
	fsScreen.init(fs_progid,request,response,true);
	fsScreen.validateAccessor(request,fs_progid);
	fsGlobal.setFsProg(fs_progid);
	fsGlobal.setFsSection("AUTH");
	fsGlobal.obtain(session);
	fsGlobal.obtain(fsAccessor);
	fsGlobal.obtain(fsPager);
	fsGlobal.setFsAction(GlobalBean.RETRIEVE_MODE);
	fsGlobal.scrape(request);
	com.fs.bean.ExecuteData fsExecuter = new com.fs.bean.ExecuteData() {
		public int retrieve(java.sql.Connection connection) throws Exception {
			int result = 0;
			KnSQL knsql = new KnSQL(this);
			knsql.append("select tprog.programid,tprog.progname,tprog.iconfile,tprog.shortname,tprog.progpath,tprod.url "); 
			knsql.append("from tprog ");
			knsql.append("left join tprod ON tprod.product = tprog.product "); 
			knsql.append("where tprog.programid = ?programid ");
			knsql.setParameter("programid",fs_progid);
			java.sql.ResultSet rs = knsql.executeQuery(connection);
			if(rs.next()) {
				result++;
				fetchResult(rs);
			}
			close(rs);
			return result;
		}	
	};
	com.fs.bean.ctrl.TheTransportor.transport(fsGlobal,fsExecuter);
	if(fsExecuter.effectedTransactions()>0) {
		String fs_url = fsExecuter.getString("url");
		String fs_path = fsExecuter.getString("progpath");
		String scheme = request.getScheme();             
		String serverName = request.getServerName(); 
		int serverPort = request.getServerPort(); 
		String aurl = scheme+"://"+serverName+(serverPort==80?"":":"+serverPort)+request.getContextPath();
		boolean isCrossUrl = false;
		if(fs_url!=null && fs_url.trim().length()>0) {
			if(fs_url.indexOf(":")>0) {
				aurl = fs_url;
				isCrossUrl = true;
			} else {
				aurl = scheme+"://"+serverName+(serverPort==80?"":":"+serverPort)+fs_url;
			}
		}
		boolean isHtml = false;
		if(fs_path!=null && fs_path.trim().length()>0) {
			isHtml = fs_path.indexOf(".html") > 0;
			aurl = aurl + fs_path;
			if(fs_path.indexOf(':')>0) {
				aurl = fs_path;
			}
		}
		String fs_lang = PageUtility.getDefaultLanguage(request);
		String fs_key = fsAccessor.getFsKey();
		if(fs_key==null || fs_key.trim().length()<=0) fs_key = session.getId();
		boolean redirect = false;
		if(redirect) {
			StringBuilder ubuf = new StringBuilder(aurl);
			ubuf.append("?seed=").append(System.currentTimeMillis());
			ubuf.append("&fsKey=").append(java.net.URLEncoder.encode(fs_key,"UTF-8"));
			ubuf.append("&fsProg=").append(fs_progid);
			if(fsAccessor.getFsUser()!=null) {
				ubuf.append("&userid=").append(java.net.URLEncoder.encode(fsAccessor.getFsUser(),"UTF-8"));
			}
			if(fsAccessor.getFsSite()!=null) {
				ubuf.append("&site=").append(java.net.URLEncoder.encode(fsAccessor.getFsSite(),"UTF-8"));
			}
			if(fsAccessor.getFsUserNaming()!=null) {
				ubuf.append("&username=").append(java.net.URLEncoder.encode(fsAccessor.getFsUserNaming(),"UTF-8"));
			}
			if(fsAccessor.getFsBranch()!=null) {
				ubuf.append("&branch=").append(java.net.URLEncoder.encode(fsAccessor.getFsBranch(),"UTF-8"));
			}
			Trace.debug("redirect to " + ubuf.toString());
			response.sendRedirect(ubuf.toString());
		} else {
			request.setAttribute("fs_appid",fs_progid);
			request.setAttribute("fs_url",aurl);
			request.setAttribute("fs_method",isHtml?"GET":"POST");
			String fs_forwarder = "/main/program_load.jsp";
			RequestDispatcher rd = application.getRequestDispatcher(fs_forwarder);
			rd.forward(request, response);			
		}
		return;
	} else {
		String fs_forwarder = "/main/program_not_found.jsp";
		RequestDispatcher rd = application.getRequestDispatcher(fs_forwarder);
		rd.forward(request, response);
		return;
	}
}
%>
