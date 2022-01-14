<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: khoatrinh
  Date: 2021-11-15
  Time: 9:53 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Manager View Polls</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <script>
            function confirm(pollID) {
                var toAppear = pollID + "A";
                document.getElementById(toAppear).style.display = "inline-block";
                document.getElementById(pollID).style.display = "none";
            }
        </script>
    </head>
    <body>
        <div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px; width: fit-content; margin: 10% auto 0px auto; ">

            <h1> Manager view poll </h1>
            <%
                String userID = (String) session.getAttribute("username");

                HashMap<String, HashMap<String, String>> listOfPolls = (HashMap<String, HashMap<String, String>>) request.getAttribute("listOfPolls");
            %>
            <p>User ID: <%=userID%> </p>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">Poll ID</th>
                    <th scope="col">Poll Name</th>
                    <th scope="col">Status</th>
                    <th scope="col"></th>
                    <th scope="col"></th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (Map.Entry<String, HashMap<String, String>> poll : listOfPolls.entrySet()) {
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
                    <td><%=status%>
                    </td>
                    <form action="pollManager">
                        <td>

                            <%
                                if (status.equalsIgnoreCase("closed")) {
                            %>
                            <input class="btn btn-warning btn-block" name="submit" type="submit" value="View">
                            <%
                            } else {
                            %>
                            <input class="btn btn-primary btn-block" name="submit" type="submit" value="Manage">
                            <%
                                }
                            %>
                        </td>
                        <td>
                            <button id="<%=pollID%>" type="button" class="btn btn-danger" onclick="confirm('<%=pollID%>')">Delete
                            </button>
                            <input id="<%=pollID%>A" style="display:none;" class="btn btn-danger" name="submit" type="submit"
                                   value="Confirm Deletion">
                            <input type="hidden" name="pollID" value="<%=poll.getKey()%>">


                        </td>
                    </form>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <form action="pollManager">
                <input class="btn btn-success" name="submit" type="submit" value="Create New Poll">
            </form>

            <form action="state_manager" method="GET">
                <button id="home" type="submit" value="HOME" name="status_change">Home</button>
            </form>
        </div>
    </body>

</html>
