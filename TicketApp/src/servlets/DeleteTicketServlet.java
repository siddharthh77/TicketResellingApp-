package servlets;

import utils.DBConnection;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DeleteTicketServlet")
public class DeleteTicketServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "You must be logged in to delete a ticket.");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("id"));

        try (Connection con = DBConnection.getConnection()) {

            // Verify ownership
            String checkSql = "SELECT seller_id FROM Tickets WHERE id=?";
            try (PreparedStatement psCheck = con.prepareStatement(checkSql)) {
                psCheck.setInt(1, ticketId);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt("seller_id") != userId) {
                    request.setAttribute("errorMessage", "You can only delete your own tickets.");
                    request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
                    return;
                }
            }

            // Delete
            String deleteSql = "DELETE FROM Tickets WHERE id=?";
            try (PreparedStatement psDelete = con.prepareStatement(deleteSql)) {
                psDelete.setInt(1, ticketId);
                psDelete.executeUpdate();
            }

            response.sendRedirect("ViewMyTicketsServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("jsp/myTickets.jsp").forward(request, response);
        }
    }
}
