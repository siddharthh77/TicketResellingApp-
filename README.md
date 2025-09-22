# TicketResellingApp-

Project setup for Java Servlets + JSP + JDBC.


TicketResellingApp/                 # Root folder (repo)
│
├── README.md                       # Explains how to run project
├── sql/                            # Database scripts
│   └── schema.sql                  # Users, Tickets, Transactions tables
│
├── src/                            # All Java code
│   ├── servlets/                   # Servlets for each module
│   │   ├── RegisterServlet.java
│   │   ├── LoginServlet.java
│   │   ├── AddTicketServlet.java
│   │   ├── EditTicketServlet.java
│   │   ├── DeleteTicketServlet.java
│   │   └── ViewTicketsServlet.java
│   │
│   ├── utils/                      # Common helper classes
│   │   └── DBConnection.java       # JDBC connection helper
│   │
│   └── model/                      # Data model classes (POJOs)
│       ├── User.java
│       └── Ticket.java
│
├── WebContent/                     # Frontend (JSP + static files)
│   ├── jsp/                        # JSP pages
│   │   ├── login.jsp
│   │   ├── register.jsp
│   │   ├── welcome.jsp
│   │   ├── viewTickets.jsp
│   │   └── postTicket.jsp
│   │
│   ├── css/                        # Stylesheets
│   │   └── style.css
│   │
│   └── js/                         # Optional JS files
│       └── scripts.js
│
└── WEB-INF/                        # Web project config
    ├── web.xml                     # Servlet mappings
    └── lib/                        # JDBC driver jar



Amith: 
