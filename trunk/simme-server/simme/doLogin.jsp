<%@page contentType="text/xml; charset=iso-8859-1"%>
<%@page import="at.einspiel.simme.server.base.User"%>
<%@page import="at.einspiel.simme.server.management.ManagedUser"%>
<%@page import="at.einspiel.simme.server.management.SessionManager"%>

<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@taglib prefix="x" uri="/WEB-INF/tlds/x.tld"%>

<c:choose>
    <c:when test="${(empty param.user) || (empty param.pwd)}">
        <%
            String msg = "Please enter username and password.";
            String title = "Error";
            out.print(SessionManager.makeMessage(title, msg));
        %>
    </c:when>
    <c:otherwise>
        <c:set var="nick" value="${param.user}" />
        <c:set var="pass" value="${param.pwd}"  />
        <c:set var="model" value="${param.model}" />
        <c:set var="version" value="${param.version}" />
    
        <jsp:useBean id="user" class="at.einspiel.simme.server.management.ManagedUser" scope="session">
            <jsp:setProperty name="user" property="nick" param="user" />
            <jsp:setProperty name="user" property="pwd" />
            <jsp:setProperty name="user" property="clientmodel" param="model" />
        </jsp:useBean>
    
        <%
            // retrieve the user from the session's context
            User u = (User) session.getAttribute("user");
            // retrieve the session manager
            SessionManager sMgr = SessionManager.getInstance();
            String requestUrl = request.getRequestURL().toString();
            sMgr.setBaseUrl(requestUrl.substring(0,requestUrl.lastIndexOf('/')));
            String version = (String) pageContext.getAttribute("version");
            // register user and return resulting messsage
            out.print(sMgr.addUser(u, version).toString());
            System.out.println(request.getParameter("user"));
        %>
    </c:otherwise>
</c:choose>