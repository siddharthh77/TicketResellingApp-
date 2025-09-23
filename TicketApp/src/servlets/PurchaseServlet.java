package servlets;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PurchaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        int buyerId = (int) request.getSession().getAttribute("userId");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketdb", "root", "password");
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement("SELECT seller_id, price FROM Tickets WHERE id=? AND status='available'");
            ps1.setInt(1, ticketId);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int sellerId = rs.getInt("seller_id");
                double price = rs.getDouble("price");

                PreparedStatement ps2 = con.prepareStatement("UPDATE Tickets SET status='sold' WHERE id=?");
                ps2.setInt(1, ticketId);
                ps2.executeUpdate();

                PreparedStatement ps3 = con.prepareStatement("INSERT INTO Transactions(ticket_id, buyer_id, seller_id, price_paid) VALUES (?,?,?,?)");
                ps3.setInt(1, ticketId);
                ps3.setInt(2, buyerId);
                ps3.setInt(3, sellerId);
                ps3.setDouble(4, price);
                ps3.executeUpdate();

                con.commit();
                ps2.close();
                ps3.close();
            }
            rs.close();
            ps1.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("ViewTicketsServlet");
    }
}
