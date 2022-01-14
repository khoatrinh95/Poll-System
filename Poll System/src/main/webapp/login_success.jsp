<%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-11-15
  Time: 9:11 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>LogIn Success</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>

    <div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px;  width: fit-content; margin: 10% auto 0px auto; ">

        <h1>Poll System 2.0</h1>
        <%
            String userID = (String) session.getAttribute("username");
        %>
        <p>User ID: <%=userID%></p>

        <form id='form' action='login' >
            <input class="btn btn-primary" type='submit' name="submit" value='View Polls' />
            <input class="btn btn-primary" type='submit' name="submit" value='Vote' />
            <input class="btn btn-primary" type='submit' name="submit" value='Log out' />
        </form>

    </div>
</body>
</html>
