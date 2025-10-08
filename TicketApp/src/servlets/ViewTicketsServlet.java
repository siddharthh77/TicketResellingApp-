package servlets;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class ViewTicketsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<String[]> tickets = new ArrayList<>();
        try {
            // 1. Load SQL Server JDBC driver (instead of MySQL driver)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 2. SQL Server connection URL format:
            // jdbc:sqlserver://[serverName]:[port];databaseName=[db];user=[username];password=[password];
            // Default SQL Server port is 1433
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=ticketdb;user=sa;password=yourPassword"
            );

            // 3. SQL query remains the same (SQL Server supports standard SQL)
            PreparedStatement ps = con.prepareStatement(
                "SELECT id, event_name, event_date, price FROM Tickets WHERE status='available'"
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] t = new String[4];
                t[0] = rs.getString("id");
                t[1] = rs.getString("event_name");
                t[2] = rs.getString("event_date");
                t[3] = rs.getString("price");
                tickets.add(t);
            }

            // 4. Close resources
            rs.close();
            ps.close();
            con.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 5. Forward tickets list to JSP
        request.setAttribute("tickets", tickets);
        RequestDispatcher rd = request.getRequestDispatcher("browseTickets.jsp");
        rd.forward(request, response);
    }
}
