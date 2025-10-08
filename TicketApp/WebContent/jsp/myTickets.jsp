<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>My Tickets</title>
</head>
<body>
<h2>My Tickets</h2>

<c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
</c:if>

<c:if test="${empty myTickets}">
    <p>You haven’t posted any tickets yet.</p>
    <a href="postTicket.jsp">Post a Ticket</a>
</c:if>

<c:if test="${not empty myTickets}">
<table border="1" cellpadding="8">
    <tr>
        <th>Event Name</th>
        <th>Date</th>
        <th>Price</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="t" items="${myTickets}">
        <tr>
            <td>${t.eventName}</td>
            <td>${t.eventDate}</td>
            <td>${t.price}</td>
            <td>${t.status}</td>
            <td>
                <a href="../editTicket.jsp?id=${t.id}">Edit</a> |
                <a href="../DeleteTicketServlet?id=${t.id}" onclick="return confirm('Delete this ticket?');">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</c:if>

<br>
<a href="postTicket.jsp">Post New Ticket</a>
</body>
</html>
