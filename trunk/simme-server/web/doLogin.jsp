<%@ page language="java" %>
<%@ page contentType="text/xml; charset=iso-8859-1" %>
<%@ page import="at.einspiel.simme.server.base.*, at.einspiel.simme.server.management.*, at.einspiel.simme.server.messaging.*" %>
<%@ page import="test.sim.net.*" %>
<%@ page import="java.util.Enumeration" %>
<%@ page errorPage="error.jsp" %>
<%

String nick="", pass="", model="", version="";

nick = request.getParameter("user");
pass = request.getParameter("pwd");
version = request.getParameter("version");
model = request.getParameter("model");      	

if ((nick == null) || (pass == null)) {

	System.out.println("---- params ----");
   Enumeration enum = request.getParameterNames();
   while (enum.hasMoreElements()) {
      String name = (String) enum.nextElement();
      System.out.println(name + "=" + request.getParameter(name));
   }	
   System.out.println("---- params ----");

   String xmlMessage = request.getParameter("xmldata");
   System.out.println(xmlMessage);
   if (xmlMessage != null) {
      try {
      	ParsedObject po = ParsedObject.parse(xmlMessage);
      	nick = po.getAttribute("user");
      	pass = po.getAttribute("pwd");
			version = po.getAttribute("version");
			model = po.getAttribute("model");      	
      	
      } catch (Exception e) {
         ; // ??
      }
   }
}

if ((nick != null) && (pass != null)) {
   User u = new User(nick, pass, null, (byte)0, null, null, model);
   String address = "<address>"+request.getRemoteAddr() + "</address>";
   String host = "<host>"+request.getRemoteHost() + "</host>";
   SessionManager sMgr = SessionManager.getInstance();
   out.println(sMgr.addUser(u).toString());
}

%>