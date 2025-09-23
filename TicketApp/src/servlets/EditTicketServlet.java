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

@WebServlet("/EditTicketServlet")
public class EditTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ticketId = request.getParameter("id");
        String eventName = request.getParameter("event_name");
        String eventDate = request.getParameter("event_date");
        String price = request.getParameter("price");
        String status = request.getParameter("status");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE Tickets SET event_name=?, event_date=?, price=?, status=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, eventName);
            ps.setString(2, eventDate);
            ps.setBigDecimal(3, new java.math.BigDecimal(price));
            ps.setString(4, status);
            ps.setInt(5, Integer.parseInt(ticketId));
            ps.executeUpdate();
            response.sendRedirect("jsp/myTickets.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error editing ticket");
        }
    }
}

