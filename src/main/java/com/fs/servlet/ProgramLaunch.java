package com.fs.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fs.bean.util.AccessorBean;
import com.fs.bean.util.GlobalBean;
import com.fs.bean.util.PageUtility;
import com.fs.bean.misc.KnSQL;
import com.fs.bean.misc.Trace;
import com.fs.dev.Console;
import com.fs.dev.library.ScreenUtility;

/**
 * To launch program service engine
 * 
 * @author tassan_oro@freewillsolutions.com
 */
@SuppressWarnings({"serial","rawtypes"})
public class ProgramLaunch extends HttpServlet implements java.io.Serializable {

	public ProgramLaunch() {
		super();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Console.out.println("-----------------------------------------------------------------");
		Console.out.println(getClass().getName()+" servlet initialize ...");
		Console.out.println("------------------------------------------------------------------");
		Console.out.println("init on "+config.getServletContext().getRealPath(""));		
		for(java.util.Enumeration en = config.getInitParameterNames();en.hasMoreElements();) {
			String key = (String)en.nextElement();
			Console.out.println(key+"="+config.getInitParameter(key));
		}
	}
	
	public AccessorBean getAccessor(HttpServletRequest request) {
		AccessorBean fsAccessor = (AccessorBean)ScreenUtility.getObjectName(request,"fsAccessor");
		if(fsAccessor==null) {
			fsAccessor = new AccessorBean();
			request.getSession().setAttribute("fsAccessor", fsAccessor);
		}
		if(!fsAccessor.isValid()) {
			String tokenkey = ScreenUtility.getAccessToken(request);
			Trace.debug(this,"access token = "+tokenkey);
			if(tokenkey!=null && tokenkey.trim().length()>0) {
				fsAccessor.setFsKey(tokenkey);
			}
		}
		return fsAccessor;
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			final String fs_progid = PageUtility.getParameter(request,"appid");
			if(fs_progid!=null && fs_progid.trim().length()>0) {
				HttpSession session = request.getSession();
				AccessorBean fsAccessor = getAccessor(request);
				Trace.debug(this,"get accessor = "+fsAccessor);
				com.fs.dev.library.ScreenUtility fsScreen = new com.fs.dev.library.ScreenUtility();
				com.fs.bean.util.GlobalBean fsGlobal = new com.fs.bean.util.GlobalBean();
				fsScreen.init(fs_progid,request,response,true);
				fsScreen.validateAccessor(request,fs_progid);
				Trace.debug(this,"accessor = "+fsAccessor);
				fsGlobal.setFsProg(fs_progid);
				fsGlobal.setFsSection("AUTH");
				fsGlobal.obtain(session);
				fsGlobal.obtain(fsAccessor);
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
						try(java.sql.ResultSet rs = knsql.executeQuery(connection)) {
							if(rs.next()) {
								result++;
								fetchResult(rs);
							}
						}
						return result;
					}	
				};
				fsExecuter.transport(fsGlobal);
				if(fsExecuter.effectedTransactions()>0) {
					String fs_url = fsExecuter.getString("url");
					String fs_path = fsExecuter.getString("progpath");
					String scheme = request.getScheme();             
					String serverName = request.getServerName(); 
					int serverPort = request.getServerPort(); 
					String aurl = scheme+"://"+serverName+(serverPort==80?"":":"+serverPort)+request.getContextPath();
					if(fs_url!=null && fs_url.trim().length()>0) {
						if(fs_url.indexOf(':')>0) {
							aurl = fs_url;
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
						ubuf.append("&language=").append(fs_lang);
						Trace.debug("redirect to : " + ubuf.toString());
						response.sendRedirect(ubuf.toString());
					} else {
						request.setAttribute("fs_appid",fs_progid);
						request.setAttribute("fs_url",aurl);
						request.setAttribute("fs_method",isHtml?"GET":"POST");
						String fs_forwarder = "/main/program_load.jsp";
						request.getRequestDispatcher(fs_forwarder).forward(request, response);
					}
					return;
				} else {
					String fs_forwarder = "/main/program_not_found.jsp";
					request.getRequestDispatcher(fs_forwarder).forward(request, response);
					return;
				}
			}
		} catch(Exception exc) {
			Trace.error(this,exc);
            request.setAttribute("exception", exc);
            request.setAttribute("error", exc.getMessage());
			String fs_forwarder = "/jsp/errorpage.jsp";
			request.getRequestDispatcher(fs_forwarder).forward(request, response);
			return;			
		}
	}
	
}
