package presentation;

import Exceptions.WrongStateException;
import business.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PollManagerServlet extends HttpServlet {
    PollManager PM;
    String color;

    public void init() {
        System.out.println("PollManagerServlet init()");
	this.PM = new PollManager();
    }

    /**
     * handles all request redirections to jsp depending on poll Status
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submitType = request.getParameter("submit");
        System.out.println("SubmitType: " + submitType);
        if (submitType!=null){
            if (submitType.equalsIgnoreCase("Create New Poll")){
                // create new poll here
                request.getRequestDispatcher("create_poll.jsp").forward(request, response);
            }
            else if (submitType.equalsIgnoreCase("Confirm Deletion")){
                // delete poll
                String pollID = request.getParameter("pollID");
                PM.deletePoll(pollID);
                HashMap<String, HashMap<String, String>> listOfPolls = PM.getListOfPollsCreatedBySelf((String)request.getSession().getAttribute("username"));
                request.setAttribute("listOfPolls", listOfPolls);
                request.getRequestDispatcher("manager_view_polls.jsp").forward(request, response);
            }
            else if (submitType.equalsIgnoreCase("View")){
                // view closed poll
                String pollID = request.getParameter("pollID");
                HashMap<String, Integer> results = PM.pollResults(pollID);
                request.setAttribute("results", results);
                request.getRequestDispatcher("view_closed_poll.jsp").forward(request, response);
            }
        }


        // manage poll here
        String pollID = (String)request.getSession().getAttribute("pollID");

        // do this because when coming from manager_view_polls.jsp
        // we know which pollID the user selects
        // (inside manager_view_polls.jsp, I set a hidden parameter called "pollID" that holds the pollID that the user selected)
        if (request.getParameter("pollID") != null) {
            pollID = request.getParameter("pollID");

            // replace the current pollID in session
            request.getSession().setAttribute("pollID", pollID);
        }

        HashMap<String, HashMap<String,String>> pollMap = PM.getPoll(pollID);
        ArrayList<HashMap<String,String>> choicesList = PM.getChoices(pollID);
        Poll poll = new Poll(pollID, pollMap, choicesList);
        Status status = poll.getPollStatus();
        request.setAttribute("poll", poll);
        request.setAttribute("choiceSize", poll.getChoices().size());

        if (status == Status.running) {
            request.getRequestDispatcher("poll_running.jsp").forward(request, response);

        } else if (status == Status.created) {
            System.out.println(request.getParameter("status_change"));

            if (request.getParameter("status_change") != null && (request.getParameter("status_change").equals("CREATED_UPDATE") || request.getParameter("status_change").equals("RUNNING_UPDATE"))) {
                // go back to create poll
                System.out.println("poll needs to be updated.");
                request.getRequestDispatcher("update_poll.jsp").forward(request, response);
            } else {
                System.out.println("poll is created. show update and run buttons.");
                request.getRequestDispatcher("poll_created.jsp").forward(request, response);
            }
        } else if (status == Status.released) {
            request.getRequestDispatcher("poll_released.jsp").forward(request, response);
        } else if (status == Status.closed) {
            // TODO: show archived poll data
            request.getRequestDispatcher("poll_released.jsp").forward(request, response);
        }

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        System.out.println("PollManager doGet()");

    }

    /**
     * handles all operations relating to poll creation and update
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ManagerServlet doPost()");

        try {
            List<String> choices = Arrays.asList(request.getParameterValues("choice"));
            List<String> descriptions = Arrays.asList(request.getParameterValues("description"));
            String name = request.getParameter("name");
            String question = request.getParameter("question");
            String updateChoice = request.getParameter("update_choice");
	        String pollID = request.getParameter("pollID");

            // get userID from session
            String userID = (String) request.getSession().getAttribute("username");


            if (request.getParameter("submit").equalsIgnoreCase("create")){
                String newPollID = PM.createPoll(userID, name, question, choices, descriptions);

                request.getSession().setAttribute("pollID", newPollID);
            }
            else if (request.getParameter("submit").equalsIgnoreCase("update")) {
                if (updateChoice == null){
                    PM.updatePoll(pollID, name, question, null, null, false);
                }
                else if (request.getParameter("update_choice").equalsIgnoreCase("AddMoreChoices")){
                    // add more choices here
                    PM.updatePoll(pollID, name, question, choices, descriptions, false);
                }
                else if (request.getParameter("update_choice").equalsIgnoreCase("ReplaceChoices")) {
                    // replace choices here
                    PM.updatePoll(pollID, name, question, choices, descriptions, true);
                }
            }
        } catch (WrongStateException e) {
            System.out.println(e.getMessage());
        }
        // TODO: check for valid submission

        doGet(request, response);
    }


}
