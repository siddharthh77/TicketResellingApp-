-- SQL schema for Users and Tickets tables
-- Create the database (only once)
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'ticket_resell')
BEGIN
    CREATE DATABASE ticket_resell;
END
GO

USE ticket_resell;
GO

-- Drop tables if they exist (so we can reset easily during development)
IF OBJECT_ID('Transactions', 'U') IS NOT NULL DROP TABLE Transactions;
IF OBJECT_ID('Tickets', 'U') IS NOT NULL DROP TABLE Tickets;
IF OBJECT_ID('Users', 'U') IS NOT NULL DROP TABLE Users;
GO

-- Create Users table
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT GETDATE()
);
GO

-- Create Tickets table
CREATE TABLE Tickets (
    id INT IDENTITY(1,1) PRIMARY KEY,
    seller_id INT NOT NULL,
    event_name NVARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    status NVARCHAR(20) DEFAULT 'available',
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (seller_id) REFERENCES Users(id) ON DELETE CASCADE
);
GO

-- Create Transactions table
CREATE TABLE Transactions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ticket_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    price_paid DECIMAL(10,2) NOT NULL,
    purchased_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ticket_id) REFERENCES Tickets(id),
    FOREIGN KEY (buyer_id) REFERENCES Users(id),
    FOREIGN KEY (seller_id) REFERENCES Users(id)
);
GO

-- Optional sample data for testing
INSERT INTO Users (name, email, password) VALUES
('A', 'a@example.com', 'password1'),
('B', 'b@example.com', 'password2');
GO

INSERT INTO Tickets (seller_id, event_name, event_date, price)
VALUES
(1, 'College Fest', '2025-10-01', 200.00),
(1, 'Concert', '2025-10-20', 500.00);
GO
