package presentation.toDelete;

import business.Poll;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HoldingServlet", value = "/HoldingServlet")
public class HoldingServlet extends HttpServlet {
    String status;
    Poll poll;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.status == "RUNNING") {
            System.out.println("poll is running");
            request.setAttribute("status", this.status);
            request.getRequestDispatcher("poll.jsp").forward(request, response); // landingPage
        } else if (this.status == "CREATED" ) {
            System.out.println("someone is currently creating a poll. refresh in 2 minutes to view poll.");
            request.getRequestDispatcher("create_poll.jsp").forward(request, response);

        } else {
            System.out.println("route to create poll");
            request.getRequestDispatcher("create_poll.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
