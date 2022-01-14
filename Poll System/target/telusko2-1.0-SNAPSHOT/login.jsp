<%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-11-15
  Time: 9:14 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
<div class="card center-screen" style="width: 18rem; background-color: darkgrey">
    <div class="card-body">
        <h1 class="card-title">Log in</h1>
        <form action="login" method="post">
            <div class="mb-3">
                <label for="formGroupExampleInput" class="form-label">Username: </label>
                <input type="text" class="form-control" id="formGroupExampleInput" name="username" required="required">
            </div>
            <div class="mb-3">
                <label for="formGroupExampleInput2" class="form-label">Password: </label>
                <input type="password" class="form-control" id="formGroupExampleInput2" name="password" required="required">
            </div>
            <input type="submit" class="btn btn-primary" value="Login" ><br>
            <%
                if (request.getAttribute("loginError") != null) {
            %>
            <p style="color: red"> <%= request.getAttribute("loginError")%></p>
            <%
                }
            %>
        </form>

        <form action="umanager">
            <button type="submit" name="submit" class="btn btn-link" value = "Sign Up">Sign Up</button>
            <button type="submit" name="submit" class="btn btn-link" value = "Forgot Password">Forgot Password</button>
            <button type="submit" name="submit" class="btn btn-link" value = "Change Password">Change Password</button>
        </form>
    </div>
</div>

<%--    Username: <input type="text" name="username">--%>
<%--    <br>--%>
<%--    Password: <input type="password" name="password">--%>
<%--    <br><br>--%>
<%--    <input type="submit" value="Login">--%>
</body>
</html>
