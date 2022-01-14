<%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-10-18
  Time: 4:32 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Start Page</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script>
        function logIn() {
            var form = document.getElementById("form");

            var password = document.createElement("input");
            password.type = "text";
            password.name = "password";
            password.required = true;
            password.placeholder = "Enter password";

            var logInButton = document.createElement("input");
            logInButton.type = "submit";
            logInButton.name = "submit";
            logInButton.value = "Log In";

            form.appendChild(password);
            form.appendChild(logInButton);
        }
    </script>
</head>
<body>
    <jsp:useBean id='manager' class='business.Manager' scope="session">
        <jsp:setProperty name='manager' property='password' />
    </jsp:useBean>

    <div style="border: 1px solid gray; width: 17%; border-radius: 30px; padding: 30px; text-align: center; margin-left: auto; margin-right: auto; margin-top: 15%">

        <h1 id="start"> Start Page</h1>
        <div id="poll_status"><% request.getParameter("PollInfo"); %></div>
        <br>
        <form id='form' action='login' >
            <input class="btn btn-outline-primary" type="submit" name="submit" value="Poll Manager" />
        </form>
        <form action='polls' >
            <input class="btn btn-outline-primary" type='submit' value='Participant' />
        </form>

    </div>

</body>
</html>
