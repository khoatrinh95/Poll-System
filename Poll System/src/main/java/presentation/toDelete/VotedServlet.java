package presentation.toDelete;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet()
public class VotedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String color = request.getParameter("color");
        out.println("<!DOCTYPE HTML>");
        out.println("<html><body>");
        out.println("you voted => " + color);
        out.println("<br>");
        out.println("<button>change vote</button>");
        out.println("</html></body>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
