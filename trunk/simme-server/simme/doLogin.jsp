<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ page contentType="text/xml; charset=iso-8859-1"%>
<%@ page import="at.einspiel.simme.server.*"%>


<!--
   [Simme-Server]
          Java ServerPage: doLogin.jsp
                    $Date: 2004/09/02 10:15:30 $
                $Revision: 1.6 $
-->

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

        <jsp:useBean id="user" class="at.einspiel.simme.server.ManagedUser" scope="session">
            <jsp:setProperty name="user" property="nick" param="user" />
            <jsp:setProperty name="user" property="pwd" />
            <jsp:setProperty name="user" property="clientmodel" param="model" />
        </jsp:useBean>

        <%
            // login
            out.print(user.login((String) pageContext.getAttribute("version")).toString());
            // print user to console
            // TODO log this 
            log("Login request by user: " + request.getParameter("user"));
        %>
    </c:otherwise>
</c:choose>