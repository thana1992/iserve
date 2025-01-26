<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.fs.bean.misc.*" %>
<%@ page import="com.fs.bean.util.*" %>
<%
	StringBuilder result = new StringBuilder();
	String type = PageUtility.getParameter(request,"type");
	String key = PageUtility.getParameter(request,"key");
	String value = PageUtility.getParameter(request,"value");
	if(type!=null) {
		result.append("<type>"+type+"</type>\n");
		if(type.equalsIgnoreCase("version")) {
			result.append("<value>"+ new com.fs.Version().fetchVersion() + "</value>\n");
		} else if(type.equalsIgnoreCase("trace")) {
			if((key!=null) && (value!=null)) {
				result.append("<key>"+key+"</key>\n");
				result.append("<value>"+value+"</value>\n");
				boolean enableMode = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on");
				if(key.equals("info")) {
					Trace.setInfoMode(enableMode);
				} else if(key.equals("debug")) {
					Trace.setDebugMode(enableMode);
				} else if(key.equals("error")) {
					Trace.setErrorMode(enableMode);
				} else if(key.equals("warning")) {
					Trace.setWarningMode(enableMode);
				} else if(key.equals("header")) {
					Trace.setHeaderMode(enableMode);
				} else if(key.equals("console")) {
					Trace.setConsoleMode(enableMode);
				} else if(key.equals("class")) {
					Trace.setClassMode(enableMode);
				} else if(key.equals("logger")) {
					Trace.setLoggerMode(enableMode);
				}
				System.out.println(key.toUpperCase()+" "+(enableMode?"enable":"disable"));
			}
		} else if(type.equalsIgnoreCase("global")) {
			if((key!=null) && (value!=null)) {
				result.append("<key>"+key+"</key>\n");
				result.append("<value>"+value+"</value>\n");
				GlobalVariable.setVariable(key,value);
				System.out.println(type.toUpperCase()+" setting "+key+" = "+value);
			}
		} else if(type.equalsIgnoreCase("getglobal")) {
			if(key!=null) {
				result.append("<key>"+key+"</key>\n");
				result.append("<value>"+GlobalVariable.getVariable(key)+"</value>\n");				
			}
		} else if(type.equalsIgnoreCase("reload")) {
			GlobalVariable.reload();
		}
	} else result.append("Type is undefined");
%>
{"type":"result", "body": "<%=result.toString()%>"}
