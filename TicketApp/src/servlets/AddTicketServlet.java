package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AddTicketServlet")
public class AddTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC connection info
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=ticket_resell";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "pass";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form parameters
        String eventName = request.getParameter("event_name");
        String eventDateStr = request.getParameter("event_date");
        String priceStr = request.getParameter("price");

        String errorMessage = null;

        // Validate price
        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                errorMessage = "Price must be greater than 0.";
            }
        } catch (NumberFormatException e) {
            errorMessage = "Invalid price format.";
        }

        // Validate event date
        LocalDate eventDate = null;
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
            request.getRequestDispatcher("/postTicket.jsp").forward(request, response);
            return;
        }

        // Get seller_id from session (assuming user logged in)
        HttpSession session = request.getSession();
        Integer sellerId = (Integer) session.getAttribute("userId");
        if (sellerId == null) {
            errorMessage = "You must be logged in to post a ticket.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/postTicket.jsp").forward(request, response);
            return;
        }

        // Insert ticket into database
        try {
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO Tickets (seller_id, event_name, event_date, price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, sellerId);
                    ps.setString(2, eventName);
                    ps.setDate(3, java.sql.Date.valueOf(eventDate));
                    ps.setDouble(4, price);

                    int rowsInserted = ps.executeUpdate();
                    if (rowsInserted > 0) {
                        response.sendRedirect(request.getContextPath() + "/viewTickets.jsp");
                    } else {
                        request.setAttribute("errorMessage", "Failed to post ticket. Try again.");
                        request.getRequestDispatcher("/postTicket.jsp").forward(request, response);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/postTicket.jsp").forward(request, response);
        }
    }
}
