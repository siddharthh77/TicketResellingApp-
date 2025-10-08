<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Tickets</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h2>My Tickets</h2>

    <!-- Show messages -->
    <c:if test="${not empty errorMessage}">
        <p style="color:red;">${errorMessage}</p>
    </c:if>

    <c:if test="${empty myTickets}">
        <p>You haven’t posted any tickets yet.</p>
        <a href="postTicket.jsp">Post a Ticket</a>
    </c:if>

    <c:if test="${not empty myTickets}">
        <table border="1" cellpadding="10" cellspacing="0">
            <thead>
                <tr>
                    <th>Event Name</th>
                    <th>Event Date</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="ticket" items="${myTickets}">
                    <tr>
                        <td>${ticket.eventName}</td>
                        <td>${ticket.eventDate}</td>
                        <td>${ticket.price}</td>
                        <td>${ticket.status}</td>
                        <td>
                            <a href="../EditTicketServlet?id=${ticket.id}">Edit</a> |
                            <a href="../DeleteTicketServlet?id=${ticket.id}" onclick="return confirm('Delete this ticket?');">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <br>
    <a href="postTicket.jsp">Post New Ticket</a> | 
    <a href="welcome.jsp">Back to Home</a>
</body>
</html>
