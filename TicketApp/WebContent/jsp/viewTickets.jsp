<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Browse Tickets</title>
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
            java.util.List<String[]> tickets = (java.util.List<String[]>) request.getAttribute("tickets");
            if (tickets != null && !tickets.isEmpty()) {
                for (String[] t : tickets) {
        %>
        <tr>
            <td><%= t[1] %></td>
            <td><%= t[2] %></td>
            <td>₹<%= t[3] %></td>
            <td>
                <form action="${pageContext.request.contextPath}/PurchaseServlet" method="post">
                    <input type="hidden" name="ticketId" value="<%= t[0] %>">
                    <button type="submit">Buy</button>
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
</body>
</html>
