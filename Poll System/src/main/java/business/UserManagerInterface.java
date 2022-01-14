package business;

public interface UserManagerInterface {
    String signUp(String userID, String FName, String LName, String email, String password);
    String forgotPassword(String email);
    boolean sendEmailSignUp(String email, String link, String templatePath);
    boolean sendEmailForgotPassword(String email, String token, String templatePath);
    boolean changePassword(String password, String userID, String newPassword);
    boolean resetPassword(String email, String token, String newPassword);
    boolean verifyEmail(String token);
    boolean signIn(String userID, String password);
}
