<%@ page import="business.Poll" %>
<%@ page import="business.Choice" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-10-14
  Time: 11:37 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Poll voting</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
    <div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px;  width: fit-content; margin: 10% auto 0px auto; ">
    <h1>Poll</h1>

    <% Poll poll = (Poll) request.getAttribute("poll");%>
    <b>Poll ID: </b>
    <%    out.println(poll.getId()); %>
    <br>

    <b>Name: </b>
    <%    out.println(poll.getName()); %>
    <br>

    <b>Question: </b>
    <%    out.println(poll.getQuestion()); %>
    <br><br>

    <form action="vote" method="GET">

        <label>PIN #: </label>
        <input class="form-control" name="pin"><br/>

        <%    List<Choice> choices = poll.getChoices();     %>

        <% for (int i = 0 ; i < choices.size() ; i++) {  %>
        <div>
            <input class="form-check-input" type="radio" name="choice" value="<%= choices.get(i).getText() %>" required/>
            <label class="form-check-label"><%= choices.get(i).getText() %></label>
            <br>
            <p>
                <i><b>  Description: </b></i>
                <%= choices.get(i).getDescription() %>
            </p>
            <br>
            <input type="hidden" name="pollID" value="<%= poll.getId() %>"/>
        </div>

        <% } %>

        <input class="btn btn-primary" type="submit">
    </form>


    <form action="state_manager" method="GET" >
        <button id="home" class="btn btn-primary" type="submit" value="HOME_PARTICIPANT" name="status_change" >Home</button>
    </form>

    </div>

</body>
</html>
