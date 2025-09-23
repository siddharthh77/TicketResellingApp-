package servlets;

import utils.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteTicketServlet")
public class DeleteTicketServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ticketId = request.getParameter("id");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM Tickets WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(ticketId));
            ps.executeUpdate();
            response.sendRedirect("jsp/myTickets.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error deleting ticket");
        }
    }
}

