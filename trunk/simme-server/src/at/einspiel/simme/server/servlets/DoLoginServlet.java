package at.einspiel.simme.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.einspiel.simme.server.base.User;
import at.einspiel.simme.server.management.SessionManager;
import at.einspiel.simme.server.messaging.ParsedObject;

/**
 * 
 * @author kariem
 */
public class DoLoginServlet extends HttpServlet {

   /** @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse) */
   protected void doGet(
      HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

      response.setContentType("text/xml; charset=iso-8859-1");
      PrintWriter out = response.getWriter();

      String nick = "", pass = "", model = "", version = "";

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
         User u = null;
         try {
            u = new User(nick, pass, null, (byte) 0, null, null, model);
         } catch (NoSuchMethodError err) {
            out.println(err.getMessage());
            err.printStackTrace(out);
            return;
         }
         String address = "<address>" + request.getRemoteAddr() + "</address>";
         String host = "<host>" + request.getRemoteHost() + "</host>";
         SessionManager sMgr = SessionManager.getInstance();
         out.println(sMgr.addUser(u).toString());
      }

   }
}
