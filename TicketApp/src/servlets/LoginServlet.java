package servlets;

import utils.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Both fields are required!");
            req.getRequestDispatcher("jsp/login.jsp").forward(req, res);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // SQL Server-safe query with parameterized statement
            String sql = "SELECT * FROM [Users] WHERE email = ? AND password = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password); // plain-text comparison

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // User found, start session
                        HttpSession session = req.getSession();
                        session.setAttribute("userId", rs.getInt("id"));
                        session.setAttribute("userName", rs.getString("name"));
                        session.setAttribute("userEmail", rs.getString("email"));

                        res.sendRedirect("jsp/welcome.jsp");
                        return;
                    } else {
                        // Invalid credentials
                        req.setAttribute("error", "Invalid email or password!");
                        req.getRequestDispatcher("jsp/login.jsp").forward(req, res);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong. Try again!");
            req.getRequestDispatcher("jsp/login.jsp").forward(req, res);
        }
    }
}
