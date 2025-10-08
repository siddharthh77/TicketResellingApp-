package servlets;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PurchaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        int buyerId = (int) request.getSession().getAttribute("userId");

        try {
            // 1. Load SQL Server JDBC driver (instead of MySQL driver)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 2. Use SQL Server JDBC connection string format
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=ticketdb;user=sa;password=yourPassword;"
            );

            // 3. Start transaction (same as MySQL)
            con.setAutoCommit(false);

            // 4. Check ticket availability
            PreparedStatement ps1 = con.prepareStatement(
                "SELECT seller_id, price FROM Tickets WHERE id=? AND status='available'"
            );
            ps1.setInt(1, ticketId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int sellerId = rs.getInt("seller_id");
                double price = rs.getDouble("price");

                // 5. Update ticket status
                PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE Tickets SET status='sold' WHERE id=?"
                );
                ps2.setInt(1, ticketId);
                ps2.executeUpdate();

                // 6. Insert into Transactions
                PreparedStatement ps3 = con.prepareStatement(
                    "INSERT INTO Transactions(ticket_id, buyer_id, seller_id, price_paid) VALUES (?,?,?,?)"
                );
                ps3.setInt(1, ticketId);
                ps3.setInt(2, buyerId);
                ps3.setInt(3, sellerId);
                ps3.setDouble(4, price);
                ps3.executeUpdate();

                // 7. Commit transaction
                con.commit();

                // Close statements
                ps2.close();
                ps3.close();
            }

            // Close resources
            rs.close();
            ps1.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 8. Redirect back to view tickets
        response.sendRedirect("ViewTicketsServlet");
    }
}
