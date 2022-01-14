<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Manage Poll</title>
        <link href="css/style.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <script>
            let count = 3;
            function addMoreChoices() {
                var textfield = document.createElement("input");
                textfield.type = "text";
                textfield.name = "choice";
                textfield.class = "form-control";
                textfield.placeholder = "Option " + count;
                var form = document.getElementById('form');
                form.appendChild(textfield );

                var textfield = document.createElement("input");
                textfield.type = "text";
                textfield.name = "description";
                textfield.class = "form-control";
                textfield.placeholder = "Option " + count + " Description";
                var form = document.getElementById('form');
                form.appendChild(textfield );
                count++;
            }
        </script>
    </head>
    <body>
        <div style="border: 1px solid lightgray; border-radius: 30px; padding: 20px 50px 50px 50px;  width: fit-content; margin: 10% auto 0px auto; ">

            <h1 id="created" style="text-align: center; color: black; padding: 10px;">Update Poll</h1>

            <form  action="pollManager" method="POST">
                <div id = "form">
                    <input type="text" class="form-control" placeholder="Enter Poll Name" name="name">
                    <input type="text" class="form-control" placeholder="Enter Poll Question" name="question">

                    <input type="text" class="form-control"  name="choice" placeholder="Option 1">
                    <input type="text" class="form-control"  name="description" placeholder="Option 1 Description">

                    <input type="text" class="form-control"  name="choice" placeholder="Option 2">
                    <input type="text" class="form-control"  name="description" placeholder="Option 2 Description">
                </div>

                <br>
                <div>
                    <input id="blue" type="radio" name="update_choice" value="AddMoreChoices" />
                    <label for="blue">Add these choices to existing choice list</label>
                    <br>
                    <input id="red" type="radio" name="update_choice" value="ReplaceChoices" />
                    <label for="red">Replace the existing choices with these choices</label>
                </div>
                <br>
                <button class="btn btn-primary" type="button" onclick="addMoreChoices()">Add choice</button>
                <input class="btn btn-primary" type="submit" name= "submit" value="Update">
                <%
                    String pollID = (String)request.getSession().getAttribute("pollID");
                %>
                <input type="hidden" name="pollID" value="<%=pollID%>">
            </form>

            <form action="state_manager" method="GET" >
                <button class="btn btn-primary" id="home" type="submit" value="HOME" name="status_change" >Home</button>
            </form>
        </div>

    </body>
</html>
