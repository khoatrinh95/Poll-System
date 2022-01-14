package presentation;

import Exceptions.WrongStateException;
import business.Poll;
import business.PollManager;
import business.Status;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class PollServlet extends HttpServlet {
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
        String submitType = request.getParameter("submit");
        String searchedPoll = request.getParameter("searchedPollID");

        if (submitType!= null){
            // if user chose to vote on a poll
            String pollID = request.getParameter("pollID");
            // replace the current pollID in session
            request.getSession().setAttribute("pollID", pollID);

            if (submitType.equalsIgnoreCase("vote")) {
                HashMap<String, HashMap<String, String>> selectedPoll = PM.getPoll(pollID);
                ArrayList<HashMap<String, String>> choices = PM.getChoices(pollID);
                Poll poll = new Poll(pollID, selectedPoll, choices);
                request.setAttribute("poll", poll);
                request.getRequestDispatcher("poll_voting.jsp").forward(request,response);
            }
            // if user chose to view a poll
            else if (submitType.equalsIgnoreCase("view")) {
                try {
                    HashMap<String, Integer> pollResult = PM.getPollResults(pollID);
                    request.setAttribute("results", pollResult);
                    request.getRequestDispatcher("poll_result_participant.jsp").forward(request,response);
                } catch (WrongStateException e) {
                    e.printStackTrace();
                }
            }
        }
        // if a user searched a poll
        else if (searchedPoll != null && !searchedPoll.isBlank()){
            String pollID = request.getParameter("searchedPollID");
            HashMap<String, HashMap<String, String>> selectedPoll = PM.getPoll(pollID);
            request.setAttribute("selectedPoll", selectedPoll);
            request.getRequestDispatcher("voter_view_polls.jsp").forward(request,response);
        }

        HashMap<String, HashMap<String, String>> listOfActivePolls = PM.getActivePolls();
        request.setAttribute("listOfActivePolls", listOfActivePolls);
        request.getRequestDispatcher("voter_view_polls.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
