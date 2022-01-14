<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Poll Here</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<h1>pollStatus: RUNNING</h1>

vote for color
<br><br>
    <form action="voted">
        <input id="red" type="radio" name="color" value="1" />
        <label for="red">red</label>
        <input id="blue" type="radio" name="color" value="2" />
        <label for="blue">blue</label>
        <br><br>
        <input type="submit">
    </form>


<br>
<input type="submit" value="PollManager_DEBUG" formaction="test" onclick="form.action='poll_manager';">
<button >Poll Manager</button>
</body>
</html>
