<%@ page contentType="application/json; charset=UTF-8"%>
<%System.out.println("session : "+request.getSession().getId());%>
{"type":"result", "body": "<%=request.getSession().getId()%>"}
