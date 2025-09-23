package servlets;

import utils.DBConnection;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/EditTicketServlet")
public class EditTicketServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "You must be logged in to edit a ticket.");
            request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
            return;
        }

        String ticketIdStr = request.getParameter("id");
        String eventName = request.getParameter("event_name");
        String eventDateStr = request.getParameter("event_date");
        String priceStr = request.getParameter("price");
        String status = request.getParameter("status");

        String errorMessage = null;
        int ticketId = 0;

        // Validate ticket ID
        try {
            ticketId = Integer.parseInt(ticketIdStr);
        } catch (NumberFormatException e) {
            errorMessage = "Invalid ticket ID.";
        }

        // Validate price
        BigDecimal price = BigDecimal.ZERO;
        if (errorMessage == null) {
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    errorMessage = "Price must be greater than 0.";
                }
            } catch (NumberFormatException e) {
                errorMessage = "Invalid price format.";
            }
        }

        // Validate date
        LocalDate eventDate = null;
        if (errorMessage == null) {
            try {
                eventDate = LocalDate.parse(eventDateStr);
                if (!eventDate.isAfter(LocalDate.now())) {
                    errorMessage = "Event date must be in the future.";
                }
            } catch (DateTimeParseException e) {
                errorMessage = "Invalid date format.";
            }
        }

        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("jsp/editTicket.jsp?id=" + ticketId).forward(request, response);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Check ownership
            String checkSql = "SELECT seller_id FROM Tickets WHERE id=?";
            try (PreparedStatement psCheck = con.prepareStatement(checkSql)) {
                psCheck.setInt(1, ticketId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        int sellerId = rs.getInt("seller_id");
                        if (sellerId != userId) {
                            request.setAttribute("errorMessage", "You can only edit your own tickets.");
                            request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("errorMessage", "Ticket not found.");
                        request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // Update ticket
            String updateSql = "UPDATE Tickets SET event_name=?, event_date=?, price=?, status=? WHERE id=?";
            try (PreparedStatement psUpdate = con.prepareStatement(updateSql)) {
                psUpdate.setString(1, eventName);
                psUpdate.setDate(2, java.sql.Date.valueOf(eventDate));
                psUpdate.setBigDecimal(3, price);
                psUpdate.setString(4, status);
                psUpdate.setInt(5, ticketId);

                int rowsUpdated = psUpdate.executeUpdate();
                if (rowsUpdated > 0) {
                    response.sendRedirect("jsp/myTickets.jsp"); // Success
                } else {
                    request.setAttribute("errorMessage", "Failed to update ticket.");
                    request.getRequestDispatcher("jsp/editTicket.jsp?id=" + ticketId).forward(request, response);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("jsp/editTicket.jsp?id=" + ticketId).forward(request, response);
        }
    }
}
