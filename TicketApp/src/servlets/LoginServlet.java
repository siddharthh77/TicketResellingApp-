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

        if(email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Both fields are required!");
            req.getRequestDispatcher("jsp/login.jsp").forward(req, res);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Users WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password); 
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("userName", rs.getString("name"));
                session.setAttribute("userEmail", rs.getString("email"));

                res.sendRedirect("jsp/welcome.jsp");
            } else {
                req.setAttribute("error", "Invalid email or password!");
                req.getRequestDispatcher("jsp/login.jsp").forward(req, res);
            }
        } catch(Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong. Try again!");
            req.getRequestDispatcher("jsp/login.jsp").forward(req, res);
        }
    }
}
