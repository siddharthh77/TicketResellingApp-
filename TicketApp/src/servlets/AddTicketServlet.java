package servlets;

import utils.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AddTicketServlet")
public class AddTicketServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "You must be logged in to post a ticket.");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        }

        String eventName = request.getParameter("event_name");
        String eventDateStr = request.getParameter("event_date");
        String priceStr = request.getParameter("price");

        String errorMessage = null;
        double price = 0;
        LocalDate eventDate = null;

        // Validate price
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                errorMessage = "Price must be greater than 0.";
            }
        } catch (NumberFormatException e) {
            errorMessage = "Invalid price format.";
        }

        // Validate date
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
            request.getRequestDispatcher("jsp/postTicket.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO Tickets (seller_id, event_name, event_date, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, eventName);
                ps.setDate(3, java.sql.Date.valueOf(eventDate));
                ps.setDouble(4, price);
                ps.executeUpdate();
            }

            response.sendRedirect("ViewMyTicketsServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("jsp/postTicket.jsp").forward(request, response);
        }
    }
}
