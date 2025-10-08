<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Post Ticket</title>
</head>
<body>
<h2>Post a New Ticket</h2>

<c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
</c:if>

<form action="../AddTicketServlet" method="post">
    Event Name: <input type="text" name="event_name" required><br><br>
    Event Date: <input type="date" name="event_date" required><br><br>
    Price: <input type="number" step="0.01" name="price" required><br><br>
    <input type="submit" value="Post Ticket">
</form>

<br>
<a href="../ViewMyTicketsServlet">Back to My Tickets</a>
</body>
</html>

