<table border="1">
<%
	java.util.Properties pop = System.getProperties();      
	for(java.util.Enumeration en = pop.keys();en.hasMoreElements();) {
		String key = (String)en.nextElement();	      
		String value = (String)pop.get(key);
		out.println("<tr>");
		out.println("<td>"+key+"</td>");
		out.println("<td>"+value+"</td>");
		out.println("</tr>");
	}
%>
</table>
