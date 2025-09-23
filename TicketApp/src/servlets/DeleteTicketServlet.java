package servlets;

import utils.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DeleteTicketServlet")
public class DeleteTicketServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ticketIdStr = request.getParameter("id");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // User not logged in
            request.setAttribute("errorMessage", "You must be logged in to delete a ticket.");
            request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
            return;
        }

        int ticketId = 0;
        try {
            ticketId = Integer.parseInt(ticketIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid ticket ID.");
            request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Check if the ticket belongs to the logged-in user
            String checkSql = "SELECT seller_id FROM Tickets WHERE id=?";
            try (PreparedStatement psCheck = con.prepareStatement(checkSql)) {
                psCheck.setInt(1, ticketId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        int sellerId = rs.getInt("seller_id");
                        if (sellerId != userId) {
                            request.setAttribute("errorMessage", "You can only delete your own tickets.");
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

            // Delete the ticket
            String deleteSql = "DELETE FROM Tickets WHERE id=?";
            try (PreparedStatement psDelete = con.prepareStatement(deleteSql)) {
                psDelete.setInt(1, ticketId);
                int rowsDeleted = psDelete.executeUpdate();
                if (rowsDeleted > 0) {
                    response.sendRedirect("jsp/myTickets.jsp"); // Success
                } else {
                    request.setAttribute("errorMessage", "Failed to delete ticket.");
                    request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
        }
    }
}
