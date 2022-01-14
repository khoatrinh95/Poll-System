import UserManagement.UserManager;
import db.DataConn;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestUserManagement {
    UserManager UM = new UserManager();
    static ArrayList<String> users = new ArrayList<>();

    @Test
    public void testSignUpPositive(){
        // test return a token (not null)
        String user = "test1";
        users.add(user);
        String token = UM.signUp(user, "first", "last", "email1", "password");
        Assert.assertTrue(!token.contains("Error"));
    }

    @Test
    public void testSignUpNegative(){
        String user = "test2";
        users.add(user);
        // test return message contain error when sign up with existing userID
        String token1 = UM.signUp(user, "first", "last", "email2", "password");
        String token2 = UM.signUp(user, "first", "last", "email2", "password");
        Assert.assertTrue(token2.contains("Error"));
    }

    @Test
    public void testForgotPassword(){
        String user = "test3";
        users.add(user);
        // test return a token (not null)
        UM.signUp(user, "first", "last", "email3", "password");
        String token = UM.forgotPassword("email3");
        Assert.assertNotNull(token);
    }

    @Test
    public void testChangePasswordPositive(){
        String user = "test4";
        users.add(user);
        // correct old password
        UM.signUp(user, "first", "last", "email4", "password");
        boolean success = UM.changePassword("password", "test4", "newPassword");
        Assert.assertTrue(success);
    }

    @Test
    public void testChangePasswordNegative(){
        String user = "test5";
        users.add(user);
        // incorrect old password
        UM.signUp(user, user, "last", "email5", "password");
        boolean success = UM.changePassword("wrongpassword", "test5", "newpassword");
        Assert.assertFalse(success);
    }

    @Test
    public void testVerifyEmailPositive(){
        String user = "test6";
        users.add(user);
        // verify unvalidated account
        String token = UM.signUp(user, "first", "last", "email6", "password");
        boolean success = UM.verifyEmail(token);
        Assert.assertTrue(success);
    }

    @Test
    public void testVerifyEmailNegative(){
        String user = "test7";
        users.add(user);
        // verify validated account
        String token = UM.signUp(user, "first", "last", "email7", "password");
        boolean success1 = UM.verifyEmail(token);
        Assert.assertTrue(success1);
        boolean success2 = UM.verifyEmail(token);
        Assert.assertFalse(success2);
    }

    @AfterClass
    public static void cleanUp(){
        DataConn dc = new DataConn();
        try{
            for (String s : users){
                dc.deleteUser(s);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
