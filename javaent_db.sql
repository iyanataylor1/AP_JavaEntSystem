-- Create the database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS javaent_db; 
USE javaent_db;

-- Create Admin Table
CREATE TABLE IF NOT EXISTS Admin (
    adminID VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL 
);

-- Insert at least 1 record into Admin 
INSERT INTO Admin (adminID, username, password) 
VALUES ('A001', 'adminUser', 'securePass123');

-- Create Customer Table
CREATE TABLE IF NOT EXISTS Customer (
    customerID VARCHAR(50) PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    companyName VARCHAR(50), -- Optional, if the customer represents a company
    phone VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    adminID VARCHAR(50), -- FK referencing Admin
    FOREIGN KEY (adminID) REFERENCES Admin(adminID) ON DELETE SET NULL
);

-- Insert at least 2 records into Customer 
INSERT INTO Customer (customerID, firstName, lastName, companyName, phone, email, adminID) 
VALUES 
	('C001', 'Jane', 'Doe', 'Tech Solutions', '876-555-0101', 'jane.doe@example.com', 'A001'),
	('C002', 'John', 'Smith', NULL, '876-555-0202', 'john.smith@example.com', 'A001');

-- Event Table
CREATE TABLE IF NOT EXISTS Event (
    eventID VARCHAR(50) PRIMARY KEY,
    eventName VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    town VARCHAR(50) NOT NULL,
    parish VARCHAR(50) NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    customerID VARCHAR(50) NOT NULL,
    FOREIGN KEY (customerID) REFERENCES Customer(customerID) ON DELETE CASCADE
);

-- Insert at least 2 records into Event
INSERT INTO Event (eventID, eventName, street, town, parish, startDate, endDate, customerID) 
VALUES 
    ('E001', 'Tech Expo 2025', '123 Innovation Rd', 'Kingston', 'Kingston', '2025-06-15', '2025-06-16', 'C001'),
    ('E002', 'Wedding Reception', '45 Sunset Blvd', 'Montego Bay', 'St. James', '2025-07-20', '2025-07-20', 'C002');

-- EquipmentType Table (Parent of Equipment)
CREATE TABLE IF NOT EXISTS EquipmentType (
    eqTypeID VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Insert at least 2 records into EquipmentType?
INSERT INTO EquipmentType (eqTypeID, name) 
VALUES 
    ('ET001', 'Audio Equipment'),
    ('ET002', 'Lighting Equipment');

-- Equipment Table (Belongs to EquipmentType, Used in Booking)
CREATE TABLE IF NOT EXISTS Equipment (
    eqID VARCHAR(50) PRIMARY KEY,
    eqTypeID VARCHAR(50),
    name VARCHAR(50) NOT NULL,
    FOREIGN KEY (eqTypeID) REFERENCES EquipmentType(eqTypeID) ON DELETE SET NULL
);

-- Insert at least 2 records into Equipment
INSERT INTO Equipment (eqID, eqTypeID, name) 
VALUES 
    ('EQ001', 'ET001', 'Wireless Microphone'),
    ('EQ002', 'ET002', 'LED Stage Light');

-- Booking Table (Bridge between Event and Equipment)
CREATE TABLE IF NOT EXISTS Booking (
    bookingID VARCHAR(50) PRIMARY KEY,
    eventID VARCHAR(50) NOT NULL,
    equipmentID VARCHAR(50) NOT NULL,
    deliveryDate DATE NOT NULL,
    returnDate DATE NOT NULL,
    FOREIGN KEY (eventID) REFERENCES Event(eventID) ON DELETE CASCADE,
    FOREIGN KEY (equipmentID) REFERENCES Equipment(eqID) ON DELETE CASCADE
);

-- Ensure no double bookings (Constraints in DB)
DELIMITER $$

CREATE TRIGGER prevent_double_booking 
BEFORE INSERT ON Booking
FOR EACH ROW
BEGIN
    -- Check if equipment is already booked during the requested dates
    IF EXISTS (
        SELECT 1 FROM Booking 
        WHERE equipmentID = NEW.equipmentID
        AND NOT (NEW.returnDate <= deliveryDate OR NEW.deliveryDate >= returnDate)
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Equipment is already booked for these dates';
    END IF;
END$$

DELIMITER ;


-- Insert at least 2 records into Booking
INSERT INTO Booking (bookingID, eventID, equipmentID, deliveryDate, returnDate) 
VALUES 
    ('B001', 'E001', 'EQ001', '2025-06-14', '2025-06-16'),
    ('B002', 'E002', 'EQ002', '2025-07-19', '2025-07-21');

-- Invoice Table
CREATE TABLE IF NOT EXISTS Invoice (
    invoiceID VARCHAR(50) PRIMARY KEY,
    eventID VARCHAR(50) NOT NULL,
    customerID VARCHAR(50) NOT NULL,
    totalAmount DOUBLE NOT NULL,
    issueDate DATE NOT NULL,
    invoiceStatus VARCHAR(50) NOT NULL,
    FOREIGN KEY (eventID) REFERENCES Event(eventID) ON DELETE CASCADE,
    FOREIGN KEY (customerID) REFERENCES Customer(customerID) ON DELETE CASCADE
);

-- Insert at least 2 records into Invoice?
INSERT INTO Invoice (invoiceID, eventID, customerID, totalAmount, issueDate, invoiceStatus)
VALUES
	('I001', 'E001', 'C001', 8000.00, '2025-06-16', 'Pending'),
    ('I002', 'E002', 'C002', 5000.00, '2025-07-20', 'Pending');

-- Price Table (Each Equipment has multiple Prices over time)
CREATE TABLE IF NOT EXISTS Price (
    priceID VARCHAR(50) PRIMARY KEY,
    eqID VARCHAR(50) NOT NULL,booking
    ratePerHour FLOAT NOT NULL,
    rateAtDate DATE NOT NULL,
    FOREIGN KEY (eqID) REFERENCES Equipment(eqID) ON DELETE CASCADE
);

-- Insert at least 2 records into Price?
INSERT INTO Price (priceID, eqID, ratePerHour, rateAtDate)
VALUES
	('P001', 'EQ001', 100.00, '2025-03-01'),
	('P002', 'EQ002', 120.00, '2025-03-05');


-- Report Table 
CREATE TABLE IF NOT EXISTS Report (
    reportID VARCHAR(50) PRIMARY KEY,
    reportType VARCHAR(50) NOT NULL,
    dateGenerated DATE NOT NULL,
    adminID VARCHAR(50) NOT NULL,
    FOREIGN KEY (adminID) REFERENCES Admin(adminID) ON DELETE CASCADE
);

-- Insert at least 2 records into Report?
INSERT INTO Report (reportID, reportType, dateGenerated, adminID)
VALUES
	('R001', 'Revenue Report', '2025-03-15', 'A001'),
	('R002', 'Booking Report', '2025-03-15', 'A001');


/*
NOTES: 

CASCADE on DELETE: If a Customer is deleted, their associated Events and Invoices are removed. 
And, if an Event is deleted, all related Bookings and Invoices are also removed.

EquipmentType is the parent of Equipment, meaning each equipment item belongs to a type.
If an EquipmentType is deleted, the Equipment record remains, but eqTypeID becomes NULL.

Since equipment prices change over time, we store multiple prices for each eqID.
The latest price can be determined using rateAtDate.

Links Report to Admin by storing adminID in Report.
If an Admin is deleted, their reports will also be deleted (ON DELETE CASCADE).
^ Might remove.
*/