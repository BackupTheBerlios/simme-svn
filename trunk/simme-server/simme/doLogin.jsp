<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ page import="at.einspiel.simme.server.*"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%/*
   [Simme-Server]
          Java ServerPage: doLogin.jsp
                    $Date: 2004/09/07 13:19:21 $
                $Revision: 1.7 $
*/%>
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

        <jsp:useBean id="user" class="at.einspiel.simme.server.ManagedUser">
            <jsp:setProperty name="user" property="nick" param="user" />
            <jsp:setProperty name="user" property="pwd" />
            <jsp:setProperty name="user" property="clientmodel" param="model" />
        </jsp:useBean>

        <%
            // login
            out.print(user.login((String) pageContext.getAttribute("version")).toString());
            // log user name 
            log("Login request by user: " + request.getParameter("user"));
        %>
    </c:otherwise>
</c:choose>