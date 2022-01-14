<%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-12-04
  Time: 8:27 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Password Change</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

</head>
<body>
<div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px;  width: fit-content; margin: 10% auto 0px auto; ">

    <h1 id="created" style="text-align: center; color: black; padding: 10px;">Reset Password</h1>

    <br>

    <p>Password changed successfully.</p>


    <form action="state_manager" method="GET" >
        <button class="btn btn-primary" id="home" type="submit" value="HOME_PARTICIPANT" name="status_change" >Home</button>
    </form>
</div>
</body>
</html>
