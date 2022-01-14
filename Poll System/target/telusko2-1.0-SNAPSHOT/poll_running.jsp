<%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-10-14
  Time: 10:35 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Poll Running</title>
        <link href="css/style.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    </head>
    <body>
        <div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px;  width: fit-content; margin: 10% auto 0px auto; ">

            <h1 id="running" style="text-align: center; color: white; padding: 10px;">Poll Manager</h1>
            <br><br>

            <form action="state_manager" method="GET">
                <input id="red" type="radio" name="status_change" value="RELEASE" />
                <label for="red">Release (RUNNING->RELEASED)</label>
            <br><br>
                <input id="blue" type="radio" name="status_change" value="RUNNING_CLEAR" />
                <label for="blue">Clear</label>
            <br><br>
                <input id="blue" type="radio" name="status_change" value="RUNNING_UPDATE" />
                <label for="blue">Update (RUNNING->CREATED)</label>
                <br><br>
                <input class="btn btn-primary" type="submit">
            </form>

            <form action="state_manager" method="GET" >
                <button class="btn btn-primary" id="home" type="submit" value="HOME" name="status_change" >Home</button>
            </form>
        </div>

    </body>
</html>
