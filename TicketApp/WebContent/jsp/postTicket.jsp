<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Post Ticket</title>
</head>
<body>
<h2>Post a Ticket</h2>
<form action="../AddTicketServlet" method="post">
    Event Name: <input type="text" name="event_name" required><br>
    Event Date: <input type="date" name="event_date" required><br>
    Price: <input type="number" step="0.01" name="price" required><br>
    <input type="submit" value="Post Ticket">
</form>
</body>
</html>
