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

<img width="880" height="119" alt="Image" src="https://github.com/user-attachments/assets/23ff0c31-fe5c-4f55-a394-17790492c8e6" />

Sreya:

<img width="872" height="183" alt="Screenshot 2025-09-22 at 10 57 05 PM" src="https://github.com/user-attachments/assets/91a095a6-1e1f-40ed-b99c-a913a3248418" />

Vamika:

<img width="873" height="163" alt="Screenshot 2025-09-22 at 10 59 09 PM" src="https://github.com/user-attachments/assets/d90f864b-ce27-4e99-8965-15e228b3b223" />

Siddharth:

<img width="876" height="162" alt="Screenshot 2025-09-22 at 11 01 24 PM" src="https://github.com/user-attachments/assets/4ef9a19d-ecea-4252-8515-22dcced4f575" />






