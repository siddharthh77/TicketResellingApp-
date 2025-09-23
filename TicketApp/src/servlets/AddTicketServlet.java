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
import javax.servlet.http.HttpSession;

@WebServlet("/AddTicketServlet")
public class AddTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int sellerId = (int) session.getAttribute("userId"); // logged in user id

        String eventName = request.getParameter("event_name");
        String eventDate = request.getParameter("event_date");
        String price = request.getParameter("price");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO Tickets (seller_id, event_name, event_date, price) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, sellerId);
            ps.setString(2, eventName);
            ps.setString(3, eventDate);
            ps.setBigDecimal(4, new java.math.BigDecimal(price));
            ps.executeUpdate();
            response.sendRedirect("jsp/myTickets.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error adding ticket");
        }
    }
}
