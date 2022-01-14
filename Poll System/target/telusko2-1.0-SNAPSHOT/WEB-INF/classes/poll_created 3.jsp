<%@ page import="business.Poll" %>
<%@ page import="business.Choice" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-10-13
  Time: 9:33 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Test</title>
        <link href="css/style.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    </head>
    <body>
        <div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px;  width: fit-content; margin: 10% auto 0px auto; ">
            <h1 id="created" style="text-align: center; color: black; padding: 10px;">Poll Manager</h1>
            <br>

            <form action="state_manager" method="GET">
            <input id="red" type="radio" name="status_change" value="CREATED_UPDATE" />
                <label for="red">Update (clear results)</label>
            <br><br>
            <input id="blue" type="radio" name="status_change" value="RUNNING" />
            <label for="blue">Run (CREATED->RUNNING)</label>
            <br><br>
            <input class="btn btn-primary" type="submit">
            </form>



            <h2>  Current poll</h2>
            <%
                Poll poll = (Poll) request.getAttribute("poll");%>
            <b>Name: </b>
            <%    out.println(poll.getName()); %>
            <br>
            <b>Question: </b>
            <%    out.println(poll.getQuestion()); %>
            <br><br>
            <%    List<Choice> choices = poll.getChoices(); %>
            <%    for (Choice c : choices) { %>
            <b>Choice: </b>
            <%        out.println(c.getText()); %>
            <br>
            &emsp; <i><b>  Description: </b></i>
            <%       out.println(c.getDescription()); %>
            <br>
            <%    }
            %>

            <br><br>

            <form action="state_manager" method="GET" >
                <button class="btn btn-primary" id="home" type="submit" value="HOME" name="status_change" >Home</button>
            </form>
        </div>
    </body>
</html>
