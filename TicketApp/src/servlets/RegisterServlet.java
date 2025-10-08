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

        if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "All fields are required!");
            req.getRequestDispatcher("jsp/register.jsp").forward(req, res);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            // check if email exists
            PreparedStatement ps = con.prepareStatement("SELECT id FROM Users WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                req.setAttribute("error", "Email already registered!");
                req.getRequestDispatcher("jsp/register.jsp").forward(req, res);
                return;
            }

            // insert new user
            ps = con.prepareStatement("INSERT INTO Users (name,email,password) VALUES (?,?,?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password); // ⚠️ later use hashing
            ps.executeUpdate();

            res.sendRedirect("jsp/login.jsp");
        } catch(Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong. Try again!");
            req.getRequestDispatcher("jsp/register.jsp").forward(req, res);
        }
    }
}
