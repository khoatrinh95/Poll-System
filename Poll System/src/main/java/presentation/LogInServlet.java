package presentation;

import business.Factory;
import business.PollManager;
import business.UserManagerInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
public class LogInServlet extends HttpServlet {
    PollManager PM;

    public void init() {
        System.out.println("PollManagerServlet init()");
        this.PM = new PollManager();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submitType = request.getParameter("submit");

        switch (submitType) {
            case "Poll Manager":
                request.getRequestDispatcher("login.jsp").forward(request,response);
                break;
            case "View Polls":
                String userID = (String) request.getSession().getAttribute("username");

                HashMap<String, HashMap<String, String>> listOfPolls = PM.getListOfPollsCreatedBySelf(userID);
                request.setAttribute("listOfPolls", listOfPolls);
                request.getRequestDispatcher("manager_view_polls.jsp").forward(request,response);
                break;
            case "Vote":
                response.sendRedirect(request.getContextPath() + "/polls");
                break;
            case "Log out":
                request.getSession().invalidate();
                request.getRequestDispatcher("/").forward(request,response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for username and password
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (authorize(username,password)) {
            //get the old session and invalidate
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            //generate a new session
            HttpSession newSession = request.getSession(true);

            // save userID in session
            newSession.setAttribute("username", username);
            response.sendRedirect("login_success.jsp");
        } else {
            // TODO: handle if wrong pw
            request.setAttribute("loginError", "Incorrect password");
            request.getRequestDispatcher("login.jsp").forward(request,response);
        }
    }

    /**
     * authorize manager
     * @param inputUsername
     * @param inputPassword
     * @return
     */
    private boolean authorize(String inputUsername, String inputPassword) {
        UserManagerInterface UM = Factory.getUMObj();
        String hashedpw = hash(inputPassword);
        return UM.signIn(inputUsername, hashedpw);

    }

    /**
     * hash function to hash password MD5
     * @param toHash
     * @return
     */
    private String hash (String toHash){
//        String toHash = "SOEN387";
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(toHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return (generatedPassword);
    }
}
