<%@page contentType="text/xml; charset=iso-8859-1"%>
<%@page import="at.einspiel.simme.server.base.User"%>
<%@page import="at.einspiel.simme.server.management.SessionManager"%>
<%@page import="at.einspiel.simme.server.messaging.ParsedObject"%>
<%@page import="java.util.Enumeration"%>

<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@taglib prefix="x" uri="/WEB-INF/tlds/x.tld"%>


<c:if test="${(param.user != '') || (param.pwd != '')}">

    <c:set var="nick" value="${param.user}" scope="session" />
    <c:set var="pass" value="${param.pwd}" scope="session" />
    <c:set var="model" value="${param.model}" />
    <c:set var="version" value="${param.version}" />

    <%
         String nick = (String)session.getAttribute("nick");
         String pass = (String)session.getAttribute("pass");
         String model = (String)pageContext.getAttribute("model");
         String version = (String)pageContext.getAttribute("version");
         User u = null;
         try {
            u = new User(nick, pass, null, (byte) 0, null, null, model);
         } catch (NoSuchMethodError err) {
            out.println(err.getMessage());
            return;
         }
         //String address = "<address>" + request.getRemoteAddr() + "</address>";
         //String host = "<host>" + request.getRemoteHost() + "</host>";
         SessionManager sMgr = SessionManager.getInstance();
         String requestUrl = request.getRequestURL().toString();
         sMgr.setBaseUrl(requestUrl.substring(0,requestUrl.lastIndexOf('/')));
         out.print(sMgr.addUser(u, version).toString());
    %>
</c:if>