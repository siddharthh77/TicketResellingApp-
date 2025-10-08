package servlets;

import utils.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/EditTicketServlet")
public class EditTicketServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "You must be logged in to edit a ticket.");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("id"));
        String eventName = request.getParameter("event_name");
        String eventDateStr = request.getParameter("event_date");
        String priceStr = request.getParameter("price");
        String status = request.getParameter("status");

        String errorMessage = null;
        double price = 0;
        LocalDate eventDate = null;

        // Validate inputs
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                errorMessage = "Price must be greater than 0.";
            }
        } catch (NumberFormatException e) {
            errorMessage = "Invalid price format.";
        }

        try {
            eventDate = LocalDate.parse(eventDateStr);
            if (!eventDate.isAfter(LocalDate.now())) {
                errorMessage = "Event date must be in the future.";
            }
        } catch (DateTimeParseException e) {
            errorMessage = "Invalid date format.";
        }

        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("jsp/editTicket.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            // Verify ownership
            String checkSql = "SELECT seller_id FROM Tickets WHERE id=?";
            try (PreparedStatement psCheck = con.prepareStatement(checkSql)) {
                psCheck.setInt(1, ticketId);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt("seller_id") != userId) {
                    request.setAttribute("errorMessage", "You can only edit your own tickets.");
                    request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
                    return;
                }
            }

            String sql = "UPDATE Tickets SET event_name=?, event_date=?, price=?, status=? WHERE id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, eventName);
                ps.setDate(2, java.sql.Date.valueOf(eventDate));
                ps.setDouble(3, price);
                ps.setString(4, status);
                ps.setInt(5, ticketId);
                ps.executeUpdate();
            }

            response.sendRedirect("ViewMyTicketsServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("jsp/editTicket.jsp").forward(request, response);
        }
    }
}
