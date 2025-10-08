package servlets;

import utils.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (name == null || name.isEmpty() || 
            email == null || email.isEmpty() || 
            password == null || password.isEmpty()) {

            req.setAttribute("error", "All fields are required!");
            req.getRequestDispatcher("jsp/register.jsp").forward(req, res);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Check if email already exists
            String checkEmailSQL = "SELECT id FROM [Users] WHERE email = ?";
            try (PreparedStatement ps = con.prepareStatement(checkEmailSQL)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        req.setAttribute("error", "Email already registered!");
                        req.getRequestDispatcher("jsp/register.jsp").forward(req, res);
                        return;
                    }
                }
            }

            // Insert new user (plain-text password)
            String insertSQL = "INSERT INTO [Users] (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertSQL)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password); // plain-text
                ps.executeUpdate();
            }

            // Redirect to login page
            res.sendRedirect("jsp/login.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong. Try again!");
            req.getRequestDispatcher("jsp/register.jsp").forward(req, res);
        }
    }
}
