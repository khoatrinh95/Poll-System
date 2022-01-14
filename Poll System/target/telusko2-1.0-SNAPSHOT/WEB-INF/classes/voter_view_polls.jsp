<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-11-17
  Time: 10:33 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Voter view polls</title>
  <link href="css/style.css" rel="stylesheet" type="text/css"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>

<div  class="center-screen">
  <h1>Poll System 2.0</h1>
  <form action="polls">
    <div class="input-group mb-3">
      <input type="text" class="form-control" name="searchedPollID" placeholder="Poll ID" aria-label="Poll ID" aria-describedby="basic-addon2">
      <div class="input-group-append">
        <input class="btn btn-outline-secondary" type="submit" value="Search">
      </div>
    </div>
    <input type="submit" name="refresh" class="btn btn-link" value="Refresh">
  </form>

  <table class="table table-striped">
    <thead>
    <tr>
      <th scope="col">Poll ID</th>
      <th scope="col">Poll Name</th>
      <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <%
      HashMap<String, HashMap<String, String>> listOfActivePolls = (HashMap<String, HashMap<String, String>>) request.getAttribute("listOfActivePolls");
      HashMap<String, HashMap<String, String>> selectedPoll = (HashMap<String, HashMap<String, String>>) request.getAttribute("selectedPoll");

      if (selectedPoll != null ) {
        listOfActivePolls = selectedPoll;
      }
      for (Map.Entry<String, HashMap<String, String>> poll : listOfActivePolls.entrySet()) {
        HashMap<String, String> pollDataMap = poll.getValue();
        String pollID = poll.getKey();
        String pollName = pollDataMap.get("PollName");
        String status = pollDataMap.get("PollStatus");
    %>
    <tr>
      <th scope="row"><%=poll.getKey()%>
      </th>
      <td><%=pollName%>
      </td>
      <form action="polls">
        <td>

          <%
            if (status.equalsIgnoreCase("running")) {
          %>
          <input class="btn btn-success btn-block" name="submit" type="submit" value="Vote">
          <%
          } else {
          %>
          <input class="btn btn-warning btn-block" name="submit" type="submit" value="View">
          <%
            }
          %>
          <input type="hidden" name="pollID" value="<%=poll.getKey()%>">
        </td>

      </form>
    </tr>
    <%
      }
    %>
    </tbody>
  </table>

  <form action="state_manager" method="GET" >
    <button class="btn btn-primary" id="home" type="submit" value="HOME_PARTICIPANT" name="status_change" >Home</button>
  </form>

</div>

</body>
</html>
