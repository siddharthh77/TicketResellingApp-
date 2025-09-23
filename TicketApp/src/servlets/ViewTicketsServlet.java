package servlets;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class ViewTicketsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String[]> tickets = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketdb", "root", "password");
            PreparedStatement ps = con.prepareStatement("SELECT id, event_name, event_date, price FROM Tickets WHERE status='available'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] t = new String[4];
                t[0] = rs.getString("id");
                t[1] = rs.getString("event_name");
                t[2] = rs.getString("event_date");
                t[3] = rs.getString("price");
                tickets.add(t);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("tickets", tickets);
        RequestDispatcher rd = request.getRequestDispatcher("browseTickets.jsp");
        rd.forward(request, response);
    }
}
