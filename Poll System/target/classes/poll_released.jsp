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

            <h1 id="released" style="text-align: center; color: white; padding: 10px;">Poll Manager</h1>

            <br><br>
            <form action="state_manager" method="GET">

            <input id="red" type="radio" name="status_change" value="RELEASED_CLEAR" />
            <label for="red">Clear (RELEASED->CREATED)</label>

            <br><br>

            <input id="blue" type="radio" name="status_change" value="UNRELEASE" />
            <label for="blue">Unrelease (RELEASED->RUNNING)</label>

            <br><br>

            <input id="blue" type="radio" name="status_change" value="CLOSE" />
            <label for="blue">Close (Archive poll info)</label>

            <br><br>

            <input id="blue" type="radio" name="status_change" value="VIEW" />
            <label for="blue">View Results</label>

            <br><br>
            <input id="blue" type="radio" name="status_change" value="DOWNLOAD" />
            <label for="blue">Download Results</label>
            <select name="file_format">
                <option value="txt">txt</option>
                <option value="xml">xml</option>
                <option value="json">json</option>
            </select>
            <br><br>
            <input class="btn btn-primary" type="submit">
            </form>

            <br><br><br>

            <% if (request.getParameter("status_change") != null && request.getParameter("status_change").equalsIgnoreCase("view") ) { %>
            <h1 id="released">Here are the results</h1>
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
