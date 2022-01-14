package presentation;

import Exceptions.WrongStateException;
import business.Poll;
import business.PollManager;
import business.Status;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;

public class StateManagerServlet extends HttpServlet {

    PollManager PM;

    public void init() {
        this.PM = new PollManager();
    }

	/**
	 * performs poll operations based on the user's choice
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choice = request.getParameter("choice");
        String status_change = request.getParameter("status_change");
        System.out.println("status_change: " + status_change);
        if (choice != null) {
			// do voting here
	    String participant = request.getParameter("participant");
	    String pollID = request.getParameter("pollID");
	    PM.vote(pollID, choice, participant);
		request.getRequestDispatcher("voted.jsp").forward(request, response);
	}
		// get pollID from session
		String pollID = (String)request.getSession().getAttribute("pollID");

	if (status_change != null){
	    try {
		if(status_change.equals("RUNNING")) {
		    PM.runPoll(pollID);
		}
		else if (status_change.equals("UNRELEASE")){
		    PM.unreleasePoll(pollID);
		}
		else if(status_change.equals("RELEASE")) {
		    PM.releasePoll(pollID);
		}
		else if(status_change.equals("RELEASED_CLEAR")) {
		    PM.clearPoll(pollID);
		}
		else if(status_change.equals("CREATED_UPDATE")) {
		    PM.clearResults(pollID);
		    PM.setPollStatus(pollID,Status.created);
		}
		else if(status_change.equals("RUNNING_CLEAR")) {
		    PM.clearPoll(pollID);
		}
		else if(status_change.equals("RUNNING_UPDATE")) {
		    PM.clearResults(pollID);
		    PM.setPollStatus(pollID,Status.created);
		}
		else if(status_change.equals("VIEW")) {
                    HashMap<String, Integer> results = PM.pollResults(pollID);
                    request.setAttribute("results", results);
                    request.setAttribute("status_change", "VIEW");
                    request.getRequestDispatcher("poll_released.jsp").forward(request, response);
                }
		else if(status_change.equals("VIEW_PARTICIPANT")) {
			HashMap<String, Integer> results = PM.pollResults(pollID);
			request.setAttribute("results", results);
			request.setAttribute("status_change", "VIEW");
			request.getRequestDispatcher("poll_result_participant.jsp").forward(request, response);
		}
		else if(status_change.equals("CLOSE")) {
		    PM.closePoll(pollID);
			String userID = (String) request.getSession().getAttribute("username");

			HashMap<String, HashMap<String, String>> listOfPolls = PM.getListOfPollsCreatedBySelf(userID);
			request.setAttribute("listOfPolls", listOfPolls);
			request.getRequestDispatcher("manager_view_polls.jsp").forward(request,response);
			return;
		}
		else if(status_change.equals("DOWNLOAD")) {
		    request.setAttribute("pollID", pollID);

		    request.getRequestDispatcher("/download_results").forward(request, response);
		}else if(status_change.equals("HOME")) {
		    request.getRequestDispatcher("login_success.jsp").forward(request, response);
			return;
		}else if(status_change.equals("HOME_PARTICIPANT")) {
			request.getRequestDispatcher("/").forward(request, response);
			return;
		}
	    } catch (WrongStateException e) {
		System.out.println(e.getMessage());
	      
	    }
		request.setAttribute("pollID",pollID);
	    request.setAttribute("status_change", status_change);
	}
	request.getRequestDispatcher("pollManager").forward(request, response);
    }

}
