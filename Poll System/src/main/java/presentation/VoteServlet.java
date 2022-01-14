package presentation;

import business.Poll;
import business.PollManager;
import business.Status;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "VoteServlet", value = "/VoteServlet")
public class VoteServlet extends HttpServlet {
    PollManager PM;

    public void init() {
        PM = new PollManager();
    }

    /**
     * handles participant's voting process
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pollID = request.getParameter("pollID");
        String choice = request.getParameter("choice");
        String pin = request.getParameter("pin");

        String result = PM.vote(pollID, pin, choice);
        // cannot find pin
        if (result.equalsIgnoreCase("not found")){
            request.setAttribute("error", "PIN # not found.");
        }
        // vote replaced successfully
        else if (result.equalsIgnoreCase("replaced")){
            request.setAttribute("replace", "Your vote has been changed.");
        }
        // new pin
        else {
            request.setAttribute("newPIN", result);
        }
        request.setAttribute("pollID", pollID);
        request.getRequestDispatcher("voted.jsp").forward(request, response);


//        PrintWriter out = response.getWriter();
//        response.setContentType("text/html");
//	String pollID = request.getParameter("pollID");
//
//        this.status = PM.getPollStatus(pollID);
//
//        if (this.status == Status.running ) {
//            this.color = "lightgreen";
//        } else if (this.status == Status.created ) {
//            this.color = "yellow";
//        } else if (this.status == Status.released ) {
//            this.color = "red";
//        } else {
//            this.color = "lightgrey";
//        }
//
//        out.println("<div style=\"background-color:" + this.color + ";\"> Poll Status: " + status +  "</div>");
//
//        if (this.status == Status.running ) {
//
//            request.setAttribute("poll", pollID);
//            request.getRequestDispatcher("poll_voting.jsp").forward(request, response);
//
//            out.println("<html><body>");
//            out.println("show poll here");
//            out.println("</body></html>");
//
//
//        } else if (this.status == Status.created ) {
//
//            out.println("<html><body>");
//            out.println("<h1>Participant</h1>");
//            out.println("keep refreshing this page until you see a poll");
//            out.println("</body></html>");
//
//        } else if (this.status == Status.released ) {
//
//            out.println("<html><body><h3>Poll Ended </h3>");
//
//            out.println("<form action=\"state_manager\" method=\"GET\">");
//
//            out.println("<input id=\"blue\" type=\"radio\" name=\"status_change\" value=\"VIEW_PARTICIPANT\" />");
//            out.println("<label for=\"blue\">View Results</label>");
//
//            out.println("<br><br>");
//
//            out.println("<input id=\"blue\" type=\"radio\" name=\"status_change\" value=\"DOWNLOAD\" />");
//            out.println("<label for=\"blue\">Download Results</label>");
//
//            out.println("<br><br>");
//            out.println("<input type=\"submit\">");
//            out.println("</form>");
//
//            out.println("</body></html>");
//        } else {
//            out.println("<html><body>");
//            out.println("no poll to see, go back.");
//            out.println("</body></html>");
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
