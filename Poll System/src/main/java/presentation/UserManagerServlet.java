package presentation;

import business.Factory;
import business.UserManagerInterface;
import db.DataConn;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserManagerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("submit");

        // user accesses servlet from sign-up page
        if (type != null) {
            if (type.equalsIgnoreCase("Sign Up")) {
                request.getRequestDispatcher("sign_up.jsp").forward(request, response);
            } else if (type.equalsIgnoreCase("Forgot Password")) {
                request.getRequestDispatcher("forgot_password.jsp").forward(request, response);
            } else if (type.equalsIgnoreCase("Change Password")) {
                request.getRequestDispatcher("change_password.jsp").forward(request, response);
            }
        }

        // user accesses servlet directly from link sent in email
        else {
            String token = request.getParameter("token");
            String message = "";
            String username = "";
            // Validate account
            boolean success = Factory.getUMObj().verifyEmail(token);

            if (success) {
                //get the old session and invalidate
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }
                //generate a new session
                HttpSession newSession = request.getSession(true);
                try {
                    username = new DataConn().getUserIDByToken(token);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                // save userID in session
                newSession.setAttribute("username", username);

                // set success message
                message = "Your email has been verified.";
                request.setAttribute("verified", true);
            } else {
                message = "We cannot verify your email";
                request.setAttribute("verified", false);
            }
            request.setAttribute("message", message);
            request.getRequestDispatcher("email_verified.jsp").forward(request, response);


        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManagerInterface UM = Factory.getUMObj();
        String type = request.getParameter("submit");

        // Sign up
        if (type.equalsIgnoreCase("Sign Up")) {
            String fname = request.getParameter("firstname");
            String lname = request.getParameter("lastname");
            String uid = request.getParameter("userid");
            String email = request.getParameter("email");
            List<String> password = Arrays.asList(request.getParameterValues("password"));

            // hash password
            String hashedPW = hash(password.get(0));

            // sign up
            String token = UM.signUp(uid, fname, lname, email, hashedPW);

            if (token.contains("Error")) {
                if (token.contains("userID")){
                    request.setAttribute("error", "User ID provided already exists.");
                } else if (token.contains("email")){
                    request.setAttribute("error", "Email provided already exists.");
                }
                request.getRequestDispatcher("sign_up.jsp").forward(request, response);
                return;
            }

            // generate link with token
            String link = request.getRequestURL().toString() + "?token=" + token;
            System.out.println(link);

            // send link to user's email
            String templatePath = this.getServletContext().getRealPath("/WEB-INF/sign_up_template");
            UM.sendEmailSignUp(email, link, templatePath);
            request.setAttribute("email", email);
            request.getRequestDispatcher("email_sent.jsp").forward(request, response);
        }

        // Forgot password
        else if (type.equalsIgnoreCase("Send email")) {
            String email = request.getParameter("email");

            // obtain token
            String token = UM.forgotPassword(email);

            // if found user -> send email
            if (!token.isBlank()) {
                // send token to user's email
                String templatePath = this.getServletContext().getRealPath("/WEB-INF/forgot_password_template");
                UM.sendEmailForgotPassword(email, token, templatePath);
            }

            request.setAttribute("email", email);
            request.getRequestDispatcher("reset_password.jsp").forward(request, response);
        }

        // user reset password
        else if (type.equalsIgnoreCase("Reset password")){
            String token = request.getParameter("token");
            String email = request.getParameter("email");
            List<String> password = Arrays.asList(request.getParameterValues("password"));

            // hash password
            String hashedPW = hash(password.get(0));

            // reset password
            boolean success = UM.resetPassword(email, token, hashedPW);

            if (success) {
                // go to change password success
                request.getRequestDispatcher("change_password_success.jsp").forward(request, response);
            } else {
                // go back to reset password + error
                request.setAttribute("email", email);
                request.setAttribute("error", "Unable to reset password");
                request.getRequestDispatcher("reset_password.jsp").forward(request, response);
            }
        }

        // Change password
        else if (type.equalsIgnoreCase("Change password")){
            String userid = request.getParameter("userid");
            String oldPassword = request.getParameter("oldpassword");
            String hashedOldPassword = hash(oldPassword);
            String newPassword = Arrays.asList(request.getParameterValues("password")).get(0);
            String hashedNewPassword = hash(newPassword);

            // change password
            boolean success = UM.changePassword(hashedOldPassword, userid, hashedNewPassword);

            if (success) {
                // go to change password success
                request.getRequestDispatcher("change_password_success.jsp").forward(request, response);
            } else {
                // go back to change password + error
                request.setAttribute("error", "Either User ID or old password is incorrect");
                request.getRequestDispatcher("change_password.jsp").forward(request, response);
            }
        }
    }

    /**
     * hash function to hash password MD5
     * @param toHash
     * @return
     */
    private String hash (String toHash){
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
