<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Available Tickets</title>
    <!-- Using CSS for styling -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h2>Available Tickets</h2>

    <table>
        <tr>
            <th>Event</th>
            <th>Date</th>
            <th>Price</th>
            <th>Action</th>
        </tr>
        <%
            // "tickets" is set by ViewTicketsServlet (DB logic is inside the servlet, not here)
            java.util.List<String[]> tickets = (java.util.List<String[]>) request.getAttribute("tickets");
            if (tickets != null && !tickets.isEmpty()) {
                for (String[] t : tickets) {
        %>
        <tr>
            <!-- t[0] = id, t[1] = event_name, t[2] = event_date, t[3] = price -->
            <td><%= t[1] %></td>
            <td><%= t[2] %></td>
            <td>₹<%= t[3] %></td>
            <td>
                <!-- Form calls PurchaseServlet (where DB update happens) -->
                <form action="${pageContext.request.contextPath}/PurchaseServlet" method="post">
                    <input type="hidden" name="ticketId" value="<%= t[0] %>">
                    <input type="submit" value="Buy">
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="4">No tickets available at the moment.</td>
        </tr>
        <%
            }
        %>
    </table>

    <div class="nav-links">
        <!-- Navigation links -->
        <a href="${pageContext.request.contextPath}/jsp/addTicket.jsp">Post Ticket</a> |
        <a href="${pageContext.request.contextPath}/jsp/login.jsp">Logout</a>
    </div>
</body>
</html>
