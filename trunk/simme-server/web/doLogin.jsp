<%@ page language="java" %> 
<%@ page contentType="text/xml; charset=iso-8859-1" %> 
<%@ page import="at.einspiel.simme.server.base.*" %>
<%@ page import="at.einspiel.simme.server.management.*" %>
<%@ page import="at.einspiel.simme.server.messaging.*" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="test.sim.net.*" %>
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
   
   
   // test if a user with this combination exists
   
   
   User u = new User(nick, pass, null, (byte)0, null, null, model);

   String address = "<address>"+request.getRemoteAddr() + "</address>";
   String host = "<host>"+request.getRemoteHost() + "</host>";

   String content = "<user nick=\"" + u.getNick() +"\" pass=\"" + u.getPassword() + "\">" + address + host + "</user>";

   SessionManager sMgr = SessionManager.getInstance();

   out.println(sMgr.addUser(u).toString());
}

%>