<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-10-14
  Time: 10:48 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Poll Released</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
    <div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px;  width: fit-content; margin: 10% auto 0px auto; ">

        <h1 id="released">Poll Manager</h1>

        <form action="state_manager" method="GET">
            <select name="file_format">
                <option value="txt">txt</option>
                <option value="xml">xml</option>
                <option value="json">json</option>
            </select>
            <button type="submit" value="DOWNLOAD" name="status_change" >Download Results</button>
            <br><br>
            <input type="submit">
        </form>

        <br><br><br>

        <% if (true) { %>
        <h1 id="released" style="text-align: center; color: black; padding: 10px;">Results</h1>

        <%
            HashMap<String, Integer> results = (HashMap<String, Integer>) request.getAttribute("results");
            if (results != null) {
        %>
        <% for (Map.Entry<String, Integer> entry : results.entrySet()) { %>
        <p> <%= entry.getKey()%></p>
        &emsp; <i><b>  Count: </b></i> <%= entry.getValue()%><br>
        <%}
        }
        else {
            out.println("No one has voted yet");
        }

        }%>

        <form action="state_manager" method="GET" >
            <button class="btn btn-primary" id="home" type="submit" value="HOME" name="status_change" >Home</button>
        </form>
    </div>
</body>
</html>
