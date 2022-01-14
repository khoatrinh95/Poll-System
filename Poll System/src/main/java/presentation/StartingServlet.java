package presentation;

import business.Poll;
import business.PollManager;
import business.Status;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet()
public class StartingServlet extends HttpServlet {
    Status status;
    PollManager PM;
    String color;

    public void init() {
	PM = new PollManager();
    }

    /**
     * redirects request to start.jsp
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        //this.status = PM.getPollStatus();

	request.setAttribute("PollInfo",PM.getPollInfo());
        request.setAttribute("color", this.color);
        request.setAttribute("status", this.status);
        request.getRequestDispatcher("start.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
