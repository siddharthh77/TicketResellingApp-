<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page session="true" %>
<html>
<head><title>Welcome</title></head>
<body>
<h2>Welcome, <%= session.getAttribute("userName") %>!</h2>
<p>Your email: <%= session.getAttribute("userEmail") %></p>
<a href="login.jsp">Logout</a>
</body>
</html>

